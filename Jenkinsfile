def html = """
<html>
<head>
<title>Cucumber Analytics Dashboard</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
<style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
    
    body {
        font-family: 'Inter', system-ui, sans-serif;
        background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
        color: #e2e8f0;
        margin: 0;
        padding: 40px 20px;
        min-height: 100vh;
        text-align: center;
    }
    
    h2 {
        font-size: 2.6rem;
        margin-bottom: 10px;
        background: linear-gradient(90deg, #67e8f9, #c084fc);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }
    
    h3 {
        font-size: 1.6rem;
        color: #94a3b8;
        margin-bottom: 40px;
    }
    
    .chart-container {
        max-width: 620px;
        margin: 0 auto 50px auto;
        background: #1e2937;
        padding: 40px;
        border-radius: 24px;
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
    }
    
    table {
        margin: 30px auto;
        border-collapse: collapse;
        background: #1e2937;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        font-size: 1.2rem;
    }
    
    th, td {
        padding: 18px 30px;
        border: 1px solid #334155;
    }
    
    th {
        background: #334155;
        color: #67e8f9;
        font-weight: 600;
    }
    
    td {
        font-weight: 500;
    }
</style>
</head>
<body>
<h2>Cucumber Test Dashboard</h2>
<h3>Pass Percentage: ${data.passPercent}%</h3>

<div class="chart-container">
    <canvas id="chart"></canvas>
</div>

<table border="1">
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
// Safe number handling to prevent NaN
const passed = parseFloat("${data.passed}") || 0;
const failed = parseFloat("${data.failed}") || 0;
const skipped = parseFloat("${data.skipped}") || 0;
const passPercent = parseFloat("${data.passPercent}") || 0;

new Chart(document.getElementById("chart"), {
    type: "pie",
    data: {
        labels: ["Passed", "Failed", "Skipped"],
        datasets: [{
            data: [passed, failed, skipped],
            backgroundColor: ["#22c55e", "#ef4444", "#64748b"],
            borderColor: "#1e2937",
            borderWidth: 8,
            hoverOffset: 35
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,
        animation: {
            duration: 2200,
            easing: "easeOutBounce",
            animateRotate: true,
            animateScale: true
        },
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    color: '#e2e8f0',
                    font: {
                        size: 16,
                        weight: '600'
                    },
                    padding: 25,
                    usePointStyle: true,
                    boxWidth: 14
                }
            },
            tooltip: {
                backgroundColor: '#1e2937',
                titleColor: '#f1f5f9',
                bodyColor: '#e2e8f0',
                padding: 16,
                cornerRadius: 12,
                displayColors: true,
                boxPadding: 8
            }
        }
    }
});
</script>
</body>
</html>
"""
