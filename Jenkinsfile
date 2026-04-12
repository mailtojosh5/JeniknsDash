<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function init() {
  const url = window.location.href;

  console.log("Page loaded:", url);

  // STEP 1: If returned from Okta
  if (url.includes("session_hint=AUTHENTICATED")) {

    console.log("✅ Authenticated detected");

    // store state so future reloads know user is logged in
    sessionStorage.setItem("isLoggedIn", "true");

    loadIframe();
    return;
  }

  // STEP 2: If already logged in (from storage)
  if (sessionStorage.getItem("isLoggedIn") === "true") {
    console.log("✅ Already logged in (from storage)");
    loadIframe();
    return;
  }

  // STEP 3: Not logged in → redirect to Okta
  console.log("❌ Not logged in → redirecting to Okta");

  sessionStorage.setItem("pendingLogin", "true");

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

init();
</script>

</body>
</html>
