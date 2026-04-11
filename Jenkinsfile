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
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
            color: #e2e8f0;
            min-height: 100vh;
            padding: 40px 20px;
        }
        
        .container {
            max-width: 1100px;
            margin: 0 auto;
        }
        
        header {
            text-align: center;
            margin-bottom: 50px;
        }
        
        h1 {
            font-size: 2.8rem;
            font-weight: 700;
            background: linear-gradient(90deg, #67e8f9, #c084fc);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .subtitle {
            font-size: 1.2rem;
            color: #94a3b8;
            margin-top: 10px;
        }
        
        /* Beautiful Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 28px;
            margin-bottom: 50px;
        }
        
        .card {
            background: rgba(30, 41, 59, 0.85);
            backdrop-filter: blur(16px);
            border-radius: 24px;
            padding: 32px 28px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.4),
                        inset 0 2px 0 rgba(255,255,255,0.08);
            border: 1px solid rgba(103, 232, 249, 0.15);
            transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
            position: relative;
            overflow: hidden;
        }
        
        .card:hover {
            transform: translateY(-15px) scale(1.03);
            box-shadow: 0 25px 50px rgba(103, 232, 249, 0.25),
                        inset 0 2px 0 rgba(255,255,255,0.12);
            border-color: rgba(103, 232, 249, 0.4);
        }
        
        .card::before {
            content: '';
            position: absolute;
            top: -100%;
            left: -100%;
            width: 40%;
            height: 300%;
            background: linear-gradient(
                120deg,
                transparent,
                rgba(255,255,255,0.25),
                transparent
            );
            transition: 0.7s;
        }
        
        .card:hover::before {
            top: -20%;
            left: -20%;
        }
        
        .card h3 {
            font-size: 1.15rem;
            font-weight: 600;
            color: #94a3b8;
            margin-bottom: 16px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .value {
            font-size: 3.6rem;
            font-weight: 700;
            line-height: 1;
            margin-bottom: 8px;
            transition: transform 0.4s ease;
        }
        
        .card:hover .value {
            transform: scale(1.08);
        }
        
        .pass-card .value { color: #4ade80; text-shadow: 0 0 20px rgba(74, 222, 128, 0.4); }
        .fail-card .value { color: #f87171; text-shadow: 0 0 20px rgba(248, 113, 113, 0.4); }
        .skip-card .value { color: #cbd5e1; text-shadow: 0 0 15px rgba(203, 213, 225, 0.3); }
        
        /* Progress Circle */
        .progress-container {
            width: 230px;
            height: 230px;
            margin: 40px auto;
            position: relative;
        }
        
        .progress-ring {
            transform: rotate(-90deg);
        }
        
        .progress-ring__circle {
            transition: stroke-dashoffset 2s cubic-bezier(0.25, 0.1, 0.25, 1);
        }
        
        .progress-text {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 2.9rem;
            font-weight: 700;
        }
        
        .progress-text small {
            font-size: 1.05rem;
            color: #94a3b8;
            margin-top: 6px;
            display: block;
        }
        
        .chart-container {
            background: rgba(30, 41, 59, 0.9);
            border-radius: 24px;
            padding: 35px;
            margin-bottom: 40px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            background: rgba(30, 41, 59, 0.9);
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
        
        th, td {
            padding: 22px 25px;
            text-align: center;
            font-size: 1.2rem;
        }
        
        th {
            background: #334155;
            color: #67e8f9;
        }
        
        .passed { color: #4ade80; }
        .failed { color: #f87171; }
        .skipped { color: #cbd5e1; }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>Cucumber Test Dashboard</h1>
            <p class="subtitle">Automated Test Results • Real-time Analytics</p>
        </header>

        <!-- Pass Percentage Circle -->
        <div class="progress-container">
            <svg class="progress-ring" width="230" height="230">
                <circle cx="115" cy="115" r="102" fill="none" stroke="#334155" stroke-width="24"/>
                <circle class="progress-ring__circle" cx="115" cy="115" r="102" fill="none" 
                        stroke="#22d3ee" stroke-width="24" 
                        stroke-dasharray="641" stroke-dashoffset="641"
                        stroke-linecap="round"/>
            </svg>
            <div class="progress-text">
                <span id="passPercentBig">0</span><span style="font-size:1.9rem;">%</span>
                <small>Overall Pass Rate</small>
            </div>
        </div>

        <!-- Beautiful Cards -->
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

        <!-- Chart -->
        <div class="chart-container">
            <h2 style="margin-bottom: 25px; text-align: center; color: #e2e8f0;">Test Status Distribution</h2>
            <canvas id="chart" height="280"></canvas>
        </div>

        <!-- Table -->
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
        // Safe number conversion to fix NaN
        function safeNum(val) {
            const n = parseFloat(val);
            return isNaN(n) ? 0 : n;
        }

        const passed      = safeNum("${data.passed}");
        const failed      = safeNum("${data.failed}");
        const skipped     = safeNum("${data.skipped}");
        const passPercent = safeNum("${data.passPercent}");

        // Counter animation
        function animateValue(id, start, end, duration = 1600) {
            const obj = document.getElementById(id);
            let startTime = null;
            
            function step(timestamp) {
                if (!startTime) startTime = timestamp;
                const progress = Math.min((timestamp - startTime) / duration, 1);
                const value = Math.floor(progress * (end - start) + start);
                obj.textContent = value;
                if (progress < 1) {
                    window.requestAnimationFrame(step);
                }
            }
            window.requestAnimationFrame(step);
        }

        // Progress ring animation
        function animateProgress(percent) {
            const circle = document.querySelector('.progress-ring__circle');
            const circumference = 641;
            const offset = circumference * (1 - percent / 100);
            circle.style.strokeDashoffset = offset;
        }

        window.onload = function() {
            animateValue('passedCount', 0, passed);
            animateValue('failedCount', 0, failed);
            animateValue('skippedCount', 0, skipped);
            animateValue('passPercentBig', 0, passPercent);

            setTimeout(() => {
                animateProgress(passPercent);
            }, 500);

            // Pie Chart
            new Chart(document.getElementById('chart'), {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [passed, failed, skipped],
                        backgroundColor: ['#4ade80', '#f87171', '#64748b'],
                        borderColor: '#1e2937',
                        borderWidth: 6,
                        hoverOffset: 25
                    }]
                },
                options: {
                    responsive: true,
                    animation: { duration: 1800 },
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: { color: '#e2e8f0', font: { size: 15 }, padding: 20 }
                        }
                    }
                }
            });
        };
    </script>
</body>
</html>
"""
