def jobSummaries = []

for (j in jobs) {

    echo "Processing job: ${j}"

    def lastRun = "N/A"

    try {
        def response = httpRequest(
            url: "${env.JENKINS_URL}/job/${j}/lastSuccessfulBuild/api/json",
            validResponseCodes: '200'
        )

        def json = readJSON text: response.content

        if (json.timestamp) {
            lastRun = new Date(json.timestamp).format("yyyy-MM-dd HH:mm:ss")
        }

    } catch (err) {
        echo "Could not fetch build info for ${j}"
    }

    copyArtifacts(
        projectName: j,
        selector: lastSuccessful(),
        filter: "**/cucumber.json",
        target: "reports/${j}",
        flatten: true,
        optional: true
    )

    jobSummaries << [
        jobName: j,
        lastRun: lastRun,
        reportPath: "reports/${j}/cucumber.json"
    ]
}
