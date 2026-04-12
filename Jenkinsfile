<!DOCTYPE html>
<html>
<head>
    <title>My App</title>
    <style>
        body, html { margin: 0; padding: 0; height: 100%; width: 100%; }
        #iframeContainer, iframe { width: 100%; height: 100vh; border: none; display: block; }
    </style>
</head>
<body>

<div id="iframeContainer"></div>

<script>
(function() {
    // We check BOTH the query string (?) and the hash (#) 
    // because Okta often returns data in the hash.
    const currentUrl = window.location.href;
    console.log("Checking URL for auth:", currentUrl);

    if (currentUrl.includes("session_hint=AUTHENTICATED")) {
        console.log("✅ Welcome back! Loading the app...");
        loadIframe();
    } 
    else {
        console.log("❌ No session found. Sending to Okta...");
        
        // Ensure the redirect back includes the 'AUTHENTICATED' hint
        const callbackUrl = window.location.origin + window.location.pathname + "?session_hint=AUTHENTICATED";
        
        // YOUR_OKTA_URL must be the one that accepts a redirect_uri parameter
        const oktaUrl = "https://your-okta-login-url" + "?redirect_uri=" + encodeURIComponent(callbackUrl);

        window.location.href = oktaUrl;
    }

    function loadIframe() {
        const container = document.getElementById("iframeContainer");
        const iframe = document.createElement("iframe");
        
        // REMINDER: This site must allow iframing (No X-Frame-Options: Deny)
        iframe.src = "https://your-app-url.com"; 
        
        container.appendChild(iframe);
    }
})(); // This (function() { ... })(); pattern makes it run immediately on load
</script>

</body>
</html>
