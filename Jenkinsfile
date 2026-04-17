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
                            filter: "**/cucumber.html",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )
                    }
                }
            }
        }

        stage('Build Summary') {
            steps {
                script {

                    def jobs = ["API-Tests", "UI-Tests", "Regression-Tests"]

                    def results = []

                    // ✅ Extract ONLY "X days ago" type text
                    def extractRunDateFromHtml = { filePath ->

                        def html = readFile(filePath)

                        // remove tags → clean text
                        def text = html.replaceAll("<[^>]*>", " ")

                        // match relative time
                        def match = (text =~ /(\d+\s+(seconds?|minutes?|hours?|days?|months?|years?)\s+ago)/)

                        if (match.find()) {
                            return match.group(1).trim()
                        }

                        return "N/A"
                    }

                    jobs.each { jobName ->

                        def lastRun = "N/A"

                        def htmlFile = "reports/${jobName}/cucumber.html"

                        if (fileExists(htmlFile)) {
                            lastRun = extractRunDateFromHtml(htmlFile)
                        }

                        results << [
                            jobName: jobName,
                            lastRun: lastRun
                        ]
                    }

                    writeFile file: "summary.json",
                        text: groovy.json.JsonOutput.toJson(results)
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'summary.json'

                    def cards = ""

                    data.each { job ->

                        cards += """
                        <div class="card">
                            <h3>${job.jobName}</h3>
                            <p>🕒 Last Run: <b>${job.lastRun}</b></p>
                        </div>
                        """
                    }

                    def html = """
<html>
<head>
<title>Cucumber Dashboard</title>

<style>
body {
    font-family: Arial;
    background: #f4f6f8;
    padding: 20px;
}

h2 {
    text-align: center;
}

.container {
    display: flex;
    justify-content: center;
    gap: 15px;
    flex-wrap: wrap;
}

.card {
    background: white;
    padding: 15px;
    width: 220px;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
</style>

</head>

<body>

<h2>🚀 Cucumber Dashboard</h2>

<div class="container">
    ${cards}
</div>

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
