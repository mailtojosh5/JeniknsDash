def html = """
<!DOCTYPE html>
<html>
<head>
<title>Cucumber Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    margin:0;
    font-family: 'Segoe UI';
    background: linear-gradient(135deg,#141e30,#243b55);
    color:white;
}

.container {
    padding:30px;
    text-align:center;
}

h1 {
    font-size:32px;
}

.grid {
    display:grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap:25px;
    margin-top:30px;
}

.card {
    background: rgba(255,255,255,0.08);
    border-radius:15px;
    padding:15px;
    box-shadow:0 8px 20px rgba(0,0,0,0.3);
    transition:0.3s;
}

.card:hover {
    transform: scale(1.03);
}

canvas {
    max-height:250px;
}

.fade {
    animation: fadeIn 1.5s ease;
}

@keyframes fadeIn {
    from {opacity:0; transform:translateY(20px);}
    to {opacity:1;}
}
</style>
</head>

<body>

<div class="container fade">

<h1>🚀 Cucumber Multi-Job Dashboard</h1>
<h3>Total Summary</h3>

<div class="grid">

"""

// ----------------------------------------------------
// 🔥 DYNAMIC JOB LIST (same as your pipeline jobs list)
// ----------------------------------------------------

def jobs = ["API-Tests","UI-Tests","Regression-Tests"]

jobs.each { job ->

    html += """
    <div class="card">
        <h3>${job}</h3>
        <canvas id="chart_${job}"></canvas>
    </div>
    """
}

html += """
</div>
</div>

<script>
"""

// ----------------------------------------------------
// 🔥 EACH JOB GETS ITS OWN CHART (NO MERGING)
// ----------------------------------------------------

jobs.each { job ->

    html += """

new Chart(document.getElementById("chart_${job}"), {
    type: "doughnut",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [
                ${data.passed},
                ${data.failed},
                ${data.skipped}
            ],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"]
        }]
    },
    options: {
        animation: { duration: 1500 }
    }
});

"""
}

html += """
</script>

</body>
</html>
"""

writeFile file: "dashboard.html", text: html
