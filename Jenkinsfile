def data = readJSON file: 'summary.json'

def cards = ""
def scripts = ""

data.eachWithIndex { job, i ->

    def id = "chart_" + i

    cards += """
    <div class="card">
        <div class="card-header">
            <h2>${job.name}</h2>
            <span class="badge">Pass %: ${job.passPercent}</span>
        </div>

        <div class="chart-box">
            <canvas id="${id}"></canvas>
        </div>
    </div>
    """

    scripts += """
    new Chart(document.getElementById("${id}"), {
        type: "pie",
        data: {
            labels: ["Passed","Failed","Skipped"],
            datasets: [{
                data: [${job.passed}, ${job.failed}, ${job.skipped}],
                backgroundColor: ["#2ecc71","#e74c3c","#95a5a6"],
                borderWidth: 1
            }]
        },
        options: {
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
    """
}

def html = """
<html>
<head>
<title>Cucumber Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>

body {
    font-family: Arial, sans-serif;
    background: #f4f6f8;
    margin: 0;
    padding: 0;
}

.header {
    background: #1f2937;
    color: white;
    padding: 20px;
    text-align: center;
}

.container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 20px;
    padding: 20px;
}

.card {
    background: white;
    border-radius: 12px;
    padding: 15px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.card-header h2 {
    font-size: 18px;
    margin: 0;
}

.badge {
    background: #2563eb;
    color: white;
    padding: 5px 10px;
    border-radius: 20px;
    font-size: 12px;
}

.chart-box {
    width: 100%;
    height: 260px;
}

</style>

</head>

<body>

<div class="header">
    <h1>🚀 Cucumber Execution Dashboard</h1>
    <p>Automated Test Analytics (Jenkins)</p>
</div>

<div class="container">
    ${cards}
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    ${scripts}
});
</script>

</body>
</html>
"""

writeFile file: "dashboard.html", text: html
