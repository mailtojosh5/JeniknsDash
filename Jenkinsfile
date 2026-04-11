pipeline {
    agent { label 'built-in' } // IMPORTANT: Must run on the controller to see /opt/

    stages {
        stage('Scan ESMORG Workspace') {
            steps {
                script {
                    // 1. Define the base path to scan
                    def scanPath = "/opt/app/jenkins/workspace/esmorg"
                    
                    echo "Scanning path: ${scanPath}"

                    // 2. Use Shell to find files and count statuses
                    // We use -maxdepth to prevent scanning too deep if the folder is huge
                    def rawData = sh(script: """
                        if [ -d "${scanPath}" ]; then
                            find ${scanPath} -maxdepth 4 -name "cucumber.json" | while read file; do
                                # Get the name of the folder containing the json as the 'Job Name'
                                jobName=\$(basename \$(dirname "\$file"))
                                passed=\$(grep -o '"status":"passed"' "\$file" | wc -l)
                                failed=\$(grep -o '"status":"failed"' "\$file" | wc -l)
                                skipped=\$(grep -o '"status":"skipped"' "\$file" | wc -l)
                                echo "\${jobName}|\${passed}|\${failed}|\${skipped}"
                            done
                        else
                            echo "ERROR: Path ${scanPath} not found." >&2
                            exit 1
                        fi
                    """, returnStdout: true).trim()

                    if (!rawData) {
                        echo "No cucumber.json files found in ${scanPath}"
                        return
                    }

                    // 3. Convert shell output to list
                    def jobResults = []
                    rawData.split("\n").each { line ->
                        def parts = line.split("\\|")
                        if (parts.length == 4) {
                            def p = parts[1].toInteger()
                            def f = parts[2].toInteger()
                            def s = parts[3].toInteger()
                            def total = p + f + s
                            
                            jobResults << [
                                name: parts[0],
                                passed: p,
                                failed: f,
                                skipped: s,
                                passPercent: total > 0 ? ((p * 100) / total).toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP) : 0
                            ]
                        }
                    }

                    // 4. Save results for dashboard
                    writeJSON file: "summary.json", json: jobResults
                }
            }
        }

        stage('Generate & Publish Dashboard') {
            steps {
                script {
                    def data = readJSON file: 'summary.json'
                    def chartsHtml = ""

                    data.each { job ->
                        chartsHtml += """
                        <div style="display: inline-block; width: 300px; margin: 15px; border: 1px solid #ccc; padding: 10px; border-radius: 5px; background: #fff;">
                            <h2 style="text-align:center; font-size: 1.1em;">${job.name}</h2>
                            <p style="text-align:center;">Pass Rate: <b>${job.passPercent}%</b></p>
                            <canvas id="chart_${job.name.replaceAll(/[^a-zA-Z0-9]/, '_')}"></canvas>
                            <script>
                                window.addEventListener('load', function() {
                                    new Chart(document.getElementById("chart_${job.name.replaceAll(/[^a-zA-Z0-9]/, '_')}"), {
                                        type: "pie",
                                        data: {
                                            labels: ["Passed","Failed","Skipped"],
                                            datasets: [{
                                                data: [${job.passed}, ${job.failed}, ${job.skipped}],
                                                backgroundColor: ["#2ecc71","#e74c3c","#95a5a6"]
                                            }]
                                        }
                                    });
                                });
                            </script>
                        </div>
                        """
                    }

                    def finalHtml = """
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>body { font-family: sans-serif; background: #f4f4f4; text-align: center; }</style>
</head>
<body>
    <h1>🚀 ESMORG Global Cucumber Analytics</h1>
    <div style="display: flex; flex-wrap: wrap; justify-content: center;">
        ${chartsHtml}
    </div>
</body>
</html>
"""
                    writeFile file: "dashboard.html", text: finalHtml
                }
                
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'ESMORG Dashboard'
                ])
            }
        }
    }
}
