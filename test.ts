import axios from 'axios';

const XRAY_CLIENT_ID = 'XXX';
const XRAY_CLIENT_SECRET = 'XXX';

async function main() {
  // 1. Get auth token
  const authRes = await axios.post(
    'https://xray.cloud.getxray.app/api/v2/authenticate',
    {
      client_id: XRAY_CLIENT_ID,
      client_secret: XRAY_CLIENT_SECRET,
    }
  );

  const token = authRes.data;

  // 2. Update test result (this "overwrites" the test run)
  const payload = {
    testExecutionKey: 'XSP-61',
    tests: [
      {
        testKey: 'XSP-63',
        status: 'PASSED', // PASSED | FAILED | TODO | EXECUTING
        comment: 'Updated via API',
      },
    ],
  };

  const res = await axios.post(
    'https://xray.cloud.getxray.app/api/v2/import/execution',
    payload,
    {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    }
  );

  console.log(res.data);
}

main().catch(console.error);
