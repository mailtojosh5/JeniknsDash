pipeline {
    agent any

    environment {
        JENKINS_URL = "http://localhost:8080"
    }

    stages {

        stage('Collect Jenkins Jobs (REST API)') {
            steps {
                script {

                    // Get all jobs via REST API (SAFE)
                    def jobsJson = sh(
                        script: """
                        curl -s "${JENKINS_URL}/api/json?tree=jobs[name,url]"
                        """,
                        returnStdout: true
                    ).trim()

                    writeFile file: "jobs.json", text: jobsJson

                    echo "Jobs fetched successfully"
                }
            }
        }

        stage('Analyze Build Status') {
            steps {
                script {

                    def jobs = readJSON file: 'jobs.json'
                    def results = []

                    jobs.jobs.each { job ->

                        try {

                            def buildJson = sh(
                                script: """
                                curl -s "${job.url}/lastBuild/api/json"
                                """,
                                returnStdout: true
                            ).trim()

                            def build = readJSON text: buildJson

                            results << [
                                job    : job.name,
                                build  : build.number,
                                status : build.result ?: "UNKNOWN"
                            ]

                        } catch (Exception e) {
                            results << [
                                job    : job.name,
                                build  : -1,
                                status : "ERROR"
                            ]
                        }
                    }

                    writeFile file: "data.json", text: groovy.json.JsonOutput.toJson(results)
                }
            }
        }

        stage('Generate Dashboard HTML') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def passed = data.count { it.status == "SUCCESS" }
                    def failed = data.count { it.status == "FAILURE" }
                    def unstable = data.count { it.status == "UNSTABLE" || it.status == "ERROR" }

                    def html = """
<html>
<head>
    <title>Jenkins Test Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h2>Jenkins Test Execution Dashboard</h2>

<canvas id="chart" width="400" height="200"></canvas>

<script>

const ctx = document.getElementById('chart');

new Chart(ctx, {
    type: 'pie',
    data: {
        labels: ['SUCCESS', 'FAILURE', 'OTHER'],
        datasets: [{
            data: [${passed}, ${failed}, ${unstable}],
            backgroundColor: ['green', 'red', 'orange']
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

        stage('Flaky Job Detection') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def flaky = data
                        .groupBy { it.job }
                        .findAll { job, builds ->
                            def statuses = builds.collect { it.status }.toSet()
                            return statuses.size() > 1
                        }
                        .keySet()

                    writeFile file: "flaky.txt", text: flaky.join("\n")

                    echo "Flaky jobs detected: ${flaky}"
                }
            }
        }

        stage('Publish Dashboard') {
            steps {
                script {
                    sh 'ls -l'

                    publishHTML([
                        reportDir: '.',
                        reportFiles: 'dashboard.html',
                        reportName: 'Jenkins Test Dashboard'
                    ])
                }
            }
        }
    }
}
