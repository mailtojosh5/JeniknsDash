pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Copy Cucumber Reports From Jobs') {
            steps {
                script {

                    def jobs = [
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

                    int passed = 0
                    int failed = 0
                    int skipped = 0

                    def files = findFiles(glob: 'reports/**/cucumber.json')

                    echo "Found ${files.size()} cucumber reports"

                    files.each { file ->

                        def json = readJSON file: file.path

                        json.each { feature ->

                            feature.elements?.each { scenario ->

                                scenario.steps?.each { step ->

                                    def status = step.result?.status ?: "skipped"

                                    switch(status) {
                                        case "passed": passed++; break
                                        case "failed": failed++; break
                                        default: skipped++; break
                                    }
                                }
                            }
                        }
                    }

                    int total = passed + failed + skipped
                    double passPercent = total > 0 ? ((passed * 100.0) / total).round(2) : 0

                    echo "Passed: ${passed}"
                    echo "Failed: ${failed}"
                    echo "Skipped: ${skipped}"
                    echo "Pass %: ${passPercent}"

                    def summary = [
                        passed: passed,
                        failed: failed,
                        skipped: skipped,
                        passPercent: passPercent
                    ]

                    writeFile file: "summary.json",
                        text: groovy.json.JsonOutput.prettyPrint(
                            groovy.json.JsonOutput.toJson(summary)
                        )

                    sh "cat summary.json"
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    // Defensive fix (in case anything becomes array)
                    def passed = (data.passed instanceof List) ? data.passed.sum() : data.passed
                    def failed = (data.failed instanceof List) ? data.failed.sum() : data.failed
                    def skipped = (data.skipped instanceof List) ? data.skipped.sum() : data.skipped
                    def percent = data.passPercent

                    def html = """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Cucumber Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    margin:0;
    font-family: 'Segoe UI', sans-serif;
    background: linear-gradient(135deg, #0f2027, #203a43, #2c5364);
    color: white;
}

.container {
    padding: 30px;
    text-align: center;
}

h1 {
    font-size: 32px;
    margin-bottom: 5px;
}

.cards {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin: 30px 0;
    flex-wrap: wrap;
}

.card {
    background: rgba(255,255,255,0.08);
    backdrop-filter: blur(12px);
    padding: 20px;
    border-radius: 15px;
    width: 180px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.4);
    transition: all 0.3s ease;
}

.card:hover {
    transform: translateY(-10px) scale(1.05);
}

.card h2 {
    font-size: 30px;
}

.pass { color: #00ff9f; }
.fail { color: #ff4d4d; }
.skip { color: #cccccc; }

.progress-container {
    width: 60%;
    margin: 20px auto;
    background: rgba(255,255,255,0.2);
    border-radius: 20px;
    overflow: hidden;
}

.progress-bar {
    height: 18px;
    width: 0;
    background: linear-gradient(90deg, #00ff9f, #00c3ff);
    animation: loadBar 2s forwards;
}

@keyframes loadBar {
    to { width: ${percent}%; }
}

.chart-container {
    width: 400px;
    margin: 40px auto;
}

table {
    margin: 30px auto;
    border-collapse: collapse;
    width: 60%;
    background: rgba(255,255,255,0.08);
    border-radius: 10px;
    overflow: hidden;
}

th, td {
    padding: 12px;
}

th {
    background: rgba(0,0,0,0.4);
}

tr:hover {
    background: rgba(255,255,255,0.1);
}

.fade {
    animation: fadeIn 1.5s ease;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px);}
    to { opacity: 1; transform: translateY(0);}
}
</style>
</head>

<body>

<div class="container fade">

<h1>🚀 Cucumber Test Dashboard</h1>
<h3>Pass Percentage: ${percent}%</h3>

<div class="progress-container">
    <div class="progress-bar"></div>
</div>

<div class="cards">
    <div class="card">
        <h4>Passed</h4>
        <h2 class="pass">${passed}</h2>
    </div>

    <div class="card">
        <h4>Failed</h4>
        <h2 class="fail">${failed}</h2>
    </div>

    <div class="card">
        <h4>Skipped</h4>
        <h2 class="skip">${skipped}</h2>
    </div>
</div>

<div class="chart-container">
    <canvas id="chart"></canvas>
</div>

<table>
<tr>
<th>Passed</th>
<th>Failed</th>
<th>Skipped</th>
<th>Pass %</th>
</tr>

<tr>
<td>${passed}</td>
<td>${failed}</td>
<td>${skipped}</td>
<td>${percent}%</td>
</tr>
</table>

</div>

<script>
new Chart(document.getElementById("chart"), {
    type: "doughnut",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${passed},${failed},${skipped}],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"]
        }]
    },
    options: {
        animation: { duration: 2000 },
        plugins: {
            legend: {
                labels: { color: "white" }
            }
        }
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
                    reportName: 'Cucumber Test Dashboard'
                ])
            }
        }
    }
}
