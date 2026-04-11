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
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Inter', system-ui, sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
            color: var(--text);
            min-height: 100vh;
            padding: 40px 20px;
            line-height: 1.6;
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
            margin-bottom: 10px;
            animation: fadeInDown 1s ease-out;
        }
        
        .subtitle {
            font-size: 1.2rem;
            color: #94a3b8;
            animation: fadeInDown 1s ease-out 0.2s both;
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
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            overflow: hidden;
        }
        
        .card:hover {
            transform: translateY(-12px);
            box-shadow: 0 20px 40px rgba(103, 232, 249, 0.15);
        }
        
        .card::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(103,232,249,0.08) 0%, transparent 70%);
            opacity: 0;
            transition: opacity 0.5s;
        }
        
        .card:hover::before {
            opacity: 1;
        }
        
        .card h3 {
            font-size: 1.1rem;
            color: #94a3b8;
            margin-bottom: 12px;
            font-weight: 500;
        }
        
        .value {
            font-size: 3.2rem;
            font-weight: 700;
            margin-bottom: 8px;
            transition: all 1.2s ease-out;
        }
        
        .pass-card .value { color: #4ade80; }
        .fail-card .value { color: #f87171; }
        .skip-card .value { color: #94a3b8; }
        
        /* Animated Circular Progress */
        .progress-container {
            width: 220px;
            height: 220px;
            margin: 30px auto;
            position: relative;
        }
        
        .progress-ring {
            transform: rotate(-90deg);
        }
        
        .progress-ring__circle {
            transition: stroke-dashoffset 1.8s cubic-bezier(0.25, 0.1, 0.25, 1);
        }
        
        .progress-text {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            font-size: 2.8rem;
            font-weight: 700;
            line-height: 1;
        }
        
        .progress-text small {
            display: block;
            font-size: 1rem;
            font-weight: 500;
            color: #94a3b8;
            margin-top: 8px;
        }
        
        .chart-container {
            background: var(--card);
            border-radius: 20px;
            padding: 35px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            margin-bottom: 40px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--card);
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
        
        th, td {
            padding: 22px 25px;
            text-align: center;
            font-size: 1.15rem;
        }
        
        th {
            background: #334155;
            color: #67e8f9;
            font-weight: 600;
        }
        
        tr:nth-child(even) {
            background: #1e2937;
        }
        
        td {
            font-weight: 600;
        }
        
        .passed { color: #4ade80; }
        .failed { color: #f87171; }
        .skipped { color: #94a3b8; }
        
        /* Animations */
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .animate-in {
            animation: fadeInDown 0.8s ease-out forwards;
        }
        
        .counter {
            display: inline-block;
        }
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
            <svg class="progress-ring" width="220" height="220">
                <circle class="progress-ring__bg" cx="110" cy="110" r="98" fill="none" stroke="#334155" stroke-width="22"/>
                <circle class="progress-ring__circle" cx="110" cy="110" r="98" fill="none" 
                        stroke="#22d3ee" stroke-width="22" stroke-dasharray="615" stroke-dashoffset="615"
                        stroke-linecap="round"/>
            </svg>
            <div class="progress-text">
                <span id="passPercentBig" class="counter">0</span><span style="font-size:1.8rem;">%</span>
                <small>Pass Rate</small>
            </div>
        </div>

        <div class="stats-grid">
            <!-- Passed -->
            <div class="card pass-card animate-in" style="animation-delay: 0.3s">
                <h3>✅ Tests Passed</h3>
                <div id="passedCount" class="value counter">0</div>
            </div>
            
            <!-- Failed -->
            <div class="card fail-card animate-in" style="animation-delay: 0.5s">
                <h3>❌ Tests Failed</h3>
                <div id="failedCount" class="value counter">0</div>
            </div>
            
            <!-- Skipped -->
            <div class="card skip-card animate-in" style="animation-delay: 0.7s">
                <h3>⏭️ Tests Skipped</h3>
                <div id="skippedCount" class="value counter">0</div>
            </div>
        </div>

        <div class="chart-container">
            <h2 style="margin-bottom: 25px; text-align: center; color: #e2e8f0;">Test Status Distribution</h2>
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
                    <td class="passed">${data.passed}</td>
                    <td class="failed">${data.failed}</td>
                    <td class="skipped">${data.skipped}</td>
                    <td style="font-weight:700; color:#67e8f9;">${data.passPercent}%</td>
                </tr>
            </tbody>
        </table>
    </div>

    <script>
        const data = {
            passed: ${data.passed},
            failed: ${data.failed},
            skipped: ${data.skipped},
            passPercent: ${data.passPercent}
        };

        // Animate number counters
        function animateCounter(id, target, duration = 1800) {
            const element = document.getElementById(id);
            let start = 0;
            const increment = target / (duration / 16);
            
            const timer = setInterval(() => {
                start += increment;
                if (start >= target) {
                    element.textContent = Math.floor(target);
                    clearInterval(timer);
                } else {
                    element.textContent = Math.floor(start);
                }
            }, 16);
        }

        // Animate circular progress
        function animateProgress(percent) {
            const circle = document.querySelector('.progress-ring__circle');
            const circumference = 615; // 2 * π * 98
            const offset = circumference - (circumference * percent / 100);
            
            circle.style.strokeDashoffset = offset;
        }

        // Initialize everything
        window.onload = function() {
            // Counters
            animateCounter('passedCount', data.passed);
            animateCounter('failedCount', data.failed);
            animateCounter('skippedCount', data.skipped);
            animateCounter('passPercentBig', data.passPercent);
            
            // Circular progress
            setTimeout(() => {
                animateProgress(data.passPercent);
            }, 600);

            // Chart.js Pie with nice animation
            const ctx = document.getElementById('chart').getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: ['Passed', 'Failed', 'Skipped'],
                    datasets: [{
                        data: [data.passed, data.failed, data.skipped],
                        backgroundColor: [
                            '#4ade80',
                            '#f87171',
                            '#64748b'
                        ],
                        borderColor: '#1e2937',
                        borderWidth: 5,
                        hoverOffset: 25
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    animation: {
                        duration: 1800,
                        easing: 'easeOutQuart'
                    },
                    plugins: {
                        legend: {
                            position: 'bottom',
                            labels: {
                                color: '#e2e8f0',
                                font: { size: 15, weight: '500' },
                                padding: 25,
                                usePointStyle: true
                            }
                        },
                        tooltip: {
                            backgroundColor: '#1e2937',
                            titleColor: '#e2e8f0',
                            bodyColor: '#e2e8f0',
                            padding: 14,
                            cornerRadius: 10
                        }
                    }
                }
            });
        };
    </script>
</body>
</html>
"""
