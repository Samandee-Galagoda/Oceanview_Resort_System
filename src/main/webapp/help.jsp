<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%
    String user = (String) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
    boolean loggedIn = (user != null);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Oceanview Resort - Help</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Help & Guidelines<% if (loggedIn) { %> · <%= role %> · <%= user %><% } %></div>
    </div>
    <div class="top-actions">
        <% if (loggedIn) { %>
        <a class="link" href="<%=request.getContextPath()%>/menu.jsp">Main Menu</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
        <% } else { %>
        <a class="link" href="<%=request.getContextPath()%>/index.jsp">Login</a>
        <% } %>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">How to use the system</h2>
        <p class="muted" style="margin:6px 0 0 0;">Guidelines for reception staff.</p>

        <div id="status" class="status" style="display:none;"></div>
        <div id="list" style="margin-top:14px; line-height:1.7;"></div>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const listEl = document.getElementById('list');

    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    async function loadHelp() {
        const { data } = await Oceanview.api('/api/help');
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Unable to load help.', false);
            return;
        }
        const guidelines = (data.data && data.data.guidelines) ? data.data.guidelines : [];
        listEl.innerHTML = guidelines.map(g => `<div>• ${g}</div>`).join('');
        showStatus('Help loaded.', true);
    }

    loadHelp();
</script>
</body>
</html>
