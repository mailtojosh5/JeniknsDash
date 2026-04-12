function authCheck() {
  const url = window.location.href;

  if (url.includes("session_hint=AUTHENTICATED")) {
    localStorage.setItem("auth", "true");
    return true;
  }

  if (!localStorage.getItem("auth")) {
    window.location.href = "OKTA_LOGIN_URL";
    return false;
  }

  return true;
}


function loadIframe() {
  const iframe = document.createElement("iframe");
  iframe.src = "https://your-jenkins-url";
  iframe.width = "100%";
  iframe.height = "800px";

  document.getElementById("iframeContainer").appendChild(iframe);
}

function initApp() {
  loadIframe();
}

<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script src="auth.js"></script>
<script src="app.js"></script>

<script>
  if (authCheck()) {
    initApp();
  }
</script>

</body>
</html>
