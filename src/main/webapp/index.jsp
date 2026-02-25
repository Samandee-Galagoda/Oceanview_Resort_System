<%@ page isELIgnored="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Oceanview Resort Reservation System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        :root {
            --navy: #0D1A63;
            --blue: #1A2CA3;
            --royal: #2845D6;
            --accent: #F68048;
            --bg: #f6f7fb;
            --text: #1a1a1a;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: "Segoe UI", Arial, sans-serif;
            background: var(--bg);
            color: var(--text);
        }

        header {
            background: linear-gradient(120deg, var(--navy), var(--royal));
            color: #fff;
            padding: 28px 24px;
        }

        header h1 {
            margin: 0 0 6px 0;
            font-size: 24px;
        }

        header p {
            margin: 0;
            opacity: 0.9;
        }

        main {
            max-width: 1200px;
            margin: 24px auto 48px;
            padding: 0 16px;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 18px;
        }

        .card {
            background: #fff;
            border-radius: 12px;
            padding: 18px;
            box-shadow: 0 10px 24px rgba(13, 26, 99, 0.12);
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .card h2 {
            margin: 0;
            font-size: 18px;
            color: var(--navy);
        }

        label {
            font-size: 13px;
            color: #3d3d3d;
            display: block;
            margin-bottom: 6px;
        }

        input, select, textarea {
            width: 100%;
            padding: 10px 12px;
            border-radius: 8px;
            border: 1px solid #d9dbea;
            font-size: 14px;
        }

        button {
            border: none;
            padding: 10px 14px;
            border-radius: 8px;
            font-size: 14px;
            cursor: pointer;
            background: var(--blue);
            color: #fff;
            transition: 0.2s ease;
        }

        button:hover {
            background: var(--royal);
        }

        .accent {
            background: var(--accent);
        }

        .accent:hover {
            background: #f56a2c;
        }

        .row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
            gap: 10px;
        }

        .status {
            padding: 10px 12px;
            border-radius: 8px;
            font-size: 13px;
            background: #eef2ff;
            color: var(--navy);
        }

        .hidden {
            display: none;
        }

        .result {
            background: #f9f9ff;
            border: 1px dashed #c8d2ff;
            padding: 10px;
            border-radius: 8px;
            font-size: 13px;
        }

        footer {
            text-align: center;
            padding: 18px;
            color: #6b6b6b;
            font-size: 12px;
        }
    </style>
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
<header>
    <h1>Oceanview Resort Reservation System</h1>
    <p>Secure, fast, and organized room booking in one place.</p>
</header>

