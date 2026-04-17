pipeline {
    agent any

    stages {

        stage('Copy Reports + Collect Build Info') {
            steps {
                script {

                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]
                    def jobSummaries = []

                    for (j in jobs) {

                        echo "Processing job: ${j}"

                        def build = Jenkins.instance
                            .getItemByFullName(j)
                            ?.getLastSuccessfulBuild()

                        def buildTime = build ? 
                            new Date(build.getTimeInMillis()).format("yyyy-MM-dd HH:mm:ss") 
                            : "N/A"

                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber.json",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )

                        jobSummaries << [
                            jobName: j,
                            lastRun: buildTime,
                            reportPath: "reports/${j}/cucumber.json"
                        ]
                    }

                    writeFile file: "jobs.json",
                        text: groovy.json.JsonOutput.prettyPrint(
                            groovy.json.JsonOutput.toJson(jobSummaries)
                        )
                }
            }
        }

        stage('Parse Reports Per Job') {
            steps {
                script {

                    def jobsData = readJSON file: 'jobs.json'
                    def results = []

                    jobsData.each { job ->

                        def passed = 0
                        def failed = 0
                        def skipped = 0

                        if (fileExists(job.reportPath)) {

                            def json = readJSON file: job.reportPath

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

                        def total = passed + failed + skipped
                        def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                        results << [
                            jobName: job.jobName,
                            lastRun: job.lastRun,
                            passed: passed,
                            failed: failed,
                            skipped: skipped,
                            passPercent: passPercent
                        ]
                    }

                    writeFile file: "summary.json",
                        text: groovy.json.JsonOutput.prettyPrint(
                            groovy.json.JsonOutput.toJson(results)
                        )
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def cardsHtml = ""
                    def totalPassed = 0
                    def totalFailed = 0
                    def totalSkipped = 0

                    data.each { job ->

                        totalPassed += job.passed
                        totalFailed += job.failed
                        totalSkipped += job.skipped

                        cardsHtml += """
                        <div class="card">
                            <h3>${job.jobName}</h3>
                            <p class="date">Last Run: ${job.lastRun}</p>
                            <p>✅ Passed: ${job.passed}</p>
                            <p>❌ Failed: ${job.failed}</p>
                            <p>⏭ Skipped: ${job.skipped}</p>
                            <p class="percent">Pass %: ${job.passPercent}%</p>
                        </div>
                        """
                    }

                    def html = """
<html>
<head>
<title>Cucumber Dashboard</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    font-family: Arial;
    background: #f4f6f8;
    margin: 20px;
}

h2 {
    text-align: center;
}

.container {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
}

.card {
    background: white;
    border-radius: 10px;
    padding: 15px;
    margin: 10px;
    width: 260px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.card h3 {
    margin-top: 0;
}

.date {
    font-size: 12px;
    color: gray;
}

.percent {
    font-weight: bold;
    margin-top: 10px;
}

.chart-container {
    width: 400px;
    margin: 30px auto;
}
</style>

</head>

<body>

<h2>🚀 Cucumber Test Dashboard</h2>

<div class="container">
    ${cardsHtml}
</div>

<div class="chart-container">
    <canvas id="chart"></canvas>
</div>

<script>
new Chart(document.getElementById("chart"), {
    type: "pie",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${totalPassed}, ${totalFailed}, ${totalSkipped}],
            backgroundColor:["#28a745","#dc3545","#6c757d"]
        }]
    }
});
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
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Cucumber Dashboard'
                ])
            }
        }
    }
}
