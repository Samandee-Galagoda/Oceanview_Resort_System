<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
<%
    String user = (String) session.getAttribute("user");
    String role = (String) session.getAttribute("role");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Oceanview Resort - Generate Bill</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Generate Bill · <%= role %> · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">Calculate & Print Bill</h2>
        <p class="muted" style="margin:6px 0 0 0;">Enter a reservation number to calculate the cost.</p>

        <div class="row" style="margin-top:14px;">
            <div>
                <label>Reservation Number</label>
                <input id="reservationNumber" type="text" placeholder="R-1001">
            </div>
            <div style="display:flex; align-items:flex-end; gap:10px; flex-wrap:wrap;">
                <button class="primary" onclick="generate()">Generate</button>
                <a class="link" style="color: var(--blue);" href="<%=request.getContextPath()%>/menu.jsp">← Back to Menu</a>
            </div>
        </div>

        <div id="status" class="status" style="display:none;"></div>
        <div id="result" class="card" style="margin-top:14px; display:none; background:#f9f9ff; border:1px dashed #c8d2ff;"></div>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const resultEl = document.getElementById('result');

    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    async function generate() {
        const reservationNumber = document.getElementById('reservationNumber').value.trim();
        resultEl.style.display = 'none';
        if (!reservationNumber) {
            showStatus('Reservation number is required.', false);
            return;
        }
        const { data } = await Oceanview.api(`/api/bill?reservationNumber=${encodeURIComponent(reservationNumber)}`);
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Failed to generate bill.', false);
            return;
        }
        showStatus('Bill generated successfully.', true);
        const b = data.data;
        resultEl.style.display = 'block';
        resultEl.innerHTML = `
          <div style="color: var(--navy); font-weight:700; font-size:16px;">Bill for ${b.reservationNumber}</div>
          <div style="margin-top:10px;">Room Type: <b>${b.roomType}</b></div>
          <div style="margin-top:6px;">Nights: <b>${b.nights}</b></div>
          <div style="margin-top:6px;">Nightly Rate: <b>${b.nightlyRate}</b></div>
          <div style="margin-top:10px; color: var(--navy);">Total Amount: <b>${b.totalAmount}</b></div>
        `;
        window.print();
    }
</script>
</body>
</html>