<main>
    <section class="card" id="loginCard">
        <h2>Staff Login</h2>
        <%
            String err = request.getParameter("error");
        %>
        <div class="status" id="loginStatus"><%= (err != null && !err.trim().isEmpty()) ? err : "Please login to access reservations." %></div>

        <form id="loginForm" method="post" action="<%=request.getContextPath()%>/api/login">
            <input type="hidden" name="mode" value="html">
            <div>
                <label for="username">Username</label>
                <input id="username" name="username" type="text" placeholder="admin">
            </div>
            <div>
                <label for="password">Password</label>
                <input id="password" name="password" type="password" placeholder="Enter password">
            </div>
            <button id="loginBtn" type="submit">Login</button>
        </form>
    </section>

    <section class="card hidden" id="reservationCard">
        <h2>Add New Reservation</h2>
        <div class="row">
            <div>
                <label>Reservation Number</label>
                <input id="resNumber" type="text" placeholder="R-1001">
            </div>
            <div>
                <label>Guest Name</label>
                <input id="guestName" type="text" placeholder="Full name">
            </div>
        </div>
        <div>
            <label>Address</label>
            <textarea id="address" rows="2" placeholder="Guest address"></textarea>
        </div>
        <div class="row">
            <div>
                <label>Contact Number</label>
                <input id="contactNumber" type="text" placeholder="+94 77 123 4567">
            </div>
            <div>
                <label>Room Type</label>
                <select id="roomType">
                    <option>Standard</option>
                    <option>Deluxe</option>
                    <option>Suite</option>
                    <option>Family</option>
                </select>
            </div>
        </div>
        <div class="row">
            <div>
                <label>Check-in Date</label>
                <input id="checkIn" type="date">
            </div>
            <div>
                <label>Check-out Date</label>
                <input id="checkOut" type="date">
            </div>
        </div>
        <button class="accent" onclick="addReservation()">Save Reservation</button>
        <div class="result" id="reservationResult">No reservation created yet.</div>
    </section>

    <section class="card hidden" id="searchCard">
        <h2>Display Reservation Details</h2>
        <div>
            <label>Reservation Number</label>
            <input id="searchNumber" type="text" placeholder="R-1001">
        </div>
        <button onclick="fetchReservation()">Search</button>
        <div class="result" id="searchResult">Reservation details will appear here.</div>
    </section>

    <section class="card hidden" id="billingCard">
        <h2>Calculate & Print Bill</h2>
        <div>
            <label>Reservation Number</label>
            <input id="billNumber" type="text" placeholder="R-1001">
        </div>
        <button onclick="generateBill()">Generate Bill</button>
        <div class="result" id="billResult">Billing summary will appear here.</div>
    </section>

    <section class="card hidden" id="reportCard">
        <h2>Operational Reports</h2>
        <button onclick="loadReport()">Refresh Summary</button>
        <div class="result" id="reportResult">Report data will appear here.</div>
    </section>

    <section class="card hidden" id="helpCard">
        <h2>Help & Guidelines</h2>
        <button onclick="loadHelp()">Load Help</button>
        <div class="result" id="helpResult">Guidelines will appear here.</div>
    </section>

    <section class="card hidden" id="exitCard">
        <h2>Exit System</h2>
        <p class="status">Click below to safely log out of the system.</p>
        <button onclick="logout()">Logout</button>
    </section>
</main>

<footer>
    Oceanview Resort System · Secure Reservations · Galle, Sri Lanka
</footer>

