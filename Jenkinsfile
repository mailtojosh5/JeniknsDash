<!DOCTYPE html>
<html>
<body>

<div id="iframeContainer"></div>

<script>
function checkAuthLoop() {
  const url = window.location.href;

  console.log("Checking URL:", url);

  if (url.includes("session_hint=AUTHENTICATED")) {
    console.log("✅ Authenticated → loading iframe");
    loadIframe();
    return;
  }

  console.log("❌ Not authenticated yet... waiting");

  // Safe loop (checks every 1 second)
  setTimeout(checkAuthLoop, 1000);
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

// start loop
checkAuthLoop();
</script>

</body>
</html>
