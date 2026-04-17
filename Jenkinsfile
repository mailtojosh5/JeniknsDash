def jobSummaries = []

for (j in jobs) {

    echo "Processing job: ${j}"

    def lastRun = "N/A"

    try {

        def jobPath = j.replaceAll('/', '/job/')
        def url = "${env.BUILD_URL}../job/${jobPath}/lastSuccessfulBuild/api/json"

        echo "Calling: ${url}"

        def response = httpRequest(
            url: url,
            validResponseCodes: '200'
        )

        def json = readJSON text: response.content

        if (json.timestamp) {
            lastRun = new Date(json.timestamp).format("yyyy-MM-dd HH:mm:ss")
        }

    } catch (err) {
        echo "❌ Failed to fetch build info for ${j}: ${err}"
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
