const express = require("express");
const path = require("path");

const app = express();

app.use(express.static(path.join(__dirname, "public")));

/*
----------------------------------
API (example for dashboard)
----------------------------------
*/
app.get("/api/status", (req, res) => {
  res.json({
    status: "RUNNING 🚀",
    time: new Date()
  });
});

/*
----------------------------------
IMPORTANT: bind to 0.0.0.0
----------------------------------
*/
app.listen(3000, "0.0.0.0", () => {
  console.log("Dashboard running on port 3000");
});