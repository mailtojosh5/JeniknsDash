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
        text-align: center;
    }
    
    h2 {
        font-size: 2.8rem;
        margin-bottom: 12px;
        background: linear-gradient(90deg, #67e8f9, #c084fc);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }
    
    h3 {
        font-size: 1.7rem;
        color: #94a3b8;
        margin-bottom: 40px;
    }
    
    .chart-wrapper {
        max-width: 650px;
        margin: 0 auto 50px auto;
        padding: 30px;
        background: #1e2937;
        border-radius: 24px;
        box-shadow: 0 20px 50px rgba(0, 0, 0, 0.45);
    }
    
    table {
        margin: 0 auto;
        border-collapse: collapse;
        background: #1e2937;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4);
        font-size: 1.25rem;
    }
    
    th, td {
        padding: 20px 35px;
        border: 1px solid #334155;
    }
    
    th {
        background: #334155;
        color: #67e8f9;
        font-weight: 600;
    }
</style>
</head>
<body>
<h2>Cucumber Test Dashboard</h2>
<h3>Pass Percentage: ${data.passPercent}%</h3>

<div class="chart-wrapper">
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
// Safe parsing to fix NaN issue
const passed = parseFloat("${data.passed}") || 0;
const failed = parseFloat("${data.failed}") || 0;
const skipped = parseFloat("${data.skipped}") || 0;

new Chart(document.getElementById("chart"), {
    type: "pie",
    data: {
        labels: ["Passed", "Failed", "Skipped"],
        datasets: [{
            data: [passed, failed, skipped],
            backgroundColor: ["#22c55e", "#ef4444", "#64748b"],
            borderColor: "#1e2937",
            borderWidth: 10,
            hoverOffset: 40
        }]
    },
    options: {
        responsive: true,
        maintainAspectRatio: true,
        animation: {
            duration: 2500,
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
                        size: 17,
                        weight: '600'
                    },
                    padding: 30,
                    boxWidth: 18
                }
            },
            tooltip: {
                backgroundColor: '#1e2937',
                titleColor: '#f8fafc',
                bodyColor: '#e2e8f0',
                padding: 18,
                cornerRadius: 12
            }
        }
    }
});
</script>
</body>
</html>
"""
