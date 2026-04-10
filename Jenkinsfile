pipeline {
    agent any

    stages {
        stage('Scrape HTML Report') {
            steps {
                script {
                    // 1. Define the URL
                    def reportUrl = "https://your-server/path/to/cucumber-report.html"
                    
                    def htmlText = ""
                    try {
                        // Using Groovy's native way to read text from a URL
                        // This uses the Jenkins JVM's networking (ignores some CLI auth issues)
                        htmlText = new URL(reportUrl).getText(requestProperties: ["Accept": "text/html"])
                    } catch (Exception e) {
                        error "Failed to fetch report: ${e.message}"
                    }

                    // 2. Extract Numbers (Regex Scraper)
                    // We look for patterns like "15 passed" or ">15<" inside passed-colored cells
                    def passed = extractCount(htmlText, /(\d+)\s+passed/) ?: extractCount(htmlText, /class="passed">(\d+)</) ?: 0
                    def failed = extractCount(htmlText, /(\d+)\s+failed/) ?: extractCount(htmlText, /class="failed">(\d+)</) ?: 0
                    def skipped = extractCount(htmlText, /(\d+)\s+skipped/) ?: extractCount(htmlText, /class="skipped">(\d+)</) ?: 0

                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "Results Extracted: P:${passed} F:${failed} S:${skipped}"

                    // 3. Create the Dashboard HTML
                    def dashboard = """
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { font-family: sans-serif; text-align: center; padding: 20px; }
        .chart-container { width: 300px; margin: auto; }
    </style>
</head>
<body>
    <h2>Test Execution Summary</h2>
    <div class="chart-container"><canvas id="myChart"></canvas></div>
    <h3>Pass Rate: ${passPercent}%</h3>
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
                    writeFile file: 'dashboard.html', text: dashboard
                }
            }
        }

        stage('Publish Dashboard') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Executive Dashboard'
                ])
            }
        }
    }
}

// Helper function to find the numbers in the HTML
def extractCount(String text, String regex) {
    def matcher = (text =~ regex)
    if (matcher.find()) {
        return matcher.group(1).toInteger()
    }
    return null
}
