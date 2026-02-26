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
    <title>Oceanview Resort - Add Reservation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/app.css">
</head>
<body>
<div class="topbar">
    <div class="brand">
        <div class="title">Oceanview Resort</div>
        <div class="subtitle">Add Reservation · <%= role %> · <%= user %></div>
    </div>
    <div class="top-actions">
        <a class="link" href="<%=request.getContextPath()%>/help.jsp">Help</a>
        <button class="btn" onclick="Oceanview.logout()">Logout</button>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2 style="margin:0; color: var(--navy);">Add New Reservation</h2>
        <p class="muted" style="margin:6px 0 0 0;">All fields are required. Dates must be valid (check-out after check-in).</p>

        <div class="row" style="margin-top:14px;">
            <div>
                <label>Reservation Number</label>
                <input id="reservationNumber" type="text" placeholder="R-1001">
            </div>
            <div>
                <label>Guest Name</label>
                <input id="guestName" type="text" placeholder="Full name">
            </div>
        </div>

        <div style="margin-top:12px;">
            <label>Address</label>
            <textarea id="address" rows="2" placeholder="Guest address"></textarea>
        </div>

        <div class="row" style="margin-top:12px;">
            <div>
                <label>Contact Number</label>
                <input id="contactNumber" type="text" placeholder="+94 77 123 4567">
            </div>
            <div>
                <label>Room Type</label>
                <select id="roomType">
                    <option value="Standard">Standard</option>
                    <option value="Deluxe">Deluxe</option>
                    <option value="Suite">Suite</option>
                    <option value="Family">Family</option>
                </select>
            </div>
        </div>

        <div class="row" style="margin-top:12px;">
            <div>
                <label>Guest Email</label>
                <input id="guestEmail" type="email" placeholder="guest@example.com">
            </div>
        </div>

        <div class="row" style="margin-top:12px;">
            <div>
                <label>Check-in Date</label>
                <input id="checkInDate" type="date">
            </div>
            <div>
                <label>Check-out Date</label>
                <input id="checkOutDate" type="date">
            </div>
        </div>

        <div style="margin-top:14px; display:flex; gap:10px; flex-wrap:wrap;">
            <button class="primary accent" onclick="saveReservation()">Save Reservation</button>
            <a class="link" style="color: var(--blue);" href="<%=request.getContextPath()%>/menu.jsp">← Back to Menu</a>
        </div>

        <div id="status" class="status" style="display:none;"></div>
    </div>
</div>

<div class="footer">Oceanview Resort System · Galle, Sri Lanka</div>

<script>window.__BASE__ = '<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/assets/app.js"></script>
<script>
    const statusEl = document.getElementById('status');
    const showStatus = (msg, ok) => {
        statusEl.style.display = 'block';
        statusEl.textContent = msg;
        statusEl.classList.toggle('error', !ok);
    };

    async function saveReservation() {
        const payload = {
            reservationNumber: document.getElementById('reservationNumber').value.trim(),
            guestName: document.getElementById('guestName').value.trim(),
            address: document.getElementById('address').value.trim(),
            contactNumber: document.getElementById('contactNumber').value.trim(),
            guestEmail: document.getElementById('guestEmail').value.trim(),
            roomType: document.getElementById('roomType').value,
            checkInDate: document.getElementById('checkInDate').value,
            checkOutDate: document.getElementById('checkOutDate').value
        };

        if (!payload.reservationNumber || !payload.guestName || !payload.address || !payload.contactNumber || !payload.guestEmail || !payload.checkInDate || !payload.checkOutDate) {
            showStatus('Please fill in all required fields.', false);
            return;
        }

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailPattern.test(payload.guestEmail)) {
            showStatus('Please enter a valid guest email address.', false);
            return;
        }

        const { data } = await Oceanview.formPost('/api/reservations', payload);
        if (data && data.success) {
            const message = `Reservation saved: ${data.data.reservationNumber} for ${data.data.guestName}. Email with reservation details has been sent to ${payload.guestEmail}.`;
            showStatus(message, true);
            alert(`Reservation details have been sent to ${payload.guestEmail}.`);
        } else {
            showStatus((data && data.message) ? data.message : 'Failed to save reservation.', false);
        }
    }
</script>
</body>
</html>
