'use strict';
require('dotenv').config();

// ── Bootstrap global proxy BEFORE any other imports ─────────────
// global-agent patches Node's built-in http/https modules so they
// honour HTTP_PROXY / HTTPS_PROXY / NO_PROXY env vars exactly like
// curl does — no manual agent wiring needed.
const { bootstrap } = require('global-agent');
bootstrap();

// Log which proxy is active (same vars curl uses)
const activeProxy = process.env.HTTPS_PROXY || process.env.https_proxy
                 || process.env.HTTP_PROXY  || process.env.http_proxy
                 || '';
if (activeProxy) {
  console.log(`[proxy] global-agent active — routing via: ${activeProxy}`);
} else {
  console.log('[proxy] No proxy env vars set — connecting directly');
}

const express = require('express');
const fetch   = require('node-fetch');
const path    = require('path');

const app  = express();
const PORT = process.env.PORT || 3000;

// ── Xray Cloud endpoints ────────────────────────────────────────
const XRAY_AUTH_URL = 'https://xray.cloud.getxray.app/api/v2/authenticate';
const XRAY_GQL_URL  = 'https://xray.cloud.getxray.app/api/v2/graphql';

// ── Helpers ─────────────────────────────────────────────────────
// No manual agent needed — global-agent handles it transparently.
function fetchOpts(body, token) {
  const headers = { 'Content-Type': 'application/json' };
  if (token) headers['Authorization'] = 'Bearer ' + token;
  return {
    method: 'POST',
    headers,
    body: JSON.stringify(body),
  };
}

async function getXrayToken() {
  const res = await fetch(XRAY_AUTH_URL, fetchOpts({
    client_id:     process.env.XRAY_CLIENT_ID,
    client_secret: process.env.XRAY_CLIENT_SECRET,
  }));
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Xray auth failed (${res.status}): ${text}`);
  }
  // Response is a quoted JSON string — strip quotes
  const raw = await res.text();
  return raw.replace(/^"|"$/g, '');
}

async function xrayGql(token, query, variables = {}) {
  const res = await fetch(XRAY_GQL_URL, fetchOpts({ query, variables }, token));
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`Xray GraphQL error (${res.status}): ${text}`);
  }
  const json = await res.json();
  if (json.errors && json.errors.length) {
    throw new Error(json.errors.map(e => e.message).join('; '));
  }
  return json.data;
}

// ── Data fetcher for one project/space ─────────────────────────
async function fetchSpace(token, project, days) {
  const since = new Date(Date.now() - days * 86400000).toISOString().slice(0, 10);

  const data = await xrayGql(token, `
    query($project: String!, $since: String!) {
      getTestExecutions(jql: "project = $project AND created >= $since", limit: 100) {
        results {
          issueId
          jira { summary key }
          testPlans(limit: 5) {
            results { jira { key summary } }
          }
          testRuns(limit: 200) {
            results {
              id
              status { name }
              test { jira { key summary } }
              assignee { displayName }
              defects { id summary status { name } }
              finishedOn
              startedOn
            }
          }
        }
      }
      getRequirements(
        jql: "project = $project AND issueType in (Story, Requirement, Epic)"
        limit: 200
      ) {
        results {
          jira { key summary }
          testCoverage { status }
        }
      }
    }
  `, { project, since });

  return buildSpaceModel(data, project);
}

function buildSpaceModel(raw, project) {
  const executions   = raw?.getTestExecutions?.results  || [];
  const requirements = raw?.getRequirements?.results    || [];
  const runs = [];
  const plansMap = {};

  executions.forEach(exec => {
    const execKey     = exec.jira?.key || exec.issueId;
    const execSummary = exec.jira?.summary || execKey;
    const planKeys    = (exec.testPlans?.results || []).map(p => p.jira?.key).filter(Boolean);

    planKeys.forEach(pk => {
      if (!plansMap[pk]) {
        const pi = exec.testPlans.results.find(p => p.jira?.key === pk);
        plansMap[pk] = { key: pk, summary: pi?.jira?.summary || pk, executions: [] };
      }
      if (!plansMap[pk].executions.find(e => e.key === execKey)) {
        plansMap[pk].executions.push({ key: execKey, summary: execSummary });
      }
    });

    (exec.testRuns?.results || []).forEach(run => {
      runs.push({
        testKey:     run.test?.jira?.key     || '?',
        testSummary: run.test?.jira?.summary || '(unknown)',
        status:      (run.status?.name || 'TODO').toUpperCase(),
        execKey,
        execSummary,
        assignee:    run.assignee?.displayName || 'Unassigned',
        defects:     (run.defects || []).map(d => ({
          key:     d.id      || '?',
          summary: d.summary || '',
          status:  d.status?.name || 'Open',
        })),
        finishedOn: run.finishedOn ? new Date(run.finishedOn).getTime() : null,
        planKeys,
      });
    });
  });

  return {
    project,
    runs,
    plansMap: Object.values(plansMap),
    requirements,
    fetchedAt: new Date().toISOString(),
  };
}

// ── Express routes ──────────────────────────────────────────────
app.use(express.static(path.join(__dirname, '..', 'public')));

// Health check — Jenkins can poll this
app.get('/health', (_req, res) => res.json({ status: 'ok', ts: new Date().toISOString() }));

// Main data API — called by the HTML page on load
app.get('/api/report', async (req, res) => {
  try {
    const projectKeys = (req.query.projects || process.env.PROJECT_KEYS || '')
      .split(',').map(s => s.trim().toUpperCase()).filter(Boolean);
    const days = parseInt(req.query.days || process.env.DATE_RANGE_DAYS || '30', 10);

    if (!projectKeys.length) {
      return res.status(400).json({ error: 'No project keys specified. Set PROJECT_KEYS in .env or pass ?projects=KEY1,KEY2' });
    }
    if (!process.env.XRAY_CLIENT_ID || !process.env.XRAY_CLIENT_SECRET) {
      return res.status(500).json({ error: 'XRAY_CLIENT_ID / XRAY_CLIENT_SECRET not set in environment' });
    }

    console.log(`[report] Fetching ${projectKeys.join(', ')} — last ${days} days`);
    const token     = await getXrayToken();
    const spaceData = await Promise.all(projectKeys.map(p => fetchSpace(token, p, days)));

    res.json({ ok: true, days, projects: projectKeys, data: spaceData });
  } catch (err) {
    console.error('[report] Error:', err.message);
    res.status(500).json({ error: err.message });
  }
});

// ── Start ───────────────────────────────────────────────────────
app.listen(PORT, () => {
  console.log(`\n🟢  Xray Report Server running at http://localhost:${PORT}`);
  console.log(`    Dashboard : http://localhost:${PORT}/`);
  console.log(`    API       : http://localhost:${PORT}/api/report`);
  console.log(`    Health    : http://localhost:${PORT}/health\n`);
});
