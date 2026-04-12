pipeline {
    agent any

    stages {
        stage('Copy Cucumber Reports From Jobs') {
            steps {
                script {
                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]
                    
                    jobs.each { jobName ->
                        echo "🔄 Copying cucumber report from ${jobName}..."
                        copyArtifacts(
                            projectName: jobName,
                            selector: lastSuccessful(),           // Fixed: it's a function call
                            filter: "**/cucumber.json",
                            target: "reports/${jobName}",
                            flatten: true,
                            optional: true,
                            fingerprintArtifacts: true
                        )
                    }
                }
            }
        }

        stage('Parse Cucumber Reports') {
            steps {
                script {
                    def passed = 0
                    def failed = 0
                    def skipped = 0
                    def totalSteps = 0

                    def files = findFiles(glob: 'reports/**/cucumber.json')
                    echo "📊 Found ${files.size()} cucumber report files"

                    files.each { file ->
                        echo "Processing: ${file.path}"
                        def json = readJSON(file: file.path)

                        // Cucumber JSON can be array of features or wrapped
                        def features = json instanceof List ? json : [json]

                        features.each { feature ->
                            feature.elements?.each { scenario ->
                                scenario.steps?.each { step ->
                                    def status = step.result?.status?.toLowerCase() ?: "skipped"
                                    totalSteps++

                                    switch(status) {
                                        case "passed":
                                            passed++
                                            break
                                        case "failed":
                                            failed++
                                            break
                                        case "skipped":
                                        case "undefined":
                                            skipped++
                                            break
                                    }
                                }
                            }
                        }
                    }

                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? Math.round((passed * 10000.0) / total) / 100 : 0.0

                    echo "✅ Passed: ${passed}"
                    echo "❌ Failed: ${failed}"
                    echo "⏭️  Skipped: ${skipped}"
                    echo "📈 Pass Rate: ${passPercent}%"

                    // Save summary
                    def summary = [
                        passed     : passed,
                        failed     : failed,
                        skipped    : skipped,
                        total      : total,
                        passPercent: passPercent,
                        timestamp  : new Date().toString()
                    ]

                    writeJSON file: 'summary.json', json: summary, pretty: 4
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {
                    def data = readJSON(file: 'summary.json')

                    def html = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cucumber Test Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <style>
        :root {
            --primary: #4a9eff;
            --success: #22c55e;
            --danger: #ef4444;
            --warning: #eab308;
        }
        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a, #1e2937);
            color: #e2e8f0;
            margin: 0;
            padding: 20px;
            line-height: 1.6;
        }
        .container {
            max-width: 1100px;
            margin: 0 auto;
        }
        h1 {
            text-align: center;
            color: white;
            margin-bottom: 10px;
            font-size: 2.8rem;
        }
        .subtitle {
            text-align: center;
            color: #94a3b8;
            font-size: 1.2rem;
            margin-bottom: 40px;
        }
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }
        .card {
            background: rgba(255,255,255,0.08);
            border-radius: 16px;
            padding: 24px;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255,255,255,0.1);
        }
        .card h3 {
            margin: 0 0 8px 0;
            font-size: 1.1rem;
            color: #94a3b8;
        }
        .big-number {
            font-size: 2.8rem;
            font-weight: 700;
        }
        .chart-container {
            background: rgba(255,255,255,0.06);
            border-radius: 16px;
            padding: 30px;
            margin-bottom: 40px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background: rgba(255,255,255,0.06);
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        }
        th, td {
            padding: 18px;
            text-align: center;
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }
        th {
            background: rgba(74, 158, 255, 0.15);
            color: #60a5fa;
        }
        .pass { color: var(--success); font-weight: bold; }
        .fail { color: var(--danger); font-weight: bold; }
        footer {
            text-align: center;
            margin-top: 50px;
            color: #64748b;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🥒 Cucumber Test Analytics</h1>
        <p class="subtitle">Automated Test Results Dashboard • ${data.timestamp}</p>

        <div class="stats">
            <div class="card">
                <h3>Passed</h3>
                <div class="big-number pass">${data.passed}</div>
            </div>
            <div class="card">
                <h3>Failed</h3>
                <div class="big-number fail">${data.failed}</div>
            </div>
            <div class="card">
                <h3>Skipped</h3>
                <div class="big-number">${data.skipped}</div>
            </div>
            <div class="card">
                <h3>Pass Rate</h3>
                <div class="big-number" style="color: ${data.passPercent > 85 ? '#22c55e' : data.passPercent > 70 ? '#eab308' : '#ef4444'}">
                    ${data.passPercent}%
                </div>
            </div>
        </div>

        <div class="chart-container">
            <canvas id="pieChart" height="120"></canvas>
        </div>

        <table>
            <tr>
                <th>Status</th>
                <th>Count</th>
                <th>Percentage</th>
            </tr>
            <tr>
                <td class="pass">✅ Passed</td>
                <td>${data.passed}</td>
                <td>${data.passPercent}%</td>
            </tr>
            <tr>
                <td class="fail">❌ Failed</td>
                <td>${data.failed}</td>
                <td>${data.total > 0 ? ((data.failed * 100.0 / data.total).round(2)) : 0}%</td>
            </tr>
            <tr>
                <td>⏭️ Skipped</td>
                <td>${data.skipped}</td>
                <td>${data.total > 0 ? ((data.skipped * 100.0 / data.total).round(2)) : 0}%</td>
            </tr>
            <tr style="font-weight:bold; background:rgba(255,255,255,0.1);">
                <td>Total Steps</td>
                <td colspan="2">${data.total}</td>
            </tr>
        </table>
    </div>

    <footer>
        Generated by Jenkins Pipeline • ${new Date().format("yyyy-MM-dd HH:mm")}
    </footer>

    <script>
        const ctx = document.getElementById('pieChart');

        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Passed', 'Failed', 'Skipped'],
                datasets: [{
                    data: [${data.passed}, ${data.failed}, ${data.skipped}],
                    backgroundColor: ['#22c55e', '#ef4444', '#64748b'],
                    borderColor: '#1e2937',
                    borderWidth: 4,
                    hoverOffset: 20
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { color: '#e2e8f0', padding: 20, font: {size: 15} }
                    },
                    tooltip: {
                        backgroundColor: 'rgba(15,23,42,0.95)',
                        titleFont: {size: 16},
                        bodyFont: {size: 15}
                    }
                }
            }
        });
    </script>
</body>
</html>
"""
                    writeFile file: 'dashboard.html', text: html
                }
            }
        }

        stage('Publish Dashboard') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Cucumber Test Dashboard',
                    reportTitles: 'Cucumber Analytics'
                ])
            }
        }
    }
}
