<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
  async function init() {
    const params = new URLSearchParams(window.location.search);
    const sessionHint = params.get("session_hint");

    if (sessionHint === "AUTHENTICATED") {
      loadIframe();
    } else {
      // Redirect to Okta login and STOP execution
      window.location.href = "YOUR_OKTA_LOGIN_URL";
      return;
    }
  }

  function loadIframe() {
    const container = document.getElementById("iframeContainer");

    // Prevent duplicate iframes
    if (container.querySelector("iframe")) return;

    const iframe = document.createElement("iframe");
    iframe.src = "https://your-jenkins-url"; // use HTTPS
    iframe.width = "100%";
    iframe.height = "800px";
    iframe.style.border = "none";

    container.appendChild(iframe);
  }

  init();
</script>

</body>
</html>