<script>
    const BASE = '<%=request.getContextPath()%>';

    const api = async (path, options = {}) => {
        const url = (path.startsWith('/')) ? (BASE + path) : (BASE + '/' + path);
        const res = await fetch(url, { credentials: 'same-origin', ...options });
        const contentType = (res.headers.get('content-type') || '').toLowerCase();
        let data;
        try {
            if (contentType.includes('application/json')) {
                data = await res.json();
            } else {
                const text = await res.text();
                data = { success: false, message: text ? text.substring(0, 200) : ('HTTP ' + res.status) };
            }
        } catch (e) {
            data = { success: false, message: 'Unexpected server response. Please check Tomcat logs.' };
        }
        return { status: res.status, data };
    };

    const formPost = (path, payload) => {
        const body = new URLSearchParams();
        Object.keys(payload).forEach(k => body.append(k, payload[k] == null ? '' : payload[k]));
        return api(path, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' },
            body: body.toString()
        });
    };

    const show = (id, show) => {
        const element = document.getElementById(id);
        if (show) {
            element.classList.remove('hidden');
        } else {
            element.classList.add('hidden');
        }
    };

    const updateStatus = (message, success = true) => {
        const status = document.getElementById('loginStatus');
        status.textContent = message;
        status.style.background = success ? '#eef2ff' : '#ffe9e1';
        status.style.color = success ? '#0D1A63' : '#b21f2d';
    };

    function toggleApp(loggedIn) {
        show('reservationCard', loggedIn);
        show('searchCard', loggedIn);
        show('billingCard', loggedIn);
        show('reportCard', loggedIn);
        show('helpCard', loggedIn);
        show('exitCard', loggedIn);
    }

    async function doLogin() {
        updateStatus('Attempting login...', true);
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        if (!username || !password) {
            updateStatus('Username and password are required.', false);
            return;
        }
        try {
            const { data } = await formPost('/api/login', { username, password });
            if (data && data.success) {
                updateStatus(data.message || `Welcome ${data.data.username}.`, true);
                const role = data.data && data.data.role ? String(data.data.role) : '';
                window.location.href = BASE + (role.toUpperCase() === 'ADMIN' ? '/admin-menu.jsp' : '/menu.jsp');
            } else {
                updateStatus((data && data.message) ? data.message : 'Login failed.', false);
            }
        } catch (e) {
            updateStatus('Network error. Is Tomcat running and the app deployed correctly?', false);
        }
    }

    // Extra safety: bind click handler via JS too (helps if inline handlers get blocked)
    document.addEventListener('DOMContentLoaded', () => {
        const btn = document.getElementById('loginBtn');
        // Intercept form submit for AJAX login; if JS fails, form posts normally (fallback).
        const form = document.getElementById('loginForm');
        if (form) {
            form.addEventListener('submit', (e) => {
                e.preventDefault();
                doLogin();
            });
        }
        if (btn) btn.addEventListener('click', () => {});

        const password = document.getElementById('password');
        if (password) password.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') doLogin();
        });
    });

    async function addReservation() {
        const payload = {
            reservationNumber: document.getElementById('resNumber').value.trim(),
            guestName: document.getElementById('guestName').value.trim(),
            address: document.getElementById('address').value.trim(),
            contactNumber: document.getElementById('contactNumber').value.trim(),
            roomType: document.getElementById('roomType').value,
            checkInDate: document.getElementById('checkIn').value,
            checkOutDate: document.getElementById('checkOut').value
        };
        const { data } = await formPost('/api/reservations', payload);
        const result = document.getElementById('reservationResult');
        result.textContent = data.success
            ? `Reservation saved for ${data.data.guestName} (${data.data.reservationNumber}).`
            : data.message;
    }

    async function fetchReservation() {
        const reservationNumber = document.getElementById('searchNumber').value.trim();
        const { data } = await api(`/api/reservations?reservationNumber=${encodeURIComponent(reservationNumber)}`);
        const result = document.getElementById('searchResult');
        if (!data.success) {
            result.textContent = data.message;
            return;
        }
        const res = data.data;
        result.textContent = `${res.guestName} | ${res.roomType} | ${res.checkInDate} to ${res.checkOutDate} | Contact: ${res.contactNumber}`;
    }

    async function generateBill() {
        const reservationNumber = document.getElementById('billNumber').value.trim();
        const { data } = await api(`/api/bill?reservationNumber=${encodeURIComponent(reservationNumber)}`);
        const result = document.getElementById('billResult');
        if (!data.success) {
            result.textContent = data.message;
            return;
        }
        const bill = data.data;
        result.textContent = `Room ${bill.roomType} | Nights: ${bill.nights} | Rate: ${bill.nightlyRate} | Total: ${bill.totalAmount}`;
    }

    async function loadReport() {
        const { data } = await api('/api/reports');
        const result = document.getElementById('reportResult');
        if (!data.success) {
            result.textContent = data.message;
            return;
        }
        const report = data.data;
        result.textContent = `Total: ${report.totalReservations}, Active: ${report.activeReservations}, Checkouts (7 days): ${report.checkoutsNextSevenDays}, Revenue: ${report.estimatedRevenue}, Top Room: ${report.mostPopularRoomType}`;
    }

    async function loadHelp() {
        const { data } = await api('/api/help');
        const result = document.getElementById('helpResult');
        if (!data.success) {
            result.textContent = data.message;
            return;
        }
        result.textContent = data.data.guidelines.join(' ');
    }

    async function logout() {
        await formPost('/api/logout', {});
        toggleApp(false);
        updateStatus('Logged out successfully.', true);
    }

    toggleApp(false);
</script>
</body>
</html>
