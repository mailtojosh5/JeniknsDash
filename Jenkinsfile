pipeline {
    agent any

    stages {

        stage('Collect Jenkins Data') {
            steps {
                script {

                    def jenkins = Jenkins.getInstanceOrNull()
                    def jobs = Jenkins.instance.getAllItems(Job)

                    def results = []

                    jobs.each { job ->

                        try {
                            def lastBuild = job.getLastBuild()

                            if (lastBuild != null) {

                                def status = lastBuild.getResult()?.toString() ?: "UNKNOWN"

                                results << [
                                    job   : job.getFullName(),
                                    build : lastBuild.getNumber(),
                                    status: status
                                ]
                            }

                        } catch (Exception e) {
                            // ignore inaccessible jobs
                        }
                    }

                    writeFile file: 'data.json', text: groovy.json.JsonOutput.toJson(results)
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def passed = data.count { it.status == "SUCCESS" }
                    def failed = data.count { it.status == "FAILURE" }
                    def unstable = data.count { it.status == "UNSTABLE" }

                    def html = """
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>
    <h2>Jenkins Test Dashboard</h2>

    <canvas id="chart"></canvas>

    <script>
        const ctx = document.getElementById('chart');

        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ['SUCCESS', 'FAILURE', 'UNSTABLE'],
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

                    writeFile file: 'dashboard.html', text: html
                }
            }
        }

        stage('Flaky Job Detection') {
            steps {
                script {

                    def data = readJSON file: 'data.json'

                    def flaky = data
                        .groupBy { it.job }
                        .findAll { k, v ->
                            def statuses = v.collect { it.status }.toSet()
                            return statuses.size() > 1
                        }
                        .keySet()

                    writeFile file: 'flaky.txt', text: flaky.join("\n")

                }
            }
        }

        stage('Publish Dashboard') {
            steps {
                publishHTML([
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Jenkins Test Dashboard'
                ])
            }
        }
    }
}
