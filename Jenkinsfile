<style>
body {
    margin: 0;
    font-family: 'Inter', system-ui, sans-serif;
    background: linear-gradient(135deg, #0f172a 0%, #1e2937 100%);
    color: #e2e8f0;
}

.container {
    padding: 40px 20px;
    max-width: 1600px;
    margin: 0 auto;
    text-align: center;
}

h1 {
    font-family: 'Space Grotesk', sans-serif;
    font-size: 3rem;
    font-weight: 700;
    background: linear-gradient(90deg, #60a5fa, #a78bfa, #f472b6);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    margin-bottom: 12px;
    letter-spacing: -0.02em;
}

.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 28px;
    margin-top: 40px;
}

.card {
    background: rgba(255, 255, 255, 0.06);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 20px;
    padding: 26px 24px;
    box-shadow: 0 10px 30px -10px rgba(0, 0, 0, 0.4);
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;
    height: 100%;
}

.card:hover {
    transform: translateY(-12px);
    box-shadow: 0 25px 50px -15px rgba(96, 165, 250, 0.35);
    border-color: rgba(147, 197, 253, 0.3);
}

.card::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 50%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255,255,255,0.18), transparent);
    transition: 0.7s;
}

.card:hover::before {
    left: 200%;
}

canvas {
    max-height: 220px;
    width: 100% !important;
    border-radius: 14px;
    margin-top: 12px;
}

.fade {
    opacity: 0;
    transform: translateY(30px);
    animation: fadeInUp 0.8s forwards cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

@keyframes fadeInUp {
    to {
        opacity: 1;
        transform: translateY(0);
    }
}
</style>
