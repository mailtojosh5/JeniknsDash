 stage('Discover Jenkins Jobs') {
            steps {
                script {

                    // Define Jenkins folder containing test jobs
                    testFolder = "QA-Automation"

                    // List of jobs to scan automatically
                    jobs = []

                    dir("/var/lib/jenkins/jobs/${testFolder}/jobs") {

                        def files = sh(
                            script: "ls",
                            returnStdout: true
                        ).trim().split("\n")

                        files.each { jobName ->
                            jobs.add("${testFolder}/${jobName}")
                        }
                    }

                    echo "Discovered jobs:"
                    jobs.each { echo it }

                }
            }
        }

        stage('Copy Cucumber Reports') {
            steps {
                script {

                    jobs.each { j ->

                        echo "Copying cucumber.json from ${j}"

                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber.json",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )
                    }

                }
            }
        }
