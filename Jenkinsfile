<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function init() {
  const url = window.location.href;

  console.log("Page loaded:", url);

  // STEP 1: Check if returned from Okta
  if (url.includes("session_hint=AUTHENTICATED")) {
    console.log("✅ Returned from Okta");

    // Store login state (like bookmark memory)
    localStorage.setItem("isLoggedIn", "true");

    loadIframe();
    return;
  }

  // STEP 2: If already logged in (bookmark-like behavior)
  if (localStorage.getItem("isLoggedIn") === "true") {
    console.log("✅ Already logged in (from storage)");
    loadIframe();
    return;
  }

  // STEP 3: First time → redirect to Okta
  console.log("❌ Not logged in → redirecting to Okta");

  // Mark that we initiated login
  localStorage.setItem("loginStarted", "true");

  const redirectBack =
    window.location.origin + "?session_hint=AUTHENTICATED";

  const oktaUrl =
    "YOUR_OKTA_LOGIN_URL?redirect_uri=" + encodeURIComponent(redirectBack);

  window.location.href = oktaUrl;
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

// 🔁 This runs EVERY time page loads (like bookmark re-run)
window.onload = function () {
  init();
};
</script>

</body>
</html>
