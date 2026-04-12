<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function loadFrame() {
  const iframe = document.createElement("iframe");
  iframe.src = "https://your-jenkins-url";
  iframe.width = "100%";
  iframe.height = "800px";
  document.getElementById("iframeContainer").appendChild(iframe);
}

function init() {
  const url = window.location.href;

  // If returned from Okta
  if (url.includes("session_hint=AUTHENTICATED")) {
    loadFrame();
    return;
  }

  // Otherwise go to Okta
  window.location.href = "OKTA_LOGIN_URL";
}

init();
</script>

</body>
</html>
