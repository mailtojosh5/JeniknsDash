pipeline {
    agent any

    stages {
        stage('Collect External Reports') {
            steps {
                script {
                    // List of jobs to pull reports from
                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]

                    for (j in jobs) {
                        echo "Attempting to copy artifacts from ${j}"
                        // Pulls any json file containing 'cucumber' from the target job
                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber*.json", 
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )
                    }
                }
            }
        }

        stage('Analyze All Workspace Reports') {
            steps {
                script {
                    def jobResults = []
                    
                    // Recursive search: Looks for any file containing 'cucumber' and '.json' 
                    // inside the current workspace and all subdirectories (like reports/)
                    def files = findFiles(glob: '**/cucumber*.json')

                    echo "Found ${files.length} JSON report(s) across workspace folders"

                    files.each { file ->
                        // Determine a friendly name for the job/folder
                        def pathParts = file.path.split(/[\\\/]/)
                        def displayName = pathParts.length > 1 ? pathParts[pathParts.length - 2] : "Root"

                        // Read as raw text to avoid 'readJSON' static method permission errors
                        def jsonContent = readFile(file.path)

                        // High-performance count of status occurrences
                        def passed = jsonContent.count('"status":"passed"')
                        def failed = jsonContent.count('"status":"failed"')
                        def skipped = jsonContent.count('"status":"skipped"')

                        def total = passed + failed + skipped
                        
                        // Calculate percentage without restricted Math methods
                        def passPercent = total > 0 ? 
                            ((passed * 100.0) / total).toBigDecimal().setScale(2, BigDecimal.ROUND_HALF_UP) 
                            : 0

                        jobResults << [
                            name: displayName,
                            passed: passed,
                            failed: failed,
                            skipped: skipped,
                            passPercent: passPercent
                        ]

                        echo "Processed ${displayName}: P:${passed} F:${failed} S:${skipped} (${passPercent}%)"
                    }

                    // Write internal summary for the dashboard generator
                    writeJSON file: "summary.json", json: jobResults
                }
            }
        }

        stage('Generate Multi-Chart Dashboard') {
            steps {
                script {
                    def data = readJSON file: 'summary.json'
                    def chartsHtml = ""

                    data.each { job ->
                        chartsHtml += """
                        <div style="display: inline-block; width: 30%; min-width: 320px; margin: 15px; border: 1px solid #ddd; padding: 10px; border-radius: 8px;">
                            <h2 style="text-align:center; font-size: 1.2em;">${job.name}</h2>
                            <p style="text-align:center;">Pass Rate: <b style="color: ${job.passPercent > 90 ? 'green' : 'orange'}">${job.passPercent}%</b></p>
                            <canvas id="chart_${job.name.replaceAll(/[^a-zA-Z0-9]/, '_')}"></canvas>
                            <script>
                                (function() {
                                    window.addEventListener('load', function() {
                                        new Chart(document.getElementById("chart_${job.name.replaceAll(/[^a-zA-Z0-9]/, '_')}"), {
                                            type: "pie",
                                            data: {
                                                labels: ["Passed","Failed","Skipped"],
                                                datasets: [{
                                                    data: [${job.passed}, ${job.failed}, ${job.skipped}],
                                                    backgroundColor: ["#2ecc71","#e74c3c","#95a5a6"]
                                                }]
                                            },
                                            options: { responsive: true, maintainAspectRatio: true }
                                        });
                                    });
                                })();
                            </script>
                        </div>
                        """
                    }

                    def finalHtml = """
<html>
<head>
    <title>Global Cucumber Analytics</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f9f9f9; padding: 20px; }
        .container { display: flex; flex-wrap: wrap; justify-content: center; }
        h1 { color: #333; text-align: center; }
    </style>
</head>
<body>
    <h1>🚀 Consolidated Cucumber Dashboard</h1>
    <div class="container">
        ${chartsHtml}
    </div>
</body>
</html>
"""
                    writeFile file: "dashboard.html", text: finalHtml
                }
            }
        }

        stage('Publish') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Consolidated Dashboard'
                ])
            }
        }
    }
}
