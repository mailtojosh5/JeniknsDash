<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function init() {
  const url = window.location.href;

  console.log("Loaded URL:", url);

  // STEP 1: After Okta redirects back here
  if (url.includes("AUTHENTICATED")) {
    console.log("✅ Authenticated → loading iframe");
    loadIframe();
    return;
  }

  // STEP 2: Not authenticated → redirect to Okta
  console.log("❌ Not authenticated → going to Okta");

  const redirectBack =
    window.location.origin + "?session_hint=AUTHENTICATED";

  const oktaUrl =
    "OKTA_LOGIN_URL?redirect_uri=" + encodeURIComponent(redirectBack);

  window.location.href = oktaUrl;
}

function loadIframe() {
  const iframe = document.createElement("iframe");
  iframe.src = "https://your-jenkins-url";
  iframe.width = "100%";
  iframe.height = "800px";

  document.getElementById("iframeContainer").appendChild(iframe);
}

init();
</script>

</body>
</html>
