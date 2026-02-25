<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String user = (String) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    if (role != null && "ADMIN".equalsIgnoreCase(role)) {
        response.sendRedirect(request.getContextPath() + "/admin-menu.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Oceanview Resort - Main Menu</title>
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
        <div class="subtitle">Main Menu · <%= role %> · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">Select an option</h2>
        <p class="muted" style="margin:6px 0 0 0;">Click a card to open the relevant function.</p>
    </div>

    <div style="height:14px;"></div>

    <div class="grid">
        <a class="action-card" href="<%=request.getContextPath()%>/reservation-add.jsp">
            <div class="label">Add New Reservation</div>
            <p class="desc">Register a guest and create a new booking with dates and room type.</p>
            <span class="badge accent">Create</span>
        </a>

        <a class="action-card" href="<%=request.getContextPath()%>/reservation-view.jsp">
            <div class="label">View Reservation Details</div>
            <p class="desc">Search by reservation number or guest name to retrieve full booking information.</p>
            <span class="badge">Search</span>
        </a>

        <a class="action-card" href="<%=request.getContextPath()%>/bill.jsp">
            <div class="label">Generate Bill</div>
            <p class="desc">Calculate total cost using room rate × number of nights.</p>
            <span class="badge">Billing</span>
        </a>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
</body>
</html>
