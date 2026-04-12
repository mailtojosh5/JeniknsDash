<!DOCTYPE html>
<html>
<head>
  <title>SSO Loader</title>
</head>

<body>

<div id="app">Loading...</div>

<script>
  const OKTA_LOGIN_URL = "https://YOUR_OKTA_DOMAIN/login";
  const JENKINS_URL = "http://YOUR_JENKINS_URL";

  function loadJenkins() {
    document.getElementById("app").innerHTML = `
      <iframe src="${JENKINS_URL}"
        style="width:100%; height:100vh; border:none;">
      </iframe>
    `;
  }

  function redirectToOkta() {
    window.location.href = OKTA_LOGIN_URL;
  }

  function checkCallback() {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code"); // Okta returns this after login

    if (code) {
      // In real setup, you would exchange this code for tokens via backend
      loadJenkins();
      return;
    }

    // If no login code → send user to Okta
    redirectToOkta();
  }

  checkCallback();
</script>

</body>
</html>
