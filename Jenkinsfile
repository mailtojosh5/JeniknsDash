pipeline {
    agent any

    environment {
        JENKINS_URL = "http://localhost:8080"
    }

    stages {

        stage('Collect Jobs Safely') {
            steps {
                script {

                    def jobsJson = sh(
                        script: "curl -g -s ${env.JENKINS_URL}/api/json?tree=jobs[name,url] || echo '{\"jobs\":[]}'",
                        returnStdout: true
                    ).trim()

                    def jobs = [:]

                    try {
                        jobs = readJSON text: jobsJson
                    } catch (Exception e) {
                        jobs = [jobs: []]
                    }

                    writeFile file: "jobs.json",
                        text: groovy.json.JsonOutput.toJson(jobs)
                }
            }
        }

        stage('Analyse Build Status (ZERO FAILURE MODE)') {
            steps {
                script {

                    def jobs = readJSON file: 'jobs.json'
                    def results = []

                    jobs.jobs.each { job ->

                        def apiUrl = "${job.url}lastBuild/api/json"

                        def response = "{}", build = [:]

                        try {
                            response = sh(
                                script: "curl -g -s '${apiUrl}' || echo '{}'",
                                returnStdout: true
                            ).trim()

                            build = readJSON text: response

                        } catch (Exception e) {
                            build = [result: "ERROR", number: -1]
                        }

                        // Normalize safely
                        def status = build?.result ?: "NO_BUILD"
                        def number = build?.number ?: -1

                        results << [
                            job: job.name,
                            build: number,
                            status: status
                        ]
                    }

                    writeFile file: "data.json",
                        text: groovy.json.JsonOutput.toJson(results)
                }
            }
        }

        stage('Generate Dashboard HTML') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def success = data.count { it.status == "SUCCESS" }
                    def failed = data.count { it.status == "FAILURE" }
                    def unstable = data.count { it.status == "UNSTABLE" }
                    def noBuild = data.count { it.status == "NO_BUILD" }
                    def error = data.count { it.status == "ERROR" }

                    def html = """
<html>
<head>
    <title>Zero-Failure Jenkins Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>
<h2>🚀 Jenkins Zero-Failure Analytics Dashboard</h2>

<canvas id="chart"></canvas>

<script>
const ctx = document.getElementById('chart');

new Chart(ctx, {
    type: 'pie',
    data: {
        labels: [
            'SUCCESS',
            'FAILURE',
            'UNSTABLE',
            'NO_BUILD',
            'ERROR'
        ],
        datasets: [{
            data: [
                ${success},
                ${failed},
                ${unstable},
                ${noBuild},
                ${error}
            ],
            backgroundColor: [
                'green',
                'red',
                'orange',
                'gray',
                'black'
            ]
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

        stage('Flaky Job Detection (Safe Mode)') {
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
                }
            }
        }

        stage('Publish Dashboard (Safe)') {
            steps {
                script {
                    sh 'ls -l'

                    publishHTML([
                        reportDir: '.',
                        reportFiles: 'dashboard.html',
                        reportName: 'Zero-Failure Jenkins Dashboard'
                    ])
                }
            }
        }
    }
}
