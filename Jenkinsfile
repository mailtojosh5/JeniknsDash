pipeline {
    agent any

    stages {

        stage('Copy Cucumber Reports From Jobs') {
            steps {

                script {

                    jobs = [
                        "API-Tests",
                        "UI-Tests",
                        "Regression-Tests"
                    ]

                    for (j in jobs) {

                        echo "Copying cucumber report from ${j}"

                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber.json",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
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

                    def files = findFiles(glob: 'reports/**/cucumber.json')

                    echo "Found ${files.size()} cucumber reports"

                    files.each { file ->

                        def json = readJSON file: file.path

                        json.each { feature ->

                            feature.elements?.each { scenario ->

                                scenario.steps?.each { step ->

                                    def status = step.result?.status ?: "skipped"

                                    if(status == "passed") passed++
                                    if(status == "failed") failed++
                                    if(status == "skipped") skipped++
                                }
                            }
                        }
                    }

                    total = passed + failed + skipped
                    passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "Passed: ${passed}"
                    echo "Failed: ${failed}"
                    echo "Skipped: ${skipped}"
                    echo "Pass %: ${passPercent}"

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
<title>Cucumber Analytics Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h2>Cucumber Test Dashboard</h2>

<h3>Pass Percentage: ${data.passPercent}%</h3>

<canvas id="chart"></canvas>

<script>

new Chart(document.getElementById("chart"), {
type: "pie",
data: {
labels: ["Passed","Failed","Skipped"],
datasets: [{
data: [${data.passed},${data.failed},${data.skipped}],
backgroundColor:["green","red","gray"]
}]
}
});

</script>

<table border="1" style="margin-top:20px">

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
