pipeline {
    agent any
    stages {
        stage('Copy Cucumber Reports From Jobs') {
            steps {
                script {
                    jobs = [
                        "API-Tests",
                        "UI-Tests",
                        "Regression-Tests"
                    ]
                    for (j in jobs) {
                        echo "Copying cucumber report from ${j}"
                        copyArtifacts(
                            projectName: j,
                            selector: lastSuccessful(),
                            filter: "**/cucumber.json",
                            target: "reports/${j}",
                            flatten: true,
                            optional: true
                        )
                    }
                }
            }
        }

        stage('Parse Cucumber Reports') {
            steps {
                script {
                    def passed = 0
                    def failed = 0
                    def skipped = 0
                    def files = findFiles(glob: 'reports/**/cucumber.json')
                    echo "Found ${files.size()} cucumber reports"

                    files.each { file ->
                        def json = readJSON file: file.path
                        json.each { feature ->
                            feature.elements?.each { scenario ->
                                scenario.steps?.each { step ->
                                    def status = step.result?.status ?: "skipped"
                                    if (status == "passed") passed++
                                    if (status == "failed") failed++
                                    if (status == "skipped") skipped++
                                }
                            }
                        }
                    }

                    def total = passed + failed + skipped
                    def passPercent = total > 0 ? ((passed * 100) / total).round(2) : 0

                    echo "Total Test Cases: ${total}"
                    echo "Passed: ${passed}"
                    echo "Failed: ${failed}"
                    echo "Skipped: ${skipped}"
                    echo "Pass %: ${passPercent}"

                    // Write summary for dashboard
                    writeFile file: "summary.json", text: groovy.json.JsonOutput.toJson([
                        total: total,
                        passed: passed,
                        failed: failed,
                        skipped: skipped,
                        passPercent: passPercent
                    ])
                }
            }
        }

        stage('Generate Dashboard') {
            steps {
                script {
                    def data = readJSON file: 'summary.json'

                    def html = """
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cucumber Analytics Dashboard</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <script src="https://cdn.tailwindcss.com"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');
        
        body {
            font-family: 'Inter', system_ui, sans-serif;
        }
        
        .card {
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
        }
        
        .card:hover {
            transform: translateY(-8px);
            box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
        }
        
        .progress-circle {
            position: relative;
            width: 160px;
            height: 160px;
            margin: 0 auto;
        }
        
        .progress-circle svg {
            transform: rotate(-90deg);
        }
        
        .progress-circle circle {
            fill: none;
            stroke-width: 14;
            stroke-linecap: round;
            transition: stroke-dashoffset 2s ease;
        }
        
        .progress-circle .bg-circle {
            stroke: #334155;
        }
        
        .progress-circle .progress-circle {
            stroke: #22c55e;
        }
        
        .percent-text {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
        }
    </style>
</head>
<body class="bg-slate-950 text-slate-200 min-h-screen py-12 px-6">
    <div class="max-w-5xl mx-auto">
        <!-- Header -->
        <div class="text-center mb-12">
            <h1 class="text-5xl font-bold bg-gradient-to-r from-blue-400 to-indigo-400 bg-clip-text text-transparent">
                🚀 Cucumber Test Analytics
            </h1>
            <p class="text-slate-400 mt-3 text-lg">Overall Execution Summary</p>
        </div>

        <!-- Global Summary Card -->
        <div class="bg-slate-900 rounded-3xl p-10 mb-12 border border-slate-800 card">
            <div class="grid grid-cols-1 md:grid-cols-5 gap-8 items-center">
                <!-- Total Test Cases -->
                <div class="text-center">
                    <div class="text-6xl font-bold text-white">${data.total}</div>
                    <div class="text-slate-400 text-sm tracking-widest mt-2">TOTAL TEST CASES</div>
                </div>

                <!-- Progress Circle -->
                <div class="flex justify-center">
                    <div class="progress-circle">
                        <svg width="160" height="160">
                            <circle class="bg-circle" cx="80" cy="80" r="70"></circle>
                            <circle class="progress-circle" cx="80" cy="80" r="70"
                                    stroke-dasharray="439.82"
                                    stroke-dashoffset="${439.82 - (439.82 * data.passPercent / 100)}"></circle>
                        </svg>
                        <div class="percent-text">
                            <div class="text-5xl font-semibold text-emerald-400">${data.passPercent}</div>
                            <div class="text-xs text-slate-400 -mt-1">PASS %</div>
                        </div>
                    </div>
                </div>

                <!-- Stats -->
                <div class="md:col-span-3 grid grid-cols-3 gap-6">
                    <div class="text-center">
                        <div class="text-emerald-400 text-4xl font-semibold">${data.passed}</div>
                        <div class="text-emerald-500 text-sm font-medium mt-1">PASSED</div>
                    </div>
                    <div class="text-center">
                        <div class="text-red-400 text-4xl font-semibold">${data.failed}</div>
                        <div class="text-red-500 text-sm font-medium mt-1">FAILED</div>
                    </div>
                    <div class="text-center">
                        <div class="text-slate-400 text-4xl font-semibold">${data.skipped}</div>
                        <div class="text-slate-500 text-sm font-medium mt-1">SKIPPED</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Doughnut Chart -->
        <div class="bg-slate-900 rounded-3xl p-10 border border-slate-800 card">
            <h2 class="text-2xl font-semibold mb-8 text-center">Test Results Breakdown</h2>
            <div class="max-w-md mx-auto">
                <canvas id="resultChart" height="300"></canvas>
            </div>
        </div>

        <!-- Detailed Numbers -->
        <div class="mt-8 text-center text-slate-500 text-sm">
            Generated on ${new Date().format("yyyy-MM-dd HH:mm:ss")}
        </div>
    </div>

    <script>
        // Tailwind script already loaded via CDN

        const ctx = document.getElementById('resultChart');
        
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Passed', 'Failed', 'Skipped'],
                datasets: [{
                    data: [${data.passed}, ${data.failed}, ${data.skipped}],
                    backgroundColor: ['#22c55e', '#ef4444', '#64748b'],
                    borderColor: '#0f172a',
                    borderWidth: 6,
                    hoverOffset: 20
                }]
            },
            options: {
                cutout: '72%',
                animation: {
                    duration: 2000,
                    easing: 'easeOutQuart'
                },
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 25,
                            font: { size: 15 },
                            usePointStyle: true,
                            color: '#e2e8f0'
                        }
                    }
                }
            }
        });
    </script>
</body>
</html>
"""

                    writeFile file: "dashboard.html", text: html
                    echo "✅ Modern animated Cucumber Dashboard generated successfully!"
                }
            }
        }

        stage('Publish Dashboard') {
            steps {
                publishHTML([
                    reportDir: '.',
                    reportFiles: 'dashboard.html',
                    reportName: 'Cucumber Analytics Dashboard',
                    keepAll: true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }
}
