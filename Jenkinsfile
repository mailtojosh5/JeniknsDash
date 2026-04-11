stage('Generate Dashboard (Clean Multi-Job UI)') {
    steps {
        script {

            def data = readJSON file: 'summary.json'

            def cards = ""
            def scripts = ""
            def i = 0

            data.each { job, stats ->

                def total = stats.passed + stats.failed + stats.skipped
                def passPercent = total > 0 ? 
                    Math.round((stats.passed * 100.0) / total * 100) / 100.0 : 0

                def chartId = "chart_${i}"
                def displayName = job.tokenize('/').last()

                cards += """
                <div class="card">
                    <div class="card-header">
                        <h2>${displayName}</h2>
                    </div>
                    
                    <div class="progress-circle" data-percent="${passPercent}">
                        <svg width="140" height="140">
                            <circle class="bg" cx="70" cy="70" r="60"></circle>
                            <circle class="progress" cx="70" cy="70" r="60"></circle>
                        </svg>
                        <div class="percent">
                            <span class="value">${passPercent}</span><span class="symbol">%</span>
                        </div>
                    </div>

                    <canvas id="${chartId}" class="pie-chart"></canvas>

                    <div class="stats">
                        <div class="stat-item passed">
                            <span>Passed</span>
                            <strong>${stats.passed}</strong>
                        </div>
                        <div class="stat-item failed">
                            <span>Failed</span>
                            <strong>${stats.failed}</strong>
                        </div>
                        <div class="stat-item skipped">
                            <span>Skipped</span>
                            <strong>${stats.skipped}</strong>
                        </div>
                    </div>
                </div>
                """

                scripts += """
                // Pie Chart
                new Chart(document.getElementById("${chartId}"), {
                    type: "doughnut",
                    data: {
                        labels: ["Passed", "Failed", "Skipped"],
                        datasets: [{
                            data: [${stats.passed}, ${stats.failed}, ${stats.skipped}],
                            backgroundColor: ["#22c55e", "#ef4444", "#64748b"],
                            borderWidth: 3,
                            borderColor: "#1e2937"
                        }]
                    },
                    options: {
                        animation: {
                            animateRotate: true,
                            animateScale: true,
                            duration: 1800,
                            easing: 'easeOutQuart'
                        },
                        cutout: "65%",
                        plugins: {
                            legend: {
                                position: 'bottom',
                                labels: {
                                    padding: 20,
                                    boxWidth: 12,
                                    font: { size: 13 }
                                }
                            }
                        }
                    }
                });

                // Animate Progress Circle
                setTimeout(() => {
                    const circle = document.querySelectorAll('.progress')[${i}];
                    const radius = circle.r.baseVal.value;
                    const circumference = radius * 2 * Math.PI;
                    circle.style.strokeDasharray = circumference;
                    circle.style.strokeDashoffset = circumference;
                    
                    const offset = circumference - (circumference * ${passPercent} / 100);
                    circle.style.strokeDashoffset = offset;
                }, 300);
                """

                i++
            }

            def html = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cucumber Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <style>
        :root {
            --bg: #0f172a;
            --card: #1e2937;
            --text: #e2e8f0;
            --accent: #3b82f6;
        }
        
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
            color: var(--text);
            min-height: 100vh;
            padding: 40px 20px;
        }

        h1 {
            text-align: center;
            font-size: 2.8rem;
            margin-bottom: 3rem;
            background: linear-gradient(90deg, #60a5fa, #a5b4fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            text-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
        }

        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
            gap: 28px;
            max-width: 1400px;
            margin: 0 auto;
        }

        .card {
            background: var(--card);
            border-radius: 20px;
            padding: 28px 24px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            border: 1px solid rgba(148, 163, 184, 0.1);
        }

        .card:hover {
            transform: translateY(-12px) scale(1.03);
            box-shadow: 0 20px 40px rgba(59, 130, 246, 0.25);
            border-color: rgba(59, 130, 246, 0.3);
        }

        .card-header h2 {
            font-size: 1.5rem;
            margin-bottom: 20px;
            color: #cbd5e1;
            text-align: center;
        }

        .progress-circle {
            position: relative;
            width: 140px;
            height: 140px;
            margin: 0 auto 25px;
        }

        .progress-circle svg {
            transform: rotate(-90deg);
        }

        .progress-circle circle {
            fill: none;
            stroke-width: 12;
            stroke-linecap: round;
            transition: stroke-dashoffset 1.8s ease;
        }

        .progress-circle .bg {
            stroke: #334155;
        }

        .progress-circle .progress {
            stroke: #22c55e;
            stroke-dasharray: 377;
            stroke-dashoffset: 377;
        }

        .percent {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 1.8rem;
            font-weight: 700;
        }

        .value { color: #22c55e; }
        .symbol { font-size: 1rem; color: #94a3b8; }

        .pie-chart {
            max-height: 220px;
            margin: 15px 0 25px;
        }

        .stats {
            display: flex;
            justify-content: space-around;
            background: rgba(15, 23, 42, 0.6);
            padding: 14px 10px;
            border-radius: 14px;
            margin-top: 10px;
        }

        .stat-item {
            text-align: center;
        }

        .stat-item span {
            font-size: 0.85rem;
            color: #94a3b8;
            display: block;
            margin-bottom: 4px;
        }

        .stat-item.passed strong { color: #22c55e; }
        .stat-item.failed strong { color: #ef4444; }
        .stat-item.skipped strong { color: #64748b; }

        .stat-item strong {
            font-size: 1.35rem;
            font-weight: 600;
        }
    </style>
</head>
<body>

    <h1>🚀 Cucumber Test Dashboard</h1>

    <div class="grid">
        ${cards}
    </div>

    <script>
        ${scripts}
    </script>

</body>
</html>
"""

            writeFile file: "dashboard.html", text: html
            echo "✅ Beautiful animated dashboard generated: dashboard.html"
        }
    }
}
