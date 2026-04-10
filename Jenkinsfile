pipeline {
    agent any

    stages {
        stage('Generate Dashboard') {
            steps {
                script {
                    // 1. Read the existing report from the workspace 
                    // (Ensure the report is in the current workspace or use a full path)
                    def reportPath = "cucumber-report.html" 
                    def rawHtml = ""
                    
                    if (fileExists(reportPath)) {
                        rawHtml = readFile(reportPath).replaceAll("`", "'") // Escape backticks
                    } else {
                        rawHtml = "<html><body><span class='passed'>0</span><span class='failed'>0</span></body></html>"
                    }

                    // 2. Create the Dashboard HTML
                    def dashboardContent = """
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: sans-serif; text-align: center; }
        #source-data { display: none; } /* Hide the original report data */
        .chart-box { width: 350px; margin: auto; }
    </style>
</head>
<body>
    <h2>Executive Test Summary</h2>
    
    <div id="source-data">
        ${rawHtml}
    </div>

    <div class="chart-box">
        <canvas id="myChart"></canvas>
    </div>
    <h3 id="stat-text">Loading Stats...</h3>

    <script>
        function initDashboard() {
            // JavaScript searches the 'source-data' div for the numbers
            // Adjust the '.passed' and '.failed' selectors to match your report's HTML tags
            const p = parseInt(document.querySelector('#source-data .passed')?.innerText || 0);
            const f = parseInt(document.querySelector('#source-data .failed')?.innerText || 0);
            const s = parseInt(document.querySelector('#source-data .skipped')?.innerText || 0);

            const total = p + f + s;
            const rate = total > 0 ? ((p * 100) / total).toFixed(2) : 0;

            document.getElementById('stat-text').innerText = 'Pass Rate: ' + rate + '% (' + total + ' total steps)';

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
        }

        window.onload = initDashboard;
    </script>
</body>
</html>
"""
                    writeFile file: 'dashboard.html', text: dashboardContent
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
