package com.oceanview.resort.config;

import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    public void initialize() {
        createTables();
        ensureUsersPasswordColumn();
        ensureUsersFullNameColumn();
        seedRoomTypes();
        seedDefaultUsers();
    }

    private void createTables() {
        // NOTE: We use plural table names because `user` can be a reserved keyword in MySQL.
        String usersSql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "full_name VARCHAR(120) NULL," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(50) NOT NULL," +
                "role VARCHAR(20) NOT NULL," +
                "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String roomTypesSql = "CREATE TABLE IF NOT EXISTS room_types (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "type_name VARCHAR(50) UNIQUE NOT NULL," +
                "nightly_rate DECIMAL(10,2) NOT NULL," +
                "max_occupancy INT NOT NULL DEFAULT 2," +
                "description VARCHAR(255) NULL" +
                ")";

        String guestsSql = "CREATE TABLE IF NOT EXISTS guests (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "full_name VARCHAR(120) NOT NULL," +
                "address VARCHAR(255) NOT NULL," +
                "contact_number VARCHAR(30) NOT NULL," +
                "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String reservationsSql = "CREATE TABLE IF NOT EXISTS reservations (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "reservation_number VARCHAR(30) UNIQUE NOT NULL," +
                "guest_id BIGINT NOT NULL," +
                "room_type_id BIGINT NOT NULL," +
                "check_in_date DATE NOT NULL," +
                "check_out_date DATE NOT NULL," +
                "status VARCHAR(20) NOT NULL DEFAULT 'BOOKED'," +
                "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (guest_id) REFERENCES guests(id)," +
                "FOREIGN KEY (room_type_id) REFERENCES room_types(id)" +
                ")";

        String reservationDetailsSql = "CREATE TABLE IF NOT EXISTS reservation_details (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "reservation_id BIGINT NOT NULL," +
                "adults INT NOT NULL DEFAULT 1," +
                "children INT NOT NULL DEFAULT 0," +
                "special_requests VARCHAR(255) NULL," +
                "notes VARCHAR(255) NULL," +
                "FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE" +
                ")";

        String invoicesSql = "CREATE TABLE IF NOT EXISTS invoices (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "invoice_number VARCHAR(30) UNIQUE NOT NULL," +
                "reservation_id BIGINT NOT NULL," +
                "issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "nights INT NOT NULL," +
                "nightly_rate DECIMAL(10,2) NOT NULL," +
                "total_amount DECIMAL(10,2) NOT NULL," +
                "payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID'," +
                "payment_method VARCHAR(30) NULL," +
                "FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE" +
                ")";

        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(usersSql);
            stmt.executeUpdate(roomTypesSql);
            stmt.executeUpdate(guestsSql);
            stmt.executeUpdate(reservationsSql);
            stmt.executeUpdate(reservationDetailsSql);
            stmt.executeUpdate(invoicesSql);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create tables", ex);
        }
    }

    private void ensureUsersPasswordColumn() {
        // If the DB was created using the older schema (password_hash), add the `password` column for demo login.
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("ALTER TABLE users ADD COLUMN password VARCHAR(50) NULL");
        } catch (Exception ignored) {
            // Column already exists or users table not present yet - ignore.
        }
    }

    private void ensureUsersFullNameColumn() {
        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("ALTER TABLE users ADD COLUMN full_name VARCHAR(120) NULL");
        } catch (Exception ignored) {
            // ignore
        }
    }

    private void seedRoomTypes() {
        String sql = "INSERT IGNORE INTO room_types (type_name, nightly_rate, max_occupancy, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            insertRoomType(stmt, "Standard", 120.00, 2, "Comfortable standard room");
            insertRoomType(stmt, "Deluxe", 180.00, 2, "Deluxe room with enhanced amenities");
            insertRoomType(stmt, "Suite", 250.00, 3, "Suite with living area and sea view");
            insertRoomType(stmt, "Family", 200.00, 4, "Family room with extra space");
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to seed room types", ex);
        }
    }

    private void insertRoomType(PreparedStatement stmt, String roomType, double rate, int maxOccupancy, String description) throws Exception {
        stmt.setString(1, roomType);
        stmt.setDouble(2, rate);
        stmt.setInt(3, maxOccupancy);
        stmt.setString(4, description);
        stmt.executeUpdate();
    }

    private void seedDefaultUsers() {
        // Enforce: only ONE admin account should exist.
        String adminCountSql = "SELECT COUNT(*) FROM users WHERE role = 'ADMIN'";
        String insertSql = "INSERT IGNORE INTO users (full_name, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             Statement countStmt = connection.createStatement();
             ResultSet rs = countStmt.executeQuery(adminCountSql)) {
            int adminCount = rs.next() ? rs.getInt(1) : 0;
            if (adminCount == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setString(1, "System Administrator");
                    insertStmt.setString(2, "admin");
                    insertStmt.setString(3, "admin123");
                    insertStmt.setString(4, "ADMIN");
                    insertStmt.executeUpdate();
                }
            }

            // Seed receptionist test users (can be more than one)
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, "Receptionist One");
                insertStmt.setString(2, "reception1");
                insertStmt.setString(3, "recep123");
                insertStmt.setString(4, "RECEPTIONIST");
                insertStmt.executeUpdate();

                insertStmt.setString(1, "Receptionist Two");
                insertStmt.setString(2, "reception2");
                insertStmt.setString(3, "recep123");
                insertStmt.setString(4, "RECEPTIONIST");
                insertStmt.executeUpdate();
            }

            // If users were inserted previously using hashed passwords, set the demo passwords so you can see them.
            try (PreparedStatement update = connection.prepareStatement("UPDATE users SET password = ? WHERE username = ? AND (password IS NULL OR password = '')")) {
                update.setString(1, "admin123");
                update.setString(2, "admin");
                update.executeUpdate();

                update.setString(1, "recep123");
                update.setString(2, "reception1");
                update.executeUpdate();

                update.setString(1, "recep123");
                update.setString(2, "reception2");
                update.executeUpdate();
            }

            try (PreparedStatement updateName = connection.prepareStatement("UPDATE users SET full_name = ? WHERE username = ? AND (full_name IS NULL OR full_name = '')")) {
                updateName.setString(1, "System Administrator");
                updateName.setString(2, "admin");
                updateName.executeUpdate();

                updateName.setString(1, "Receptionist One");
                updateName.setString(2, "reception1");
                updateName.executeUpdate();

                updateName.setString(1, "Receptionist Two");
                updateName.setString(2, "reception2");
                updateName.executeUpdate();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to seed default users", ex);
        }
    }
}
