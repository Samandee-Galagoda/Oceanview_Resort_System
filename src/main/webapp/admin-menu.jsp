<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String user = (String) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    if (role == null || !"ADMIN".equalsIgnoreCase(role)) {
        response.sendRedirect(request.getContextPath() + "/menu.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Oceanview Resort - Admin Menu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<%
    String flash = (String) session.getAttribute("flashMessage");
    if (flash != null) {
        session.removeAttribute("flashMessage");
%>
    <div style="margin: 10px; padding: 8px 12px; border-radius: 6px; background: #e6ffed; color: #135200;">
        <%= flash %>
    </div>
<%
    }
%>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Admin Menu · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">Admin Controls</h2>
        <p class="muted" style="margin:6px 0 0 0;">Manage reservations, users, and room types.</p>
    </div>

    <div style="height:14px;"></div>

    <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:flex-end; gap:12px; flex-wrap:wrap;">
            <div>
                <h3 style="margin:0; color: var(--navy);">Weekly Reservations Report</h3>
                <p class="muted" style="margin:6px 0 0 0;">Most reserved room types in the last 7 days (auto-updates).</p>
            </div>
            <div style="display:flex; gap:10px; align-items:center;">
                <button class="primary" onclick="loadWeeklyRoomTypeGraph()">Refresh Graph</button>
            </div>
        </div>
        <div id="weeklyReportStatus" class="status" style="display:none; margin-top:12px;"></div>
        <div id="weeklyRoomTypeGraph" style="margin-top:14px;"></div>
    </div>

    <div style="height:14px;"></div>

    <div class="grid">
        <a class="action-card" href="<%=request.getContextPath()%>/admin-reservations.jsp">
            <div class="label">View Reservations</div>
            <p class="desc">See all reservations and search by reservation number or guest name.</p>
            <span class="badge">Overview</span>
        </a>

        <a class="action-card" href="<%=request.getContextPath()%>/admin-users.jsp">
            <div class="label">Manage Users</div>
            <p class="desc">Register receptionists, edit user details, and delete users.</p>
            <span class="badge accent">Users</span>
        </a>

        <a class="action-card" href="<%=request.getContextPath()%>/admin-room-types.jsp">
            <div class="label">Manage Room Types</div>
            <p class="desc">Add/edit room types, update prices, and remove unused types.</p>
            <span class="badge">Rooms</span>
        </a>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const weeklyStatusEl = document.getElementById('weeklyReportStatus');
    const weeklyGraphEl = document.getElementById('weeklyRoomTypeGraph');

    const showWeeklyStatus = (msg, ok) => {
        weeklyStatusEl.style.display = 'block';
        weeklyStatusEl.textContent = msg;
        weeklyStatusEl.classList.toggle('error', !ok);
    };

    function renderWeeklyBars(counts) {
        const entries = Object.entries(counts || {});
        if (entries.length === 0) {
            weeklyGraphEl.innerHTML = '<div class="muted">No reservations recorded in the last 7 days.</div>';
            return;
        }
        const max = Math.max(...entries.map(([, v]) => Number(v) || 0), 1);
        const esc = (s) => String(s || '').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');

        const rows = entries.map(([type, value]) => {
            const v = Number(value) || 0;
            const pct = Math.round((v / max) * 100);
            return '<div style="display:grid; grid-template-columns: 120px 1fr 48px; gap:10px; align-items:center; margin:8px 0;">' +
                '<div style="font-weight:700; color: var(--navy);">' + esc(type) + '</div>' +
                '<div style="height:12px; background:#eef1ff; border-radius:999px; overflow:hidden;">' +
                '<div style="height:12px; width:' + pct + '%; background: linear-gradient(90deg, #3b82f6, #6366f1);"></div>' +
                '</div>' +
                '<div style="text-align:right; font-weight:800;">' + v + '</div>' +
                '</div>';
        }).join('');

        weeklyGraphEl.innerHTML = '<div class="card" style="margin-top:10px; box-shadow:none; border:1px solid #e7e9fb;">' + rows + '</div>';
    }

    async function loadWeeklyRoomTypeGraph() {
        const { data } = await Oceanview.api('/api/reports?view=weeklyRoomTypes');
        if (!data || !data.success) {
            showWeeklyStatus((data && data.message) ? data.message : 'Failed to load weekly report.', false);
            weeklyGraphEl.innerHTML = '';
            return;
        }
        const counts = (data.data && data.data.counts) ? data.data.counts : {};
        const total = Object.values(counts).reduce((a, b) => a + (Number(b) || 0), 0);
        showWeeklyStatus('Weekly report loaded. Total reservations counted: ' + total + '.', true);
        renderWeeklyBars(counts);
    }

    // Auto-refresh so graph updates after new reservations are added
    loadWeeklyRoomTypeGraph();
    setInterval(loadWeeklyRoomTypeGraph, 15000);
    document.addEventListener('visibilitychange', () => {
        if (!document.hidden) loadWeeklyRoomTypeGraph();
    });
</script>
</body>
</html>

