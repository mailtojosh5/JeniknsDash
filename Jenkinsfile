pipeline {
    agent any

    stages {

        stage('Discover Cucumber Reports From Workspaces') {
            steps {
                script {

                    workspaceRoot = "/opt/jenkins/workspace"

                    sh "rm -rf reports"
                    sh "mkdir -p reports"

                    def files = sh(
                        script: """
                        find ${workspaceRoot} -type f -iname "*cucumber*.json" 2>/dev/null
                        """,
                        returnStdout: true
                    ).trim().split("\\n")

                    echo "Found ${files.size()} cucumber reports"

                    def index = 0

                    files.each { file ->

                        if(file?.trim()) {

                            echo "Copying ${file}"

                            def jobName = file.tokenize("/")[4]   // workspace/jobName/...

                            sh "cp '${file}' reports/${jobName}_${index}.json"

                            index++
                        }
                    }

                    sh "ls -R reports"
                }
            }
        }

        stage('Parse Reports') {
            steps {
                script {

                    def jobStats = [:]

                    def files = findFiles(glob: 'reports/*.json')

                    echo "Processing ${files.size()} cucumber reports"

                    files.each { file ->

                        def jobName = file.name.split("_")[0]

                        if(!jobStats.containsKey(jobName)) {

                            jobStats[jobName] = [
                                passed:0,
                                failed:0,
                                skipped:0
                            ]
                        }

                        def json = readJSON file: file.path

                        json.each { feature ->

                            feature.elements?.each { scenario ->

                                scenario.steps?.each { step ->

                                    def status = step.result?.status ?: "skipped"

                                    if(status == "passed")
                                        jobStats[jobName].passed++

                                    if(status == "failed")
                                        jobStats[jobName].failed++

                                    if(status == "skipped")
                                        jobStats[jobName].skipped++
                                }
                            }
                        }
                    }

                    def htmlCharts = ""
                    def chartScripts = ""
                    def chartIndex = 0

                    jobStats.each { job, data ->

                        total = data.passed + data.failed + data.skipped

                        passPercent = total > 0 ?
                            (((data.passed * 100.0) / total) * 100 as int) / 100.0
                            : 0

                        def chartId = "chart${chartIndex}"

                        htmlCharts += """
                        <div class="card">
                            <h2>${job}</h2>
                            <h3>Pass %: ${passPercent}%</h3>
                            <canvas id="${chartId}"></canvas>

                            <table>
                                <tr>
                                    <th>Passed</th>
                                    <th>Failed</th>
                                    <th>Skipped</th>
                                </tr>
                                <tr>
                                    <td>${data.passed}</td>
                                    <td>${data.failed}</td>
                                    <td>${data.skipped}</td>
                                </tr>
                            </table>
                        </div>
                        """

                        chartScripts += """
                        new Chart(document.getElementById("${chartId}"), {
                            type: "pie",
                            data: {
                                labels: ["Passed","Failed","Skipped"],
                                datasets: [{
                                    data: [${data.passed},${data.failed},${data.skipped}],
                                    backgroundColor:["#2ecc71","#e74c3c","#95a5a6"]
                                }]
                            }
                        });
                        """

                        chartIndex++
                    }

                    def html = """
<html>

<head>

<title>Jenkins Cucumber Analytics Dashboard</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>

body{
font-family: Arial;
background:#f4f6f8;
padding:30px;
}

h1{
text-align:center;
margin-bottom:40px;
}

.grid{
display:grid;
grid-template-columns:repeat(auto-fit,minmax(400px,1fr));
gap:30px;
}

.card{
background:white;
padding:25px;
border-radius:10px;
box-shadow:0 3px 10px rgba(0,0,0,0.1);
text-align:center;
}

table{
margin:auto;
margin-top:15px;
border-collapse:collapse;
}

th,td{
border:1px solid #ddd;
padding:8px 14px;
}

th{
background:#2c3e50;
color:white;
}

</style>

</head>

<body>

<h1>Jenkins Cucumber Test Dashboard</h1>

<div class="grid">

${htmlCharts}

</div>

<script>

${chartScripts}

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
