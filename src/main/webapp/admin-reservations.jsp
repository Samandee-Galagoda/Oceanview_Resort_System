<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="true" %>
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
    <title>Oceanview Resort - Reservations (Admin)</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Reservations · Admin · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <div style="display:flex; justify-content:space-between; align-items:flex-end; gap:12px; flex-wrap:wrap;">
            <div>
                <h2 style="margin:0; color: var(--navy);">All Reservations</h2>
                <p class="muted" style="margin:6px 0 0 0;">Search by reservation number or guest name. Click a ticket to open full details.</p>
            </div>
            <div style="display:flex; gap:10px; align-items:center; flex-wrap:wrap;">
                <a class="link" style="color: var(--blue);" href="<%=request.getContextPath()%>/admin-menu.jsp">← Admin Menu</a>
                <button class="primary" onclick="loadAll()">Refresh</button>
            </div>
        </div>

        <div class="row" style="margin-top:14px;">
            <div>
                <label>Reservation Number</label>
                <input id="searchNumber" type="text" placeholder="R-1001">
            </div>
            <div>
                <label>Guest Name</label>
                <input id="searchGuestName" type="text" placeholder="e.g. John">
            </div>
            <div style="display:flex; align-items:flex-end; gap:10px; flex-wrap:wrap;">
                <button class="primary" onclick="search()">Search</button>
                <button class="primary accent" onclick="clearSearch()">Clear</button>
            </div>
        </div>

        <div id="status" class="status" style="display:none;"></div>
        <div id="tickets" style="margin-top:14px;"></div>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const ticketsEl = document.getElementById('tickets');

    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    const renderTickets = (items) => {
        if (!items || items.length === 0) {
            ticketsEl.innerHTML = '<div class="muted">No reservations found.</div>';
            return;
        }
        const esc = (s) => String(s || '').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
        const cards = items.map(r => {
            const payload = encodeURIComponent(JSON.stringify(r));
            return `
              <div class="ticket" data-json="${payload}" onclick="openTicketFromEl(this)">
                <div class="top">
                  <div class="code">${esc(r.reservationNumber)}</div>
                  <div class="tag">${esc(r.roomType)}</div>
                </div>
                <div class="line"></div>
                <div class="info">
                  <div><span class="muted">Guest:</span> ${esc(r.guestName)}</div>
                  <div><span class="muted">Dates:</span> ${esc(r.checkInDate)} → ${esc(r.checkOutDate)}</div>
                  <div><span class="muted">Contact:</span> ${esc(r.contactNumber)}</div>
                </div>
              </div>
            `;
        }).join('');
        ticketsEl.innerHTML = `<div class="ticket-grid">${cards}</div>`;
    };

    window.openTicketFromEl = (el) => {
        let r;
        try {
            const raw = el.getAttribute('data-json') || '';
            r = JSON.parse(decodeURIComponent(raw));
        } catch (e) { return; }
        const esc = (s) => String(s || '').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');
        const html = `
          <div style="display:grid; gap:12px;">
            <div class="card" style="box-shadow:none; border:1px solid #e7e9fb;">
              <div style="display:flex; justify-content:space-between; gap:10px; flex-wrap:wrap;">
                <div>
                  <div class="muted">Reservation Number</div>
                  <div style="font-weight:800; color:var(--navy); font-size:18px;">${esc(r.reservationNumber)}</div>
                </div>
                <div>
                  <div class="muted">Room Type</div>
                  <div style="font-weight:800;">${esc(r.roomType)}</div>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="card" style="box-shadow:none; border:1px solid #e7e9fb;">
                <div class="muted">Guest</div>
                <div style="font-weight:700;">${esc(r.guestName)}</div>
                <div class="muted" style="margin-top:8px;">Contact</div>
                <div>${esc(r.contactNumber)}</div>
              </div>
              <div class="card" style="box-shadow:none; border:1px solid #e7e9fb;">
                <div class="muted">Stay Dates</div>
                <div><b>${esc(r.checkInDate)}</b> → <b>${esc(r.checkOutDate)}</b></div>
                <div class="muted" style="margin-top:8px;">Created</div>
                <div>${esc(r.createdAt)}</div>
              </div>
            </div>
            <div class="card" style="box-shadow:none; border:1px solid #e7e9fb;">
              <div class="muted">Address</div>
              <div>${esc(r.address)}</div>
            </div>
          </div>
        `;
        Oceanview.modal.open('Reservation Ticket', html);
    };

    async function loadAll() {
        const { data } = await Oceanview.api('/api/reservations');
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Failed to load reservations.', false);
            return;
        }
        showStatus(`Loaded ${data.data.length} reservations.`, true);
        renderTickets(data.data);
    }

    async function search() {
        const number = document.getElementById('searchNumber').value.trim();
        const guestName = document.getElementById('searchGuestName').value.trim();
        if (!number && !guestName) {
            showStatus('Enter a reservation number or guest name to search.', false);
            return;
        }
        let url;
        if (number) {
            url = '/api/reservations?reservationNumber=' + encodeURIComponent(number);
        } else {
            url = '/api/reservations?guestName=' + encodeURIComponent(guestName);
        }
        const { data } = await Oceanview.api(url);
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'No reservations found.', false);
            renderTickets([]);
            return;
        }
        const list = Array.isArray(data.data) ? data.data : [data.data];
        showStatus(list.length === 1 ? 'Reservation found.' : (list.length + ' reservation(s) found.'), true);
        renderTickets(list);
    }

    function clearSearch() {
        document.getElementById('searchNumber').value = '';
        document.getElementById('searchGuestName').value = '';
        loadAll();
    }

    loadAll();
</script>
</body>
</html>

