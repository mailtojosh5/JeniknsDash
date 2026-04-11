def data = readJSON file: 'summary.json'

def chartScripts = ""
def canvases = ""

data.eachWithIndex { job, i ->

    def safeId = "chart_" + i   // IMPORTANT: avoid special chars

    canvases += """
        <div style="margin-bottom:40px;">
            <h2>${job.name}</h2>
            <h3>Pass %: ${job.passPercent}</h3>

            <canvas id="${safeId}" width="400" height="300"></canvas>
        </div>
    """

    chartScripts += """
        new Chart(document.getElementById("${safeId}"), {
            type: "pie",
            data: {
                labels: ["Passed","Failed","Skipped"],
                datasets: [{
                    data: [${job.passed}, ${job.failed}, ${job.skipped}],
                    backgroundColor: ["green","red","gray"]
                }]
            }
        });
    """
}

def html = """
<html>
<head>
<title>Cucumber Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<body>

<h1>Cucumber Multi-Job Dashboard</h1>

${canvases}

<script>
document.addEventListener("DOMContentLoaded", function() {
    ${chartScripts}
});
</script>

</body>
</html>
"""

writeFile file: "dashboard.html", text: html
