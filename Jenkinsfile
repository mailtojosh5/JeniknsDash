pipeline {
    agent any

    stages {
        stage('Generate Client-Side Dashboard') {
            steps {
                script {
                    // The URL to the other job's HTML report
                    def reportUrl = "https://your-jenkins/job/Other-Job/lastBuild/artifact/cucumber-report.html"

                    def htmlContent = """
<html>
<head>
    <title>Executive Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: sans-serif; text-align: center; padding: 20px; }
        .loader { border: 16px solid #f3f3f3; border-top: 16px solid #3498db; border-radius: 50%; width: 50px; height: 50px; animation: spin 2s linear infinite; margin: auto; }
        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
    </style>
</head>
<body>
    <h2>Test Summary Dashboard</h2>
    <div id="loading"><p>Fetching data from report...</p><div class="loader"></div></div>
    <div id="dashboard" style="display:none;">
        <div style="width:300px; margin:auto;"><canvas id="myChart"></canvas></div>
        <h3 id="rate"></h3>
    </div>

    <script>
    async function updateDashboard() {
        try {
            // 1. Fetch the report HTML
            const response = await fetch('${reportUrl}');
            const text = await response.text();
            
            // 2. Parse the HTML using the browser's DOM parser
            const parser = new DOMParser();
            const doc = parser.parseFromString(text, 'text/html');

            // 3. Find the numbers (Adjust these selectors to match your report's HTML)
            // Example: looking for <td class="passed">
            const p = parseInt(doc.querySelector('.passed')?.innerText || 0);
            const f = parseInt(doc.querySelector('.failed')?.innerText || 0);
            const s = parseInt(doc.querySelector('.skipped')?.innerText || 0);

            const total = p + f + s;
            const percent = total > 0 ? ((p * 100) / total).toFixed(2) : 0;

            // 4. Update the UI
            document.getElementById('loading').style.display = 'none';
            document.getElementById('dashboard').style.display = 'block';
            document.getElementById('rate').innerText = 'Pass Rate: ' + percent + '%';

            new Chart(document.getElementById('myChart'), {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [p, f, s],
                        backgroundColor: ['#2ecc71', '#e74c3c', '#95a5a6']
                    }]
                }
            });
        } catch (err) {
            document.getElementById('loading').innerHTML = '<p style="color:red">Error loading report: ' + err.message + '</p>';
        }
    }

    window.onload = updateDashboard;
    </script>
</body>
</html>
"""
                    writeFile file: 'dashboard.html', text: htmlContent
                }
            }
        }

        stage('Publish') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Executive Dashboard'
                ])
            }
        }
    }
}
