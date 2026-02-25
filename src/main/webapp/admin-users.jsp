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
    <title>Oceanview Resort - Manage Users</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Manage Users · Admin · <%= user %></div>
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
                <h2 style="margin:0; color: var(--navy);">Users</h2>
                <p class="muted" style="margin:6px 0 0 0;">Only one ADMIN is allowed. Create receptionists here.</p>
            </div>
            <div style="display:flex; gap:10px; align-items:center;">
                <a class="link" style="color: var(--blue);" href="<%=request.getContextPath()%>/admin-menu.jsp">← Admin Menu</a>
                <button class="primary" onclick="loadUsers()">Refresh</button>
            </div>
        </div>

        <div id="status" class="status" style="display:none; margin-top:12px;"></div>

        <div class="card" style="margin-top:14px; background:#f9f9ff; border:1px dashed #c8d2ff;">
            <div style="color: var(--navy); font-weight:700;">Register New Receptionist</div>
            <div class="row" style="margin-top:10px;">
                <div>
                    <label>Username</label>
                    <input id="newUsername" placeholder="reception3">
                </div>
                <div>
                    <label>Password</label>
                    <input id="newPassword" placeholder="min 4 chars">
                </div>
            </div>
            <div style="margin-top:12px;">
                <button class="primary accent" onclick="createUser()">Create Receptionist</button>
            </div>
        </div>

        <div style="margin-top:14px; overflow:auto;" id="tableWrap"></div>
        <p class="muted" style="margin-top:10px;">Tip: Use Update to change password/role, Delete removes receptionists only.</p>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const tableWrap = document.getElementById('tableWrap');

    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    const escapeHtml = (s) => String(s || '').replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;');

    function render(users) {
        if (!users || users.length === 0) {
            tableWrap.innerHTML = '<div class="muted">No users found.</div>';
            return;
        }
        const rows = users.map(u => `
          <tr>
            <td style="padding:10px; border-bottom:1px solid #e7e9fb;">${escapeHtml(u.username)}</td>
            <td style="padding:10px; border-bottom:1px solid #e7e9fb;">
              <select data-username="${escapeHtml(u.username)}" class="roleSel">
                <option value="ADMIN" ${u.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                <option value="RECEPTIONIST" ${u.role === 'RECEPTIONIST' ? 'selected' : ''}>RECEPTIONIST</option>
              </select>
            </td>
            <td style="padding:10px; border-bottom:1px solid #e7e9fb;">
              <input data-username="${escapeHtml(u.username)}" class="pwdInp" value="${escapeHtml(u.password)}" />
            </td>
            <td style="padding:10px; border-bottom:1px solid #e7e9fb; white-space:nowrap;">
              <button class="primary" onclick="updateUser('${escapeHtml(u.username)}')">Update</button>
              <button class="primary accent" onclick="deleteUser('${escapeHtml(u.username)}')">Delete</button>
            </td>
          </tr>
        `).join('');

        tableWrap.innerHTML = `
          <table style="width:100%; border-collapse:collapse; font-size:13px;">
            <thead>
              <tr style="text-align:left; color: var(--navy);">
                <th style="padding:10px; border-bottom:1px solid #e7e9fb;">Username</th>
                <th style="padding:10px; border-bottom:1px solid #e7e9fb;">Role</th>
                <th style="padding:10px; border-bottom:1px solid #e7e9fb;">Password (demo)</th>
                <th style="padding:10px; border-bottom:1px solid #e7e9fb;">Actions</th>
              </tr>
            </thead>
            <tbody>${rows}</tbody>
          </table>
        `;
    }

    async function loadUsers() {
        const { data } = await Oceanview.api('/api/users');
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Failed to load users.', false);
            return;
        }
        showStatus(`Loaded ${data.data.length} users.`, true);
        render(data.data);
    }

    async function createUser() {
        const username = document.getElementById('newUsername').value.trim();
        const password = document.getElementById('newPassword').value.trim();
        const { data } = await Oceanview.formPost('/api/users', { action:'create', username, password });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Create failed.', false);
            return;
        }
        showStatus('User created.', true);
        document.getElementById('newUsername').value = '';
        document.getElementById('newPassword').value = '';
        loadUsers();
    }

    async function updateUser(username) {
        const roleSel = document.querySelector(`.roleSel[data-username="${CSS.escape(username)}"]`);
        const pwdInp = document.querySelector(`.pwdInp[data-username="${CSS.escape(username)}"]`);
        const role = roleSel ? roleSel.value : '';
        const password = pwdInp ? pwdInp.value : '';
        const { data } = await Oceanview.formPost('/api/users', { action:'update', username, role, password });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Update failed.', false);
            return;
        }
        showStatus('User updated.', true);
        loadUsers();
    }

    async function deleteUser(username) {
        if (!confirm(`Delete user ${username}?`)) return;
        const { data } = await Oceanview.formPost('/api/users', { action:'delete', username });
        if (!data || !data.success) {
            showStatus((data && data.message) ? data.message : 'Delete failed.', false);
            return;
        }
        showStatus('User deleted.', true);
        loadUsers();
    }

    loadUsers();
</script>
</body>
</html>

