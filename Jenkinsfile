<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
  async function init() {
   
    const isAuthenticated = sessionStorage.getItem("isAuthenticated");

    if (isAuthenticated === "true") {
      loadIframe();
    } else {
      // Redirect to Okta login
      window.location.href = "YOUR_OKTA_LOGIN_URL";
    }
  }

  function loadIframe() {
    const iframe = document.createElement("iframe");
    iframe.src = "http://your-jenkins-url";
    iframe.width = "100%";
    iframe.height = "800px";
    iframe.style.border = "none";

    document.getElementById("iframeContainer").appendChild(iframe);
  }

  init();
</script>

</body>
</html>
