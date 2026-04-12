 <style>
    :root {
      --primary: #4361ee;
      --success: #4cc9f0;
      --danger: #f72585;
      --warning: #f8961e;
      --dark: #212529;
      --light: #f8f9fa;
      --card-bg: #ffffff;
      --header-bg: linear-gradient(135deg, #4361ee 0%, #3a0ca3 100%);
      --transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }
    
    body {
      font-family: 'Poppins', sans-serif;
      background-color: #f5f7fa;
      color: #495057;
      min-height: 100vh;
    }
    
    /* Service Card - Enhanced with 3D effects */
    .service-card {
      background: var(--card-bg);
      border-radius: 16px;
      padding: 1.5rem;
      margin-bottom: 1.5rem;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
      transition: var(--transition);
      border: none;
      position: relative;
      overflow: hidden;
      transform-style: preserve-3d;
      perspective: 1000px;
      will-change: transform;
      z-index: 1;
    }
    
    .service-card::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, var(--success), var(--primary));
      transform: scaleX(0);
      transform-origin: left;
      transition: transform 0.6s cubic-bezier(0.65, 0, 0.35, 1);
      z-index: -1;
    }
    
    .service-card:hover {
      transform: translateY(-8px) rotateX(5deg) rotateY(2deg);
      box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
    }
    
    .service-card:hover::before {
      transform: scaleX(1);
    }
    
    /* Status-specific styling */
    .service-card.success {
      --card-accent: var(--success);
    }
    
    .service-card.warning {
      --card-accent: var(--warning);
    }
    
    .service-card.error {
      --card-accent: var(--danger);
    }
    
    /* Card glow effect */
    .service-card::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      border-radius: inherit;
      box-shadow: 0 0 20px 0px var(--card-accent);
      opacity: 0;
      transition: opacity 0.4s ease;
      z-index: -1;
    }
    
    .service-card:hover::after {
      opacity: 0.3;
    }
    
    /* Floating animation for important cards */
    @keyframes float {
      0% { transform: translateY(0px); }
      50% { transform: translateY(-10px); }
      100% { transform: translateY(0px); }
    }
    
    .service-card.important {
      animation: float 4s ease-in-out infinite;
    }
    
    /* Status indicator with animation */
    .status-indicator {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      margin-right: 0.5rem;
      position: relative;
    }
    
    .status-indicator::before {
      content: '';
      position: absolute;
      width: 100%;
      height: 100%;
      border-radius: 50%;
      background: inherit;
      animation: pulse 2s infinite;
      opacity: 0.7;
    }
    
    .status-indicator.success {
      background: var(--success);
      box-shadow: 0 0 15px rgba(76, 201, 240, 0.7);
    }
    
    .status-indicator.warning {
      background: var(--warning);
      box-shadow: 0 0 15px rgba(248, 150, 30, 0.7);
    }
    
    .status-indicator.error {
      background: var(--danger);
      box-shadow: 0 0 15px rgba(247, 37, 133, 0.7);
    }
    
    @keyframes pulse {
      0% { transform: scale(1); opacity: 0.7; }
      70% { transform: scale(2.5); opacity: 0; }
      100% { transform: scale(1); opacity: 0; }
    }
    
    /* Percentage display with gradient text */
    .percentage {
      font-size: 2.8rem;
      font-weight: 700;
      margin: 1rem 0;
      line-height: 1;
      background: linear-gradient(135deg, var(--card-accent), #4361ee);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      position: relative;
      display: inline-block;
    }
    
    /* Animated underline effect */
    .percentage::after {
      content: '';
      position: absolute;
      bottom: -5px;
      left: 0;
      width: 100%;
      height: 3px;
      background: linear-gradient(90deg, var(--card-accent), transparent);
      transform: scaleX(0);
      transform-origin: right;
      transition: transform 0.6s cubic-bezier(0.65, 0, 0.35, 1);
    }
    
    .service-card:hover .percentage::after {
      transform: scaleX(1);
      transform-origin: left;
    }
    
    /* Progress bar with animation */
    .progress-container {
      height: 8px;
      background: #e9ecef;
      border-radius: 4px;
      margin: 1.5rem 0;
      overflow: hidden;
      position: relative;
    }
    
    .progress-container::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(90deg, 
        rgba(255,255,255,0) 0%, 
        rgba(255,255,255,0.8) 50%, 
        rgba(255,255,255,0) 100%);
      animation: shine 2s infinite;
    }
    
    .progress-bar {
      height: 100%;
      border-radius: 4px;
      transition: width 1s cubic-bezier(0.65, 0, 0.35, 1);
      position: relative;
      z-index: 1;
    }
    
    @keyframes shine {
      0% { transform: translateX(-100%); }
      100% { transform: translateX(100%); }
    }
    
    /* Date display styles */
    .date-display {
      display: flex;
      justify-content: space-between;
      margin-top: 0.5rem;
      font-size: 0.8rem;
      color: #6c757d;
    }
    
    .date-item {
      display: flex;
      align-items: center;
    }
    
    .date-item i {
      margin-right: 0.3rem;
      font-size: 0.9rem;
    }
    
    /* Card entry animation */
    @keyframes cardEntrance {
      from {
        opacity: 0;
        transform: translateY(50px) scale(0.9);
      }
      to {
        opacity: 1;
        transform: translateY(0) scale(1);
      }
    }
    
    /* Responsive adjustments */
    @media (max-width: 768px) {
      .service-card {
        padding: 1.25rem;
      }
      
      .percentage {
        font-size: 2.2rem;
      }
      
      .date-display {
        flex-direction: column;
        gap: 0.3rem;
      }
    }
  </style>
