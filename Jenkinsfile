<!DOCTYPE html>
<html>
<body>

<h3>Auth Flow Demo</h3>

<button onclick="runAuth()">1️⃣ Login (Auth JS)</button>
<button onclick="runApp()">2️⃣ Load App (App JS)</button>

<div id="iframeContainer"></div>

<!-- JS FILE 1 -->
<script>
function runAuth() {
  console.log("Running auth flow...");

  const url = window.location.href;

  if (url.includes("session_hint=AUTHENTICATED")) {
    console.log("Already authenticated");
    localStorage.setItem("auth", "true");
    return;
  }

  console.log("Redirecting to Okta...");

  window.location.href = "YOUR_OKTA_LOGIN_URL";
}
</script>

<!-- JS FILE 2 -->
<script>
function runApp() {
  console.log("Running app flow...");

  if (localStorage.getItem("auth") !== "true") {
    alert("Not authenticated yet!");
    return;
  }

  loadIframe();
}

function loadIframe() {
  const container = document.getElementById("iframeContainer");

  if (container.querySelector("iframe")) return;

  const iframe = document.createElement("iframe");
  iframe.src = "https://your-jenkins-url";
  iframe.width = "100%";
  iframe.height = "800px";
  iframe.style.border = "none";

  container.appendChild(iframe);
}
</script>

</body>
</html>
