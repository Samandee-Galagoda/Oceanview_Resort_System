-- Oceanview Resort System (MySQL) schema
-- Run this once (optional). The application also creates tables automatically on startup.

CREATE DATABASE IF NOT EXISTS oceanview_resort
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE oceanview_resort;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(50) NOT NULL,
  role VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS room_types (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type_name VARCHAR(50) UNIQUE NOT NULL,
  nightly_rate DECIMAL(10,2) NOT NULL,
  max_occupancy INT NOT NULL DEFAULT 2,
  description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS guests (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(120) NOT NULL,
  address VARCHAR(255) NOT NULL,
  contact_number VARCHAR(30) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reservations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  reservation_number VARCHAR(30) UNIQUE NOT NULL,
  guest_id BIGINT NOT NULL,
  room_type_id BIGINT NOT NULL,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_res_guest FOREIGN KEY (guest_id) REFERENCES guests(id),
  CONSTRAINT fk_res_roomtype FOREIGN KEY (room_type_id) REFERENCES room_types(id)
);

CREATE TABLE IF NOT EXISTS reservation_details (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  reservation_id BIGINT NOT NULL,
  adults INT NOT NULL DEFAULT 1,
  children INT NOT NULL DEFAULT 0,
  special_requests VARCHAR(255) NULL,
  notes VARCHAR(255) NULL,
  CONSTRAINT fk_detail_res FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS invoices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  invoice_number VARCHAR(30) UNIQUE NOT NULL,
  reservation_id BIGINT NOT NULL,
  issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  nights INT NOT NULL,
  nightly_rate DECIMAL(10,2) NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
  payment_method VARCHAR(30) NULL,
  CONSTRAINT fk_invoice_res FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
);

-- Seed room types (safe to re-run)
INSERT IGNORE INTO room_types (type_name, nightly_rate, max_occupancy, description) VALUES
('Standard', 120.00, 2, 'Comfortable standard room'),
('Deluxe',   180.00, 2, 'Deluxe room with enhanced amenities'),
('Suite',    250.00, 3, 'Suite with living area and sea view'),
('Family',   200.00, 4, 'Family room with extra space');

-- Seed users (admin + receptionists). Passwords are stored in plain text for demo/testing.
INSERT IGNORE INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('reception1', 'recep123', 'RECEPTIONIST'),
('reception2', 'recep123', 'RECEPTIONIST');
