<style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Space+Grotesk:wght@500;600;700&display=swap');

    body {
        margin: 0;
        font-family: 'Inter', system-ui, sans-serif;
        background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
        color: #e2e8f0;
        min-height: 100vh;
    }

    .container {
        padding: 40px 20px;
        max-width: 1600px;
        margin: 0 auto;
    }

    h1 {
        font-family: 'Space Grotesk', sans-serif;
        font-size: 3rem;
        font-weight: 700;
        background: linear-gradient(90deg, #60a5fa, #a78bfa, #f472b6);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        margin-bottom: 10px;
        letter-spacing: -0.02em;
    }

    .subtitle {
        font-size: 1.25rem;
        color: #94a3b8;
        margin-bottom: 50px;
    }

    .grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
        gap: 28px;
        margin-top: 30px;
    }

    /* Make it show more columns on larger screens */
    @media (min-width: 1200px) {
        .grid {
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        }
    }

    @media (min-width: 1600px) {
        .grid {
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        }
    }

    .card {
        background: rgba(255, 255, 255, 0.06);
        backdrop-filter: blur(20px);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 20px;
        padding: 26px 24px;
        box-shadow: 0 10px 30px -10px rgba(0, 0, 0, 0.4);
        transition: all 0.4s cubic-bezier(0.4, 0.1, 0.2, 1);
        position: relative;
        overflow: hidden;
        height: 100%; /* Makes all cards same height in a row */
    }

    .card:hover {
        transform: translateY(-12px);
        box-shadow: 0 25px 50px -12px rgba(96, 165, 250, 0.35);
        border-color: rgba(147, 197, 253, 0.35);
    }

    /* Shine effect */
    .card::before {
        content: '';
        position: absolute;
        top: 0;
        left: -120%;
        width: 40%;
        height: 100%;
        background: linear-gradient(
            90deg,
            transparent,
            rgba(255,255,255,0.20),
            transparent
        );
        transition: 0.8s;
    }

    .card:hover::before {
        left: 150%;
    }

    .status {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        font-size: 0.95rem;
        font-weight: 600;
        padding: 7px 16px;
        border-radius: 9999px;
        margin-bottom: 18px;
    }

    .status.running   { background: rgba(52, 211, 153, 0.18); color: #34d399; }
    .status.completed { background: rgba(147, 197, 253, 0.18); color: #60a5fa; }
    .status.failed    { background: rgba(248, 113, 113, 0.18); color: #f87171; }
    .status.pending   { background: rgba(251, 191, 36, 0.18); color: #fbbf24; }
    .status.queued    { background: rgba(163, 163, 163, 0.18); color: #d1d5db; }

    .status-dot {
        width: 9px;
        height: 9px;
        border-radius: 50%;
        background: currentColor;
    }

    .running .status-dot {
        animation: pulse 1.8s infinite ease-in-out;
    }

    @keyframes pulse {
        0%, 100% { opacity: 1; }
        50% { opacity: 0.3; }
    }

    .card h3 {
        font-size: 1.4rem;
        margin: 0 0 10px 0;
        color: #f1f5f9;
        font-weight: 600;
    }

    .card p {
        color: #94a3b8;
        line-height: 1.55;
        margin: 0 0 20px 0;
    }

    canvas {
        width: 100% !important;
        max-height: 170px;
        border-radius: 14px;
        margin-top: 10px;
    }

    /* Staggered fade-in animation for many cards */
    .card {
        opacity: 0;
        transform: translateY(30px);
        animation: fadeInUp 0.6s forwards cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    /* You can add more delays via inline style or JS if you have 20+ cards */
</style>
