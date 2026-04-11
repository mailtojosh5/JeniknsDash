def jobResults = [:]

files.each { file ->

    if(!file?.trim()) return

    def jobName = file.tokenize("/")[-3]

    def json = readJSON text: readFile(file)

    if(!jobResults.containsKey(jobName)) {
        jobResults[jobName] = [
            passed: 0,
            failed: 0,
            skipped: 0,
            total: 0,
            lastRun: ""
        ]
    }

    json.each { feature ->
        feature.elements?.each { scenario ->

            scenario.steps?.each { step ->

                def status = step.result?.status ?: "skipped"

                jobResults[jobName].total++

                if(status == "passed")
                    jobResults[jobName].passed++
                else if(status == "failed")
                    jobResults[jobName].failed++
                else
                    jobResults[jobName].skipped++
            }
        }
    }

    // capture last modified time of file as "last run"
    def lastModified = new Date(new File(file).lastModified()).format("yyyy-MM-dd HH:mm:ss")

    jobResults[jobName].lastRun = lastModified
}
