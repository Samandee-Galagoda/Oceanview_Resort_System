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
    <title>Oceanview Resort - View Reservation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">View Reservation · <%= role %> · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">Reservation Details</h2>
        <p class="muted" style="margin:6px 0 0 0;">Search by reservation number or guest name.</p>

        <div class="row" style="margin-top:14px;">
            <div>
                <label>Reservation Number</label>
                <input id="reservationNumber" type="text" placeholder="R-1001">
            </div>
            <div>
                <label>Guest Name</label>
                <input id="guestName" type="text" placeholder="e.g. John">
            </div>
            <div style="display:flex; align-items:flex-end; gap:10px; flex-wrap:wrap;">
                <button class="primary" onclick="search()">Search</button>
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

    const esc = (s) => String(s || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    const escJs = (s) => String(s || '').replace(/\\/g, '\\\\').replace(/'/g, "\\'").replace(/"/g, '\\"');

    function renderOne(r) {
        const resNum = escJs(r.reservationNumber);
        return '<div style="padding:12px 0; border-bottom:1px solid #e7e9fb;">' +
            '<div style="display:flex; justify-content:space-between; align-items:flex-start; gap:12px;">' +
            '<div style="flex:1;">' +
            '<div style="color: var(--navy); font-weight:700; font-size:16px;">' + esc(r.reservationNumber) + '</div>' +
            '<div class="muted" style="margin-top:6px;">Guest</div>' +
            '<div>' + esc(r.guestName) + ' · ' + esc(r.contactNumber) + '</div>' +
            '<div class="muted" style="margin-top:10px;">Address</div>' +
            '<div>' + esc(r.address) + '</div>' +
            '<div class="muted" style="margin-top:10px;">Booking</div>' +
            '<div>' + esc(r.roomType) + ' · ' + esc(r.checkInDate) + ' → ' + esc(r.checkOutDate) + '</div>' +
            '</div>' +
            '<div>' +
            '<button class="btn" type="button" style="background: rgba(246,128,72,0.18); color:#7a2400; border:1px solid #ffd0bf; white-space:nowrap;" onclick="cancelReservation(\'' + resNum + '\')">Cancel</button>' +
            '</div>' +
            '</div>' +
            '</div>';
    }

    async function search() {
        const reservationNumber = document.getElementById('reservationNumber').value.trim();
        const guestName = document.getElementById('guestName').value.trim();
        resultEl.style.display = 'none';
        if (!reservationNumber && !guestName) {
            showStatus('Enter a reservation number or guest name to search.', false);
            return;
        }
        let url;
        if (reservationNumber) {
            url = '/api/reservations?reservationNumber=' + encodeURIComponent(reservationNumber);
        } else {
            url = '/api/reservations?guestName=' + encodeURIComponent(guestName);
        }
        const { data } = await Oceanview.api(url);
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'No reservations found.', false);
            return;
        }
        const list = Array.isArray(data.data) ? data.data : [data.data];
        showStatus(list.length === 1 ? 'Reservation found.' : (list.length + ' reservation(s) found.'), true);
        resultEl.style.display = 'block';
        resultEl.innerHTML = list.map(function(r) { return renderOne(r); }).join('');
    }

    async function cancelReservation(reservationNumber) {
        if (!confirm('Cancel reservation ' + reservationNumber + '? This action cannot be undone.')) {
            return;
        }
        try {
            const { data } = await Oceanview.formPost('/api/reservations', {
                action: 'cancel',
                reservationNumber: reservationNumber
            });
            if (!data || !data.success) {
                showStatus((data && data.message) ? data.message : 'Failed to cancel reservation.', false);
                return;
            }
            showStatus('Reservation ' + reservationNumber + ' cancelled successfully.', true);
            // Refresh the search results
            const reservationNumberEl = document.getElementById('reservationNumber');
            const guestNameEl = document.getElementById('guestName');
            if (reservationNumberEl.value.trim()) {
                // If searching by number, clear and re-search
                await search();
            } else if (guestNameEl.value.trim()) {
                // If searching by name, re-search to refresh the list
                await search();
            }
        } catch (ex) {
            showStatus('Failed to cancel reservation: ' + (ex.message || 'Unknown error'), false);
        }
    }
</script>
</body>
</html>
