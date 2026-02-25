package com.oceanview.resort.repository;

import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.model.Guest;
import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class JdbcGuestRepository implements GuestDao {
    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public long create(Guest guest) {
        String sql = "INSERT INTO guests (full_name, address, contact_number, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getFullName());
            stmt.setString(2, guest.getAddress());
            stmt.setString(3, guest.getContactNumber());
            stmt.setTimestamp(4, guest.getCreatedAt() == null ? new Timestamp(System.currentTimeMillis()) : Timestamp.valueOf(guest.getCreatedAt()));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : 0L;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create guest", ex);
        }
    }

    @Override
    public Guest findById(long id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Guest guest = new Guest();
                guest.setId(rs.getLong("id"));
                guest.setFullName(rs.getString("full_name"));
                guest.setAddress(rs.getString("address"));
                guest.setContactNumber(rs.getString("contact_number"));
                Timestamp created = rs.getTimestamp("created_at");
                guest.setCreatedAt(created == null ? null : created.toLocalDateTime());
                return guest;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch guest", ex);
        }
    }
}
