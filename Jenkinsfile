pipeline {
    agent any

    stages {
        stage('Process Cucumber Reports') {
            steps {
                script {
                    // 1. Initialize a Map to hold data across the script
                    def stats = [passed: 0, failed: 0, skipped: 0, passPercent: 0]
                    
                    def reportLinks = [
                        [name: "API Tests", url: "http://jenkins/job/API-Tests/lastBuild/artifact/cucumber.json"],
                        [name: "UI Tests", url: "http://jenkins/job/UI-Tests/lastBuild/artifact/cucumber.json"]
                    ]

                    reportLinks.each { report ->
                        echo "Fetching: ${report.name}"
                        
                        // Use -f to fail on 404/500 and -L to follow redirects
                        def jsonText = sh(
                            script: "curl -s -f -L '${report.url}' || echo '[]'",
                            returnStdout: true
                        ).trim()

                        // Validate if it's actually JSON (starts with [ or {)
                        if (jsonText.startsWith("[") || jsonText.startsWith("{")) {
                            try {
                                def json = readJSON text: jsonText
                                json.each { feature ->
                                    feature.elements?.each { scenario ->
                                        scenario.steps?.each { step ->
                                            def status = step.result?.status ?: "skipped"
                                            if (status == "passed") stats.passed++
                                            else if (status == "failed") stats.failed++
                                            else stats.skipped++
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                echo "Failed to parse JSON for ${report.name}: ${e.message}"
                            }
                        } else {
                            echo "Skipping ${report.name}: Response was not valid JSON."
                        }
                    }

                    // 2. Calculate Totals
                    def total = stats.passed + stats.failed + stats.skipped
                    stats.passPercent = total > 0 ? ((stats.passed * 100) / total).round(2) : 0

                    // 3. Generate HTML with explicit variable injection
                    def htmlContent = """
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
    <h2>Test Results</h2>
    <p><strong>Pass Rate: ${stats.passPercent}%</strong></p>
    <div style="width:300px;"><canvas id="myChart"></canvas></div>
    <table border="1">
        <tr><th>Passed</th><th>Failed</th><th>Skipped</th></tr>
        <tr><td>${stats.passed}</td><td>${stats.failed}</td><td>${stats.skipped}</td></tr>
    </table>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            new Chart(document.getElementById('myChart'), {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [${stats.passed}, ${stats.failed}, ${stats.skipped}],
                        backgroundColor: ['#2ecc71', '#e74c3c', '#95a5a6']
                    }]
                }
            });
        });
    </script>
</body>
</html>
"""
                    writeFile file: "dashboard.html", text: htmlContent
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
                    reportName: 'Cucumber Test Dashboard'
                ])
            }
        }
    }
}
