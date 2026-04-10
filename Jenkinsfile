pipeline {
    agent any

    stages {
        stage('Generate Test Dashboard') {
            steps {
                script {
                    // 1. Setup report sources
                    def reportLinks = [
                        [name: "API Tests", url: "http://jenkins/job/API-Tests/lastBuild/artifact/cucumber.json"],
                        [name: "UI Tests", url: "http://jenkins/job/UI-Tests/lastBuild/artifact/cucumber.json"]
                    ]

                    def passed = 0
                    def failed = 0
                    def skipped = 0

                    // 2. Fetch and Count
                    reportLinks.each { report ->
                        echo "Fetching: ${report.name}"
                        
                        // -f fails on 404, -L follows redirects, -s is silent
                        def jsonText = sh(
                            script: "curl -s -f -L '${report.url}' || echo ''",
                            returnStdout: true
                        ).trim()

                        if (jsonText) {
                            // Counting occurrences directly in the string
                            passed += jsonText.count('"status":"passed"')
                            failed += jsonText.count('"status":"failed"')
                            skipped += jsonText.count('"status":"skipped"')
                        } else {
                            echo "WARNING: Could not retrieve data for ${report.name}"
                        }
                    }

                    // 3. Calculate Percentage
                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "Final Results - Passed: ${passed}, Failed: ${failed}, Skipped: ${skipped}"

                    // 4. Generate the HTML Dashboard
                    def htmlContent = """
<html>
<head>
    <title>Cucumber Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 300px; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 10px; text-align: center; }
        th { background-color: #f4f4f4; }
        .summary { margin-bottom: 20px; }
    </style>
</head>
<body>
    <h2>Cucumber Test Execution Dashboard</h2>
    <div class="summary">
        <strong>Total Steps:</strong> ${total} <br>
        <strong>Pass Percentage:</strong> ${passPercent}%
    </div>

    <div style="width: 350px; height: 350px;">
        <canvas id="statusChart"></canvas>
    </div>

    <table>
        <tr><th>Passed</th><th>Failed</th><th>Skipped</th></tr>
        <tr>
            <td style="color:green"><strong>${passed}</strong></td>
            <td style="color:red"><strong>${failed}</strong></td>
            <td style="color:gray"><strong>${skipped}</strong></td>
        </tr>
    </table>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const ctx = document.getElementById('statusChart').getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [${passed}, ${failed}, ${skipped}],
                        backgroundColor: ['#2ecc71', '#e74c3c', '#95a5a6']
                    }]
                },
                options: { responsive: true }
            });
        });
    </script>
</body>
</html>
"""
                    writeFile file: 'dashboard.html', text: htmlContent
                }
            }
        }

        stage('Publish Results') {
            steps {
                // Fixed publishHTML with all required parameters
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Cucumber Test Dashboard'
                ])
            }
        }
    }
}
