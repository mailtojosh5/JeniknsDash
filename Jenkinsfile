stage('Analyse Build Status') {
    steps {
        script {

            def jobs = readJSON file: 'jobs.json'
            def results = []

            jobs.jobs.each { job ->

                try {

                    // safer URL build
                    def apiUrl = "${job.url}lastBuild/api/json"

                    def buildJson = sh(
                        script: "curl -g -s '${apiUrl}' || echo '{}'",
                        returnStdout: true
                    ).trim()

                    def build = readJSON text: buildJson

                    if (!build || !build.result) {
                        results << [
                            job: job.name,
                            build: -1,
                            status: "NO_BUILD"
                        ]
                    } else {
                        results << [
                            job: job.name,
                            build: build.number,
                            status: build.result
                        ]
                    }

                } catch (Exception e) {

                    results << [
                        job: job.name,
                        build: -1,
                        status: "ERROR"
                    ]
                }
            }

            writeFile file: 'data.json',
                text: groovy.json.JsonOutput.toJson(results)
        }
    }
}
