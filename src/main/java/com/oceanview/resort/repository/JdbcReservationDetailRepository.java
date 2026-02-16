package com.oceanview.resort.repository;

import com.oceanview.resort.dao.ReservationDetailDao;
import com.oceanview.resort.model.ReservationDetail;
import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcReservationDetailRepository implements ReservationDetailDao {

    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public void create(ReservationDetail detail) {
        String sql = "INSERT INTO reservation_details " +
                "(reservation_id, adults, children, special_requests, notes) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, detail.getReservationId());
            stmt.setInt(2, detail.getAdults());
            stmt.setInt(3, detail.getChildren());
            stmt.setString(4, detail.getSpecialRequests());
            stmt.setString(5, detail.getNotes());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create reservation detail", ex);
        }
    }

    @Override
    public ReservationDetail findByReservationId(long reservationId) {
        String sql = "SELECT id, reservation_id, adults, children, special_requests, notes " +
                "FROM reservation_details WHERE reservation_id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch reservation detail", ex);
        }
    }

    @Override
    public boolean deleteByReservationId(long reservationId) {
        String sql = "DELETE FROM reservation_details WHERE reservation_id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to delete reservation detail", ex);
        }
    }

    private ReservationDetail mapRow(ResultSet rs) throws Exception {
        ReservationDetail detail = new ReservationDetail();
        detail.setId(rs.getLong("id"));
        detail.setReservationId(rs.getLong("reservation_id"));
        detail.setAdults(rs.getInt("adults"));
        detail.setChildren(rs.getInt("children"));
        detail.setSpecialRequests(rs.getString("special_requests"));
        detail.setNotes(rs.getString("notes"));
        return detail;
    }
}

