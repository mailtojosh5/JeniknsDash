<!DOCTYPE html>
<html>
<head>
    <title>My App Portal</title>
</head>
<body>
    <div id="iframeContainer">
        <h2 id="status">Connecting to Authentication...</h2>
    </div>

    <script>
    (function() {
        const url = window.location.href;
        
        // Target App and Okta Config
        const GITHUB_URL = window.location.origin + window.location.pathname;
        const OKTA_BASE = "https://your-org.okta.com"; 
        const JENKINS_URL = "https://your-jenkins-url.com";

        // STEP 4: Detect if we have returned to GitHub from Okta
        if (url.includes("session_hint=AUTHENTICATED") || url.includes("check=true")) {
            document.getElementById("status").innerText = "Loading App...";
            loadIframe();
        } 
        // STEP 1: If we are just starting, go to Okta
        else {
            // We append the 'redirect_uri' to the Okta URL. 
            // This is the instruction that tells Okta to return to GitHub after login.
            const returnTo = encodeURIComponent(GITHUB_URL + "?session_hint=AUTHENTICATED");
            
            // Note: This URL format depends on your specific Okta App type
            const oktaUrl = `${OKTA_BASE}/home/bookmark/0oa.../123?redirect_uri=${returnTo}`;

            window.location.href = oktaUrl;
        }

        function loadIframe() {
            const container = document.getElementById("iframeContainer");
            container.innerHTML = `<iframe src="${JENKINS_URL}" style="position:fixed; top:0; left:0; width:100%; height:100%; border:none;"></iframe>`;
        }
    })();
    </script>
</body>
</html>
