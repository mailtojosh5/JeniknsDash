<style>
    :root {
        --primary: #4a9eff;
        --accent: #00d4ff;
        --bg-dark: #0f172a;
        --card-bg: rgba(255, 255, 255, 0.06);
        --text-light: #e2e8f0;
        --text-muted: #94a3b8;
    }

    body {
        margin: 0;
        font-family: 'Segoe UI', system-ui, -apple-system, BlinkMacSystemFont, 'Roboto', sans-serif;
        background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
        color: var(--text-light);
        line-height: 1.6;
    }

    .container {
        max-width: 1280px;
        margin: 0 auto;
        padding: 40px 20px;
        text-align: center;
    }

    h1 {
        font-size: 42px;
        font-weight: 600;
        letter-spacing: -0.5px;
        margin-bottom: 12px;
        background: linear-gradient(90deg, #ffffff, var(--accent));
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
    }

    .subtitle {
        font-size: 18px;
        color: var(--text-muted);
        margin-bottom: 50px;
    }

    .grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
        gap: 28px;
        margin-top: 40px;
    }

    .card {
        background: var(--card-bg);
        border-radius: 20px;
        padding: 28px 24px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4);
        border: 1px solid rgba(255, 255, 255, 0.08);
        transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
        backdrop-filter: blur(12px);
    }

    .card:hover {
        transform: translateY(-8px);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
        border-color: rgba(74, 158, 255, 0.2);
    }

    .card h3 {
        margin: 0 0 16px 0;
        font-size: 22px;
        font-weight: 600;
    }

    canvas {
        max-height: 280px;
        width: 100% !important;
        border-radius: 12px;
        margin-top: 12px;
    }

    .fade {
        animation: fadeInUp 0.8s ease forwards;
    }

    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* Optional: Add subtle glow to accents */
    .highlight {
        color: var(--accent);
    }
</style>
