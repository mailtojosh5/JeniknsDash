def html = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cucumber Analytics Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        
        :root {
            --bg: #0f172a;
            --card: #1e2937;
            --text: #e2e8f0;
            --accent: #22d3ee;
        }
        
        * { margin: 0; padding: 0; box-sizing: border-box; }
        
        body {
            font-family: 'Inter', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
            color: var(--text);
            min-height: 100vh;
            padding: 40px 20px;
        }
        
        .container { max-width: 1100px; margin: 0 auto; }
        
        header { text-align: center; margin-bottom: 50px; }
        
        h1 {
            font-size: 2.8rem;
            font-weight: 700;
            background: linear-gradient(90deg, #67e8f9, #c084fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            margin-bottom: 10px;
        }
        
        .subtitle { font-size: 1.2rem; color: #94a3b8; }
        
        .progress-container {
            width: 220px;
            height: 220px;
            margin: 30px auto;
            position: relative;
        }
        
        .progress-ring { transform: rotate(-90deg); }
        
        .progress-ring__circle {
            transition: stroke-dashoffset 1.8s cubic-bezier(0.25, 0.1, 0.25, 1);
        }
        
        .progress-text {
            position: absolute;
            top: 50%; left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 2.8rem;
            font-weight: 700;
        }
        
        .progress-text small {
            display: block;
            font-size: 1rem;
            color: #94a3b8;
            margin-top: 8px;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
            margin-bottom: 50px;
        }
        
        .card {
            background: var(--card);
            border-radius: 20px;
            padding: 30px 25px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            transition: all 0.4s ease;
        }
        
        .card:hover { transform: translateY(-12px); }
        
        .value {
            font-size: 3.2rem;
            font-weight: 700;
        }
        
        .pass-card .value { color: #4ade80; }
        .fail-card .value { color: #f87171; }
        .skip-card .value { color: #94a3b8; }
        
        .chart-container {
            background: var(--card);
            border-radius: 20px;
            padding: 35px;
            margin-bottom: 40px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--card);
            border-radius: 16px;
            overflow: hidden;
        }
        
        th, td { padding: 22px; text-align: center; font-size: 1.15rem; }
        th { background: #334155; color: #67e8f9; }
        .passed { color: #4ade80; }
        .failed { color: #f87171; }
        .skipped { color: #94a3b8; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Cucumber Test Dashboard</h1>
            <p class="subtitle">Automated Test Results • Real-time Analytics</p>
        </header>

        <div class="progress-container">
            <svg class="progress-ring" width="220" height="220">
                <circle cx="110" cy="110" r="98" fill="none" stroke="#334155" stroke-width="22"/>
                <circle class="progress-ring__circle" cx="110" cy="110" r="98" fill="none" 
                        stroke="#22d3ee" stroke-width="22" stroke-dasharray="615" stroke-dashoffset="615"
                        stroke-linecap="round"/>
            </svg>
            <div class="progress-text">
                <span id="passPercentBig">0</span><span style="font-size:1.8rem;">%</span>
                <small>Pass Rate</small>
            </div>
        </div>

        <div class="stats-grid">
            <div class="card pass-card">
                <h3>✅ Tests Passed</h3>
                <div id="passedCount" class="value">0</div>
            </div>
            <div class="card fail-card">
                <h3>❌ Tests Failed</h3>
                <div id="failedCount" class="value">0</div>
            </div>
            <div class="card skip-card">
                <h3>⏭️ Tests Skipped</h3>
                <div id="skippedCount" class="value">0</div>
            </div>
        </div>

        <div class="chart-container">
            <h2 style="margin-bottom: 25px; text-align: center;">Test Status Distribution</h2>
            <canvas id="chart" height="280"></canvas>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Passed</th>
                    <th>Failed</th>
                    <th>Skipped</th>
                    <th>Pass Rate</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td class="passed">${data.passed ?: 0}</td>
                    <td class="failed">${data.failed ?: 0}</td>
                    <td class="skipped">${data.skipped ?: 0}</td>
                    <td style="font-weight:700; color:#67e8f9;">${data.passPercent ?: 0}%</td>
                </tr>
            </tbody>
        </table>
    </div>

    <script>
        // Safe number parsing to prevent NaN
        function safeNum(val) {
            const n = parseFloat(val);
            return isNaN(n) ? 0 : n;
        }

        const passed     = safeNum("${data.passed}");
        const failed     = safeNum("${data.failed}");
        const skipped    = safeNum("${data.skipped}");
        const passPercent = safeNum("${data.passPercent}");

        // Animate counters
        function animateValue(id, start, end, duration) {
            const obj = document.getElementById(id);
            const range = end - start;
            const startTime = Date.now();
            
            function update() {
                const now = Date.now();
                const progress = Math.min((now - startTime) / duration, 1);
                const value = Math.floor(progress * range + start);
                obj.textContent = value;
                if (progress < 1) requestAnimationFrame(update);
            }
            update();
        }

        // Animate circular progress
        function animateProgress(percent) {
            const circle = document.querySelector('.progress-ring__circle');
            const circumference = 615;
            const offset = circumference * (1 - percent / 100);
            circle.style.strokeDashoffset = offset;
        }

        window.onload = function() {
            // Counters
            animateValue('passedCount', 0, passed, 1600);
            animateValue('failedCount', 0, failed, 1600);
            animateValue('skippedCount', 0, skipped, 1600);
            animateValue('passPercentBig', 0, passPercent, 1600);

            // Progress ring
            setTimeout(() => animateProgress(passPercent), 400);

            // Chart
            new Chart(document.getElementById('chart'), {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [passed, failed, skipped],
                        backgroundColor: ['#4ade80', '#f87171', '#64748b'],
                        borderColor: '#1e2937',
                        borderWidth: 6,
                        hoverOffset: 30
                    }]
                },
                options: {
                    responsive: true,
                    animation: { duration: 1800, easing: 'easeOutQuart' },
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: { color: '#e2e8f0', font: { size: 15 }, padding: 25 }
                        }
                    }
                }
            });
        };
    </script>
</body>
</html>
"""
