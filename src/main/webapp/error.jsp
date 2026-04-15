<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head><title>Error | Car Puzzle</title>
<style>
  body{background:#0d0d0f;color:#f0f0f4;font-family:'Rajdhani',sans-serif;
       display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh;text-align:center;}
  h1{font-size:6rem;color:#e8001d;margin:0}
  p{font-size:1.3rem;color:#7a7d8a;margin:10px 0 30px}
  a{background:#e8001d;color:#fff;padding:12px 32px;border-radius:8px;text-decoration:none;font-size:1.1rem}
</style></head>
<body>
  <h1>💥</h1>
  <h2>Something went off the track!</h2>
  <p>Error <%= request.getAttribute("javax.servlet.error.status_code") %> — <%= request.getAttribute("javax.servlet.error.message") %></p>
  <a href="${pageContext.request.contextPath}/game">Back to the Race</a>
</body>
</html>
