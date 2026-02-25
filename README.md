# Oceanview_Resort_System

## Overview
Oceanview Resort Reservation System is a Java web application that manages room bookings, guest details,
billing, and operational reports. It uses layered architecture with controllers, services, DAOs,
mappers, DTOs, and repositories.

## Quick Start
1. Update database connection in `src/main/resources/db.properties`.
2. Build and deploy the WAR to your servlet container (Tomcat 9+ recommended).
3. On first run, the system creates tables and seeds default data.

Default admin login:
- Username: `admin`
- Password: `admin123`

## API Endpoints (JSON)
- `POST /api/login`
- `POST /api/logout`
- `POST /api/reservations`
- `GET /api/reservations?reservationNumber=R-1001`
- `GET /api/bill?reservationNumber=R-1001`
- `GET /api/reports`
- `GET /api/help`