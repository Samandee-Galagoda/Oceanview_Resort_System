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
    <title>Oceanview Resort - Manage Room Types</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Manage Room Types · Admin · <%= user %></div>
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
                <h2 style="margin:0; color: var(--navy);">Room Types</h2>
                <p class="muted" style="margin:6px 0 0 0;">Click a room card to open details (edit/delete inside).</p>
            </div>
            <div style="display:flex; gap:10px; align-items:center;">
                <a class="link" style="color: var(--blue);" href="<%=request.getContextPath()%>/admin-menu.jsp">← Admin Menu</a>
                <button class="primary" onclick="loadRoomTypes()">Refresh</button>
            </div>
        </div>

        <div id="status" class="status" style="display:none; margin-top:12px;"></div>
        <div id="cards" style="margin-top:14px;"></div>

        <div style="margin-top:14px; display:flex; justify-content:flex-end;">
            <button class="primary accent" onclick="openAddRoom()">Add Room</button>
        </div>
        <p class="muted" style="margin-top:10px;">Note: deleting a room type may fail if reservations already reference it.</p>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const cardsEl = document.getElementById('cards');
    let cachedRooms = [];

    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    const escapeHtml = (s) => String(s || '').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');

    function render(items) {
        cachedRooms = items || [];
        if (!items || items.length === 0) {
            cardsEl.innerHTML = '<div class="muted">No room types found.</div>';
            return;
        }
        const cards = items.map(t => `
          <div class="action-card" style="position:relative;" onclick="openRoom(${t.id})">
            <div class="label">${escapeHtml(t.typeName)}</div>
            <p class="desc">Rate per night: <b>${escapeHtml(t.nightlyRate)}</b></p>
            <p class="desc">Max occupancy: <b>${escapeHtml(t.maxOccupancy)}</b></p>
            <span class="badge">ID ${t.id}</span>
            <button class="btn" type="button"
                    style="position:absolute; right:12px; bottom:12px; background: rgba(246,128,72,0.18); color:#7a2400; border:1px solid #ffd0bf;"
                    onclick="event.stopPropagation(); deleteRoomType('${t.id}')">Delete</button>
          </div>
        `).join('');
        cardsEl.innerHTML = `<div class="grid">${cards}</div>`;
    }

    async function loadRoomTypes() {
        const { data } = await Oceanview.api('/api/room-types');
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Failed to load room types.', false);
            return;
        }
        showStatus(`Loaded ${data.data.length} room types.`, true);
        render(data.data);
    }

    window.openAddRoom = () => {
        const html = `
          <div class="row">
            <div>
              <label>Room Type Name</label>
              <input id="ar_name" placeholder="Premium">
            </div>
            <div>
              <label>Rate per Night</label>
              <input id="ar_rate" placeholder="150.00">
            </div>
          </div>
          <div style="margin-top:14px; display:flex; gap:10px; justify-content:flex-end;">
            <button class="primary" type="button" onclick="createRoom()">Save</button>
          </div>
        `;
        Oceanview.modal.open('Add Room Type', html);
    };

    window.createRoom = async () => {
        const typeName = (document.getElementById('ar_name') || {}).value?.trim() || '';
        const nightlyRate = (document.getElementById('ar_rate') || {}).value?.trim() || '';
        const { data } = await Oceanview.formPost('/api/room-types', { action:'create', typeName, nightlyRate, maxOccupancy:'2', description:'' });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Create failed.', false);
            return;
        }
        showStatus('Room type created.', true);
        Oceanview.modal.close();
        loadRoomTypes();
    };

    window.openRoom = (id) => {
        const t = cachedRooms.find(x => Number(x.id) === Number(id));
        if (!t) return;
        const html = `
          <div class="row">
            <div>
              <label>Room Type Name</label>
              <input id="er_name" value="${escapeHtml(t.typeName)}">
            </div>
            <div>
              <label>Rate per Night</label>
              <input id="er_rate" value="${escapeHtml(t.nightlyRate)}">
            </div>
          </div>
          <div class="muted" style="margin-top:10px;">Room Type ID: <b>${t.id}</b></div>
          <div style="margin-top:14px; display:flex; gap:10px; justify-content:flex-end;">
            <button class="primary" type="button" onclick="saveRoom(${t.id})">Edit & Save</button>
          </div>
        `;
        Oceanview.modal.open('Room Type Details', html);
    };

    window.saveRoom = async (id) => {
        const typeName = (document.getElementById('er_name') || {}).value?.trim() || '';
        const nightlyRate = (document.getElementById('er_rate') || {}).value?.trim() || '';
        const { data } = await Oceanview.formPost('/api/room-types', { action:'update', id, typeName, nightlyRate, maxOccupancy:'2', description:'' });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Update failed.', false);
            return;
        }
        showStatus('Room type updated.', true);
        Oceanview.modal.close();
        loadRoomTypes();
    };

    async function deleteRoomType(id) {
        if (!confirm('Delete this room type?')) return;
        const { data } = await Oceanview.formPost('/api/room-types', { action:'delete', id });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Delete failed.', false);
            return;
        }
        showStatus('Room successfully deleted.', true);
        loadRoomTypes();
    }

    loadRoomTypes();
</script>
</body>
</html>

