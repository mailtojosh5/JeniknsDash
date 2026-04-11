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

        stage('Parse Cucumber Reports (Per Job Analytics)') {
            steps {
                script {

                    def jobResults = []

                    def files = findFiles(glob: 'reports/**/cucumber.json')

                    echo "Found ${files.size()} cucumber reports"

                    files.each { file ->

                        def jobName = file.path.tokenize('/')[1]

                        def json = []

                        try {
                            json = readJSON file: file.path
                        } catch(Exception e) {
                            echo "Invalid JSON in ${file.path}"
                            json = []
                        }

                        def passed = 0
                        def failed = 0
                        def skipped = 0

                        json.each { feature ->
                            feature.elements?.each { scenario ->
                                scenario.steps?.each { step ->

                                    def status = step.result?.status ?: "skipped"

                                    if(status == "passed") passed++
                                    else if(status == "failed") failed++
                                    else skipped++
                                }
                            }
                        }

                        def total = passed + failed + skipped

                        // SAFE Jenkins-compatible percentage (NO round(), NO Math.round)
                        def passPercent = total > 0 ?
                            (((passed * 100.0) / total) * 100 as int) / 100.0
                            : 0

                        jobResults << [
                            name: jobName,
                            passed: passed,
                            failed: failed,
                            skipped: skipped,
                            passPercent: passPercent
                        ]

                        echo "${jobName} -> Passed:${passed}, Failed:${failed}, Skipped:${skipped}, Pass%:${passPercent}"
                    }

                    writeFile file: "summary.json",
                        text: groovy.json.JsonOutput.toJson(jobResults)
                }
            }
        }

        stage('Generate Dashboard (Multi-Job Charts)') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def charts = ""

                    data.each { job ->

                        charts += """
                        <div style="margin-bottom:40px;">
                            <h2>${job.name}</h2>
                            <h3>Pass Percentage: ${job.passPercent}%</h3>

                            <canvas id="chart_${job.name}" width="400" height="300"></canvas>

                            <script>
                                new Chart(document.getElementById("chart_${job.name}"), {
                                    type: "pie",
                                    data: {
                                        labels: ["Passed","Failed","Skipped"],
                                        datasets: [{
                                            data: [${job.passed}, ${job.failed}, ${job.skipped}],
                                            backgroundColor: ["green","red","gray"]
                                        }]
                                    }
                                });
                            </script>
                        </div>
                        <hr/>
                        """
                    }

                    def html = """
<html>
<head>
<title>Cucumber Analytics Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h1>🚀 Cucumber Multi-Job Dashboard</h1>

${charts}

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
                    reportName: 'Cucumber Multi-Job Dashboard'
                ])
            }
        }

    }
}
