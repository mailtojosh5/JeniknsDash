pipeline {
    agent any

    stages {

        stage('Copy Reports') {
            steps {
                script {

                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]

                    for (j in jobs) {

                        echo "Copying reports from ${j}"

                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber.json, **/cucumber.html",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )
                    }
                }
            }
        }

        stage('Parse Reports + Build Summary') {
            steps {
                script {

                    def results = []

                    // ✅ Extract "X days ago" OR fallback timestamp
                    def extractRunDateFromHtml = { filePath ->

                        def html = readFile(filePath)

                        // ✅ 1. Relative time (PRIMARY)
                        def relative = (html =~ /([0-9]+\s+(seconds?|minutes?|hours?|days?|months?|years?)\s+ago)/)
                        if (relative.find()) {
                            return relative.group(1)
                        }

                        // ✅ 2. ISO timestamp fallback
                        def iso = (html =~ /([0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2})/)
                        if (iso.find()) {
                            return iso.group(1).replace("T", " ")
                        }

                        // ✅ 3. Standard timestamp fallback
                        def standard = (html =~ /([0-9]{4}-[0-9]{2}-[0-9]{2}\s[0-9]{2}:[0-9]{2}:[0-9]{2})/)
                        if (standard.find()) {
                            return standard.group(1)
                        }

                        return "N/A"
                    }

                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]

                    jobs.each { jobName ->

                        def passed = 0
                        def failed = 0
                        def skipped = 0

                        def jsonFile = "reports/${jobName}/cucumber.json"
                        def htmlFile = "reports/${jobName}/cucumber.html"

                        def lastRun = "N/A"

                        // ✅ Get last run from HTML
                        if (fileExists(htmlFile)) {
                            lastRun = extractRunDateFromHtml(htmlFile)
                        }

                        // ✅ Parse JSON
                        if (fileExists(jsonFile)) {

                            def json = readJSON file: jsonFile

                            json.each { feature ->
                                feature.elements?.each { scenario ->
                                    scenario.steps?.each { step ->

                                        def status = step.result?.status ?: "skipped"

                                        if (status == "passed") passed++
                                        if (status == "failed") failed++
                                        if (status == "skipped") skipped++
                                    }
                                }
                            }
                        }

                        def total = passed + failed + skipped
                        def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                        results << [
                            jobName: jobName,
                            lastRun: lastRun,
                            passed: passed,
                            failed: failed,
                            skipped: skipped,
                            passPercent: passPercent
                        ]
                    }

                    writeJSON file: "summary.json", json: results
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def cards = ""
                    def totalPassed = 0
                    def totalFailed = 0
                    def totalSkipped = 0

                    data.each { job ->

                        totalPassed += job.passed
                        totalFailed += job.failed
                        totalSkipped += job.skipped

                        cards += """
                        <div class="card">
                            <h3>${job.jobName}</h3>
                            <p class="meta">🕒 Last Run: <b>${job.lastRun}</b></p>

                            <p>✅ Passed: ${job.passed}</p>
                            <p>❌ Failed: ${job.failed}</p>
                            <p>⏭ Skipped: ${job.skipped}</p>

                            <h4>Pass Rate: ${job.passPercent}%</h4>
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
    margin: 0;
    padding: 20px;
}

h2 {
    text-align: center;
}

.container {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 15px;
}

.card {
    background: white;
    width: 260px;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.card h3 {
    margin: 0 0 10px 0;
}

.meta {
    font-size: 13px;
    color: #555;
}

h4 {
    margin-top: 10px;
}

.chart-box {
    width: 420px;
    margin: 30px auto;
}
</style>

</head>

<body>

<h2>🚀 Cucumber Test Dashboard</h2>

<div class="container">
    ${cards}
</div>

<div class="chart-box">
    <canvas id="chart"></canvas>
</div>

<script>
new Chart(document.getElementById("chart"), {
    type: "pie",
    data: {
        labels: ["Passed", "Failed", "Skipped"],
        datasets: [{
            data: [${totalPassed}, ${totalFailed}, ${totalSkipped}],
            backgroundColor: ["#28a745", "#dc3545", "#6c757d"]
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
