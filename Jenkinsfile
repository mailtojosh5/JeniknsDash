pipeline {
    agent any

    stages {
        stage('Scrape Cucumber HTML') {
            steps {
                script {
                    // The URL to your existing HTML report
                    def reportUrl = "https://your-server/path/to/cucumber-report.html"
                    
                    // 1. Fetch the HTML content
                    // Using -k if your certs are self-signed
                    def htmlText = sh(
                        script: "curl -s -k -f -L '${reportUrl}' || echo ''",
                        returnStdout: true
                    ).trim()

                    if (!htmlText) {
                        error "Could not fetch the HTML report from ${reportUrl}"
                    }

                    // 2. Extract Numbers using Regex
                    // Note: Cucumber HTML reports usually store totals in tags like:
                    // <td class="passed">15</td> OR <span>15 passed</span>
                    // Adjust the regex below based on your specific report's HTML source
                    
                    def passed = extractCount(htmlText, /class="passed">(\d+)</) ?: extractCount(htmlText, /(\d+) passed/) ?: 0
                    def failed = extractCount(htmlText, /class="failed">(\d+)</) ?: extractCount(htmlText, /(\d+) failed/) ?: 0
                    def skipped = extractCount(htmlText, /class="skipped">(\d+)</) ?: extractCount(htmlText, /(\d+) skipped/) ?: 0

                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "Scraped Totals -> Passed: ${passed}, Failed: ${failed}, Skipped: ${skipped}"

                    // 3. Generate the Dashboard
                    def dashboardHtml = """
                    <html>
                    <head>
                        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                        <style>body { font-family: Arial; text-align: center; padding: 20px; }</style>
                    </head>
                    <body>
                        <h2>Automated Test Summary</h2>
                        <div style="width:300px; margin:auto;"><canvas id="myChart"></canvas></div>
                        <p><strong>Success Rate: ${passPercent}%</strong></p>
                        <p><a href="${reportUrl}" target="_blank">View Original Full Report</a></p>
                        <script>
                            new Chart(document.getElementById('myChart'), {
                                type: 'pie',
                                data: {
                                    labels: ['Passed', 'Failed', 'Skipped'],
                                    datasets: [{
                                        data: [${passed}, ${failed}, ${skipped}],
                                        backgroundColor: ['#2ecc71', '#e74c3c', '#95a5a6']
                                    }]
                                }
                            });
                        </script>
                    </body>
                    </html>
                    """
                    writeFile file: 'dashboard.html', text: dashboardHtml
                }
            }
        }

        stage('Publish') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Executive Summary'
                ])
            }
        }
    }
}

// Helper function to find numbers in the HTML string
def extractCount(String text, String regex) {
    def matcher = (text =~ regex)
    if (matcher.find()) {
        return matcher.group(1).toInteger()
    }
    return null
}
