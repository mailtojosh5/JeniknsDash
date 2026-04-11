def html = """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Cucumber Analytics Dashboard</title>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<style>
body {
    margin:0;
    font-family: 'Segoe UI', sans-serif;
    background: linear-gradient(135deg, #1e3c72, #2a5298);
    color: white;
}

.container {
    padding: 30px;
    text-align: center;
}

h1 {
    margin-bottom: 10px;
}

.cards {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin: 30px 0;
    flex-wrap: wrap;
}

.card {
    background: rgba(255,255,255,0.1);
    backdrop-filter: blur(10px);
    padding: 20px;
    border-radius: 15px;
    width: 180px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.3);
    transition: transform 0.3s;
}

.card:hover {
    transform: translateY(-10px) scale(1.05);
}

.card h2 {
    margin: 10px 0;
    font-size: 32px;
}

.pass { color: #00ff9f; }
.fail { color: #ff4d4d; }
.skip { color: #cccccc; }

.progress-container {
    width: 60%;
    margin: 20px auto;
    background: rgba(255,255,255,0.2);
    border-radius: 20px;
    overflow: hidden;
}

.progress-bar {
    height: 20px;
    width: 0;
    background: linear-gradient(90deg, #00ff9f, #00c3ff);
    animation: loadBar 2s forwards;
}

@keyframes loadBar {
    from { width: 0; }
    to { width: ${data.passPercent}%; }
}

.chart-container {
    width: 400px;
    margin: 40px auto;
}

table {
    margin: 30px auto;
    border-collapse: collapse;
    width: 60%;
    background: rgba(255,255,255,0.1);
    backdrop-filter: blur(10px);
    border-radius: 10px;
    overflow: hidden;
}

th, td {
    padding: 12px;
    text-align: center;
}

th {
    background: rgba(0,0,0,0.3);
}

tr:hover {
    background: rgba(255,255,255,0.1);
}

.fade-in {
    animation: fadeIn 1.5s ease-in;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px);}
    to { opacity: 1; transform: translateY(0);}
}
</style>

</head>

<body>

<div class="container fade-in">

<h1>🚀 Cucumber Test Dashboard</h1>
<h3>Pass Percentage: ${data.passPercent}%</h3>

<div class="progress-container">
    <div class="progress-bar"></div>
</div>

<div class="cards">

    <div class="card">
        <h4>Passed</h4>
        <h2 class="pass">${data.passed}</h2>
    </div>

    <div class="card">
        <h4>Failed</h4>
        <h2 class="fail">${data.failed}</h2>
    </div>

    <div class="card">
        <h4>Skipped</h4>
        <h2 class="skip">${data.skipped}</h2>
    </div>

</div>

<div class="chart-container">
    <canvas id="chart"></canvas>
</div>

<table>
<tr>
<th>Passed</th>
<th>Failed</th>
<th>Skipped</th>
<th>Pass %</th>
</tr>

<tr>
<td>${data.passed}</td>
<td>${data.failed}</td>
<td>${data.skipped}</td>
<td>${data.passPercent}%</td>
</tr>
</table>

</div>

<script>

const ctx = document.getElementById("chart");

new Chart(ctx, {
    type: "doughnut",
    data: {
        labels: ["Passed","Failed","Skipped"],
        datasets: [{
            data: [${data.passed},${data.failed},${data.skipped}],
            backgroundColor:["#00ff9f","#ff4d4d","#cccccc"],
            borderWidth: 1
        }]
    },
    options: {
        animation: {
            animateScale: true,
            duration: 2000
        },
        plugins: {
            legend: {
                labels: {
                    color: "white"
                }
            }
        }
    }
});

</script>

</body>
</html>
"""
