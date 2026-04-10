module.exports = {
  apps: [
    {
      name: "jenkins-dashboard",
      script: "server.js",

      instances: "max",
      exec_mode: "cluster",

      watch: false,

      // 🔥 ZERO DOWNTIME FEATURE
      max_memory_restart: "300M",

      env: {
        NODE_ENV: "production",
        PORT: 3000
      }
    }
  ]
};