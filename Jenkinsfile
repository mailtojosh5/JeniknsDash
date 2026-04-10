pipeline {
    agent any

    stages {

        stage('Define Cucumber Report URLs') {
            steps {
                script {

                    cucumberReports = [
                        [
                            name: "API Tests",
                            url: "http://jenkins/job/API-Tests/lastBuild/artifact/cucumber.json"
                        ],
                        [
                            name: "UI Tests",
                            url: "http://jenkins/job/UI-Tests/lastBuild/artifact/cucumber.json"
                        ]
                    ]

                    echo "Reports configured: ${cucumberReports.size()}"
                }
            }
        }

        stage('Download Cucumber Reports') {
            steps {
                script {

                    def passed = 0
                    def failed = 0
                    def skipped = 0

                    cucumberReports.each { report ->

                        echo "Fetching report for ${report.name}"

                        def jsonText = sh(
                            script: "curl -s '${report.url}' || echo '[]'",
                            returnStdout: true
                        ).trim()

                        def json = []

                        try {
                            json = readJSON text: jsonText
                        } catch(Exception e) {
                            echo "Invalid JSON for ${report.name}"
                            json = []
                        }

                        json.each { feature ->

                            feature.elements?.each { scenario ->

                                scenario.steps?.each { step ->

                                    def status = step.result?.status ?: "skipped"

                                    if(status == "passed") { passed++ }
                                    if(status == "failed") { failed++ }
                                    if(status == "skipped") { skipped++ }
                                }
                            }
                        }
                    }

                    total = passed + failed + skipped
                    passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "PASSED: ${passed}"
                    echo "FAILED: ${failed}"
                    echo "SKIPPED: ${skipped}"
                    echo "PASS %: ${passPercent}"

                    writeFile file: "summary.json", text: groovy.json.JsonOutput.toJson([
                        passed: passed,
                        failed: failed,
                        skipped: skipped,
                        passPercent: passPercent
                    ])
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def html = """
<html>
<head>
<title>Cucumber Test Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h2>Cucumber Test Execution Dashboard</h2>

<h3>Pass Percentage: ${data.passPercent}%</h3>

<canvas id="chart" width="400" height="400"></canvas>

<script>

new Chart(document.getElementById("chart"), {
    type: "pie",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${data.passed}, ${data.failed}, ${data.skipped}],
            backgroundColor: ["green","red","gray"]
        }]
    }
});

</script>

<table border="1" style="margin-top:30px">
<tr>
<th>Passed</th>
<th>Failed</th>
<th>Skipped</th>
<th>Pass %</th>
</tr>

<tr>
<td>${data.passed}</td>
<td>${data.failed}</td>
<td>${data.skipped}</td>
<td>${data.passPercent}%</td>
</tr>

</table>

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
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Cucumber Test Dashboard'
                ])

            }
        }

    }
}
