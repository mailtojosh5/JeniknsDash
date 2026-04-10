pipeline {
    agent any

    stages {
        stage('Define Cucumber Report URLs') {
            steps {
                script {
                    // Using env or a script-scoped map for better reliability
                    env.REPORT_URLS = [
                        "API Tests": "http://jenkins/job/API-Tests/lastBuild/artifact/cucumber.json",
                        "UI Tests": "http://jenkins/job/UI-Tests/lastBuild/artifact/cucumber.json"
                    ]
                }
            }
        }

        stage('Process Reports') {
            steps {
                script {
                    def passed = 0
                    def failed = 0
                    def skipped = 0

                    // Note: In a real environment, ensure you have 'Pipeline Utility Steps' plugin for readJSON
                    def reports = ["API Tests": "http://jenkins/job/API-Tests/lastBuild/artifact/cucumber.json", "UI Tests": "http://jenkins/job/UI-Tests/lastBuild/artifact/cucumber.json"]
                    
                    reports.each { name, url ->
                        echo "Fetching report for ${name}"
                        def jsonText = sh(script: "curl -s '${url}' || echo '[]'", returnStdout: true).trim()
                        
                        try {
                            def json = readJSON text: jsonText
                            json.each { feature ->
                                feature.elements?.each { scenario ->
                                    scenario.steps?.each { step ->
                                        def status = step.result?.status ?: "skipped"
                                        if(status == "passed") { passed++ }
                                        else if(status == "failed") { failed++ }
                                        else { skipped++ }
                                    }
                                }
                            }
                        } catch(Exception e) {
                            echo "Could not process ${name}: ${e.message}"
                        }
                    }

                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    def summary = [passed: passed, failed: failed, skipped: skipped, passPercent: passPercent]
                    writeJSON file: 'summary.json', json: summary
                    
                    // Generate HTML
                    def html = """
                    <html>
                    <head>
                        <title>Dashboard</title>
                        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                    </head>
                    <body>
                        <h2>Test Results: ${passPercent}% Pass</h2>
                        <div style="width:400px; height:400px;"><canvas id="chart"></canvas></div>
                        <script>
                            window.onload = function() {
                                new Chart(document.getElementById("chart"), {
                                    type: "pie",
                                    data: {
                                        labels: ["Passed","Failed","Skipped"],
                                        datasets: [{
                                            data: [${passed}, ${failed}, ${skipped}],
                                            backgroundColor: ["#2ecc71","#e74c3c","#95a5a6"]
                                        }]
                                    }
                                });
                            };
                        </script>
                    </body>
                    </html>
                    """
                    writeFile file: "dashboard.html", text: html
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
                    reportName: 'Cucumber Test Dashboard'
                ])
            }
        }
    }
}
