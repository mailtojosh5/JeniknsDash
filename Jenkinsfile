def html = """
<html>
<head>
<title>Cucumber Analytics Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<style>
    body {
        background: #0f172a;
        color: #e2e8f0;
        font-family: Arial, sans-serif;
        text-align: center;
        padding: 40px;
        margin: 0;
    }
    h2 {
        color: #67e8f9;
        font-size: 32px;
    }
    h3 {
        color: #94a3b8;
        font-size: 24px;
    }
    .chart-container {
        width: 500px;
        margin: 30px auto;
        padding: 20px;
        background: #1e2937;
        border-radius: 16px;
    }
    table {
        margin: 30px auto;
        border-collapse: collapse;
        background: #1e2937;
        font-size: 18px;
    }
    th, td {
        padding: 15px 30px;
        border: 1px solid #475569;
    }
    th {
        background: #334155;
        color: #67e8f9;
    }
</style>
</head>
<body>
<h2>Cucumber Test Dashboard</h2>
<h3>Pass Percentage: ${data.passPercent}%</h3>

<div class="chart-container">
    <canvas id="chart" width="450" height="450"></canvas>
</div>

<table border="1" style="margin-top:20px">
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

<script>
    // Very safe number handling
    var passed = ${data.passed};
    var failed = ${data.failed};
    var skipped = ${data.skipped};

    new Chart(document.getElementById("chart"), {
        type: "pie",
        data: {
            labels: ["Passed","Failed","Skipped"],
            datasets: [{
                data: [passed, failed, skipped],
                backgroundColor: ["#4ade80", "#f87171", "#94a3b8"],
                borderColor: "#1e2937",
                borderWidth: 8
            }]
        },
        options: {
            animation: {
                duration: 2000,
                easing: "easeOutQuart"
            }
        }
    });
</script>
</body>
</html>
"""
