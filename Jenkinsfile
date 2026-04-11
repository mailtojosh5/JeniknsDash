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
<!DOCTYPE html>
<html>
<head>
<title>Cucumber Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    margin:0;
    font-family: 'Segoe UI';
    background: linear-gradient(135deg,#1f4037,#99f2c8);
    color:#fff;
}

.container {
    padding:30px;
    text-align:center;
}

h1 {
    font-size:34px;
}

.cards {
    display:flex;
    justify-content:center;
    gap:20px;
    margin:30px 0;
    flex-wrap:wrap;
}

.card {
    background: rgba(0,0,0,0.3);
    padding:20px;
    border-radius:15px;
    width:180px;
    transition:0.3s;
}

.card:hover {
    transform:translateY(-10px) scale(1.05);
}

.pass {color:#00ff9f;}
.fail {color:#ff4d4d;}
.skip {color:#ddd;}

.progress {
    width:60%;
    margin:20px auto;
    background:#ffffff33;
    border-radius:20px;
}

.bar {
    height:20px;
    width:0;
    background:linear-gradient(90deg,#00ff9f,#00c3ff);
    animation:load 2s forwards;
}

@keyframes load {
    to { width:${data.passPercent}%;}
}

.charts {
    display:flex;
    flex-wrap:wrap;
    justify-content:center;
    gap:40px;
    margin-top:40px;
}

.chart-box {
    width:300px;
}

table {
    margin:30px auto;
    border-collapse:collapse;
    width:60%;
    background:#00000033;
}

th, td {
    padding:12px;
}

th {
    background:#00000066;
}

.fade {
    animation:fadeIn 1.5s ease;
}

@keyframes fadeIn {
    from {opacity:0; transform:translateY(20px);}
    to {opacity:1;}
}
</style>
</head>

<body>

<div class="container fade">

<h1>🚀 Cucumber Test Dashboard</h1>
<h3>Pass Percentage: ${data.passPercent}%</h3>

<div class="progress">
    <div class="bar"></div>
</div>

<div class="cards">
    <div class="card">
        <h4>Passed</h4>
        <h2 class="pass">${data.passed}</h2>
    </div>

    <div class="card">
        <h4>Failed</h4>
        <h2 class="fail">${data.failed}</h2>
    </div>

    <div class="card">
        <h4>Skipped</h4>
        <h2 class="skip">${data.skipped}</h2>
    </div>
</div>

<div class="charts">

    <!-- Chart 1 -->
    <div class="chart-box">
        <canvas id="pieChart"></canvas>
    </div>

    <!-- Chart 2 -->
    <div class="chart-box">
        <canvas id="barChart"></canvas>
    </div>

    <!-- Chart 3 -->
    <div class="chart-box">
        <canvas id="doughnutChart"></canvas>
    </div>

</div>

<table>
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

</div>

<script>

// PIE CHART
new Chart(document.getElementById("pieChart"), {
    type: "pie",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${data.passed},${data.failed},${data.skipped}],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"]
        }]
    },
    options:{animation:{duration:2000}}
});

// BAR CHART
new Chart(document.getElementById("barChart"), {
    type: "bar",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            label:"Test Results",
            data: [${data.passed},${data.failed},${data.skipped}],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"]
        }]
    },
    options:{animation:{duration:2000}}
});

// DOUGHNUT CHART
new Chart(document.getElementById("doughnutChart"), {
    type: "doughnut",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${data.passed},${data.failed},${data.skipped}],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"]
        }]
    },
    options:{animation:{duration:2000}}
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
