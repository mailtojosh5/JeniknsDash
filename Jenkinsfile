<style>
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Space+Grotesk:wght@500;600;700&display=swap');

    body {
        margin: 0;
        font-family: 'Inter', system-ui, sans-serif;
        background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
        color: #e2e8f0;
        min-height: 100vh;
        overflow-x: hidden;
    }

    .container {
        padding: 40px 20px;
        max-width: 1400px;
        margin: 0 auto;
        text-align: center;
    }

    h1 {
        font-family: 'Space Grotesk', sans-serif;
        font-size: 3.2rem;
        font-weight: 700;
        background: linear-gradient(90deg, #60a5fa, #a78bfa, #f472b6);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        margin-bottom: 12px;
        letter-spacing: -0.02em;
    }

    .subtitle {
        font-size: 1.25rem;
        color: #94a3b8;
        margin-bottom: 50px;
        font-weight: 400;
    }

    .grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
        gap: 28px;
        margin-top: 40px;
    }

    .card {
        background: rgba(255, 255, 255, 0.06);
        backdrop-filter: blur(20px);
        border: 1px solid rgba(255, 255, 255, 0.1);
        border-radius: 20px;
        padding: 28px 24px;
        box-shadow: 0 10px 30px -10px rgba(0, 0, 0, 0.4);
        transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;
    }

    .card::before {
        content: '';
        position: absolute;
        top: 0;
        left: -100%;
        width: 50%;
        height: 100%;
        background: linear-gradient(
            90deg,
            transparent,
            rgba(255,255,255,0.15),
            transparent
        );
        transition: 0.7s;
    }

    .card:hover::before {
        left: 200%;
    }

    .card:hover {
        transform: translateY(-12px) scale(1.03);
        box-shadow: 0 20px 40px -15px rgba(96, 165, 250, 0.3);
        border-color: rgba(147, 197, 253, 0.3);
    }

    .card h3 {
        font-size: 1.5rem;
        margin: 0 0 12px 0;
        color: #f1f5f9;
        font-weight: 600;
    }

    canvas {
        max-height: 260px;
        width: 100% !important;
        border-radius: 16px;
        margin-top: 16px;
    }

    /* Enhanced fade-in animation with staggered effect */
    .fade {
        opacity: 0;
        transform: translateY(40px);
        animation: fadeInUp 0.8s forwards cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    .card:nth-child(1) { animation-delay: 100ms; }
    .card:nth-child(2) { animation-delay: 200ms; }
    .card:nth-child(3) { animation-delay: 300ms; }
    .card:nth-child(4) { animation-delay: 400ms; }

    @keyframes fadeInUp {
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* Subtle glow effect on cards */
    .card {
        position: relative;
    }

    .card:after {
        content: '';
        position: absolute;
        inset: -1px;
        background: linear-gradient(45deg, #60a5fa, #c084fc, #f472b6);
        border-radius: 21px;
        z-index: -1;
        opacity: 0;
        transition: opacity 0.4s ease;
        filter: blur(12px);
    }

    .card:hover:after {
        opacity: 0.15;
    }
</style>
