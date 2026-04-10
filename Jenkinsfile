pipeline {
    agent any

    stages {

        stage('Define Job APIs') {
            steps {
                script {

                    jobApis = [
                        [
                            name: "API-Tests",
                            url: "http://localhost:8080/job/API-Tests/lastBuild/api/json"
                        ],
                        [
                            name: "UI-Tests",
                            url: "http://localhost:8080/job/UI-Tests/lastBuild/api/json"
                        ],
                        [
                            name: "Regression",
                            url: "http://localhost:8080/job/Regression/lastBuild/api/json"
                        ]
                    ]

                    echo "Total jobs configured: ${jobApis.size()}"
                }
            }
        }

        stage('Fetch Build Data') {
            steps {
                script {

                    def results = []

                    jobApis.each { job ->

                        echo "Fetching ${job.name}"

                        def response = sh(
                            script: """
                            curl -s '${job.url}' || echo '{}'
                            """,
                            returnStdout: true
                        ).trim()

                        def build = [:]

                        try {
                            build = readJSON text: response
                        } catch(Exception e) {
                            build = [:]
                        }

                        def status = build?.result ?: "NO_BUILD"
                        def number = build?.number ?: -1

                        echo "${job.name} -> ${status}"

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

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def success = data.count { it.status == "SUCCESS" }
                    def failure = data.count { it.status == "FAILURE" }
                    def unstable = data.count { it.status == "UNSTABLE" }
                    def nobuild = data.count { it.status == "NO_BUILD" }

                    def html = """
<html>
<head>
<title>Jenkins Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h2>Jenkins Job Execution Summary</h2>

<canvas id="chart"></canvas>

<script>

new Chart(document.getElementById('chart'), {
type: 'pie',
data: {
labels: ['SUCCESS','FAILURE','UNSTABLE','NO_BUILD'],
datasets: [{
data: [${success},${failure},${unstable},${nobuild}],
backgroundColor: ['green','red','orange','gray']
}]
}
});

</script>

<h3>Job Results</h3>

<table border="1">
<tr>
<th>Job</th>
<th>Build</th>
<th>Status</th>
</tr>
"""

                    data.each { row ->
                        html += "<tr><td>${row.job}</td><td>${row.build}</td><td>${row.status}</td></tr>"
                    }

                    html += """
</table>

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
                    reportName: 'Jenkins Execution Dashboard'
                ])
            }
        }

    }
}
