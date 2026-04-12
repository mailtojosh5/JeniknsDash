<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function init() {
  const url = window.location.href;

  console.log("Current URL:", url);

  // STEP 1: After Okta login comes back here
  if (url.includes("session_hint=AUTHENTICATED")) {
    console.log("✅ Authenticated → loading iframe");
    loadIframe();
    return;
  }

  // STEP 2: Not authenticated → send to Okta
  console.log("❌ Not authenticated → redirecting to Okta");

  const redirectBack =
    window.location.origin + "?session_hint=AUTHENTICATED";

  const oktaUrl =
    "YOUR_OKTA_LOGIN_URL?redirect_uri=" + encodeURIComponent(redirectBack);

  window.location.href = oktaUrl;
}

function loadIframe() {
  const container = document.getElementById("iframeContainer");

  // prevent duplicate iframe
  if (container.querySelector("iframe")) return;

  const iframe = document.createElement("iframe");

  iframe.src = "https://your-jenkins-url";
  iframe.width = "100%";
  iframe.height = "800px";
  iframe.style.border = "none";

  container.appendChild(iframe);
}

init();
</script>

</body>
</html>
