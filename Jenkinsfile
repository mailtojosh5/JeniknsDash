pipeline {
    agent any

    stages {

        stage('Scan Workspace for Cucumber JSON') {
            steps {
                script {

                    workspaceRoot = "/opt/jenkins/workspace"

                    def files = sh(
                        script: """
                        find ${workspaceRoot} -type f -iname "*cucumber*.json" 2>/dev/null
                        """,
                        returnStdout: true
                    ).trim().split("\\n")

                    echo "Found ${files.size()} cucumber files"

                    // store directly (NO COPY)
                    writeFile file: "fileList.txt", text: files.join("\n")
                }
            }
        }

        stage('Parse All Reports Directly') {
            steps {
                script {

                    def files = readFile("fileList.txt").split("\n")

                    def jobStats = [:]

                    files.each { file ->

                        if(file?.trim()) {

                            echo "Reading: ${file}"

                            def jobName = file.tokenize("/")[-4] ?: "Unknown"

                            def json = []

                            try {
                                json = readJSON text: readFile(file)
                            } catch(Exception e) {
                                echo "Skipping invalid JSON: ${file}"
                                return
                            }

                            if(!jobStats.containsKey(jobName)) {
                                jobStats[jobName] = [
                                    passed:0,
                                    failed:0,
                                    skipped:0
                                ]
                            }

                            json.each { feature ->
                                feature.elements?.each { scenario ->
                                    scenario.steps?.each { step ->

                                        def status = step.result?.status ?: "skipped"

                                        if(status == "passed") jobStats[jobName].passed++
                                        else if(status == "failed") jobStats[jobName].failed++
                                        else jobStats[jobName].skipped++
                                    }
                                }
                            }
                        }
                    }

                    writeFile file: "summary.json",
                        text: groovy.json.JsonBuilder(jobStats).toPrettyString()
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def cards = ""
                    def scripts = ""
                    def i = 0

                    data.each { job, stats ->

                        def total = stats.passed + stats.failed + stats.skipped

                        def passPercent = total > 0 ?
                            (((stats.passed * 100.0) / total) * 100 as int) / 100.0
                            : 0

                        def id = "chart_${i}"
                        def displayName = job.tokenize('/').last()

                        cards += """
                        <div class="card">
                            <h2>${displayName}</h2>
                            <div class="badge">Pass %: ${passPercent}</div>

                            <canvas id="${id}"></canvas>

                            <div class="stats">
                                Passed: ${stats.passed} |
                                Failed: ${stats.failed} |
                                Skipped: ${stats.skipped}
                            </div>
                        </div>
                        """

                        scripts += """
                        new Chart(document.getElementById("${id}"), {
                            type: "pie",
                            data: {
                                labels: ["Passed","Failed","Skipped"],
                                datasets: [{
                                    data: [${stats.passed},${stats.failed},${stats.skipped}],
                                    backgroundColor: ["#2ecc71","#e74c3c","#95a5a6"]
                                }]
                            }
                        });
                        """

                        i++
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

h1 {
    text-align: center;
}

.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 20px;
}

.card {
    background: white;
    padding: 20px;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
    text-align: center;
}

.badge {
    display: inline-block;
    background: #2563eb;
    color: white;
    padding: 5px 10px;
    border-radius: 20px;
    margin-bottom: 10px;
}

.stats {
    margin-top: 10px;
    font-size: 13px;
    color: #555;
}
</style>

</head>

<body>

<h1>🚀 Zero-Copy Cucumber Dashboard</h1>

<div class="grid">
${cards}
</div>

<script>
${scripts}
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
