package com.oceanview.resort.repository;

import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.model.Reservation;
import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationRepository implements ReservationDao {
    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public boolean existsByReservationNumber(String reservationNumber) {
        String sql = "SELECT 1 FROM reservations WHERE reservation_number = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reservationNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to check reservation number", ex);
        }
    }

    @Override
    public void create(Reservation reservation) {
        String sql = "INSERT INTO reservations " +
                "(reservation_number, guest_id, room_type_id, check_in_date, check_out_date, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, reservation.getReservationNumber());
            stmt.setLong(2, reservation.getGuestId());
            stmt.setLong(3, reservation.getRoomTypeId());
            stmt.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setString(6, reservation.getStatus() == null ? "BOOKED" : reservation.getStatus());
            LocalDateTime createdAt = reservation.getCreatedAt() == null ? LocalDateTime.now() : reservation.getCreatedAt();
            stmt.setTimestamp(7, Timestamp.valueOf(createdAt));
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    reservation.setId(keys.getLong(1));
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create reservation", ex);
        }
    }

    @Override
    public Reservation findByReservationNumber(String reservationNumber) {
        String sql = "SELECT r.id, r.reservation_number, r.check_in_date, r.check_out_date, r.created_at, r.status," +
                " g.id AS guest_id, g.full_name, g.address, g.contact_number," +
                " rt.id AS room_type_id, rt.type_name" +
                " FROM reservations r" +
                " JOIN guests g ON r.guest_id = g.id" +
                " JOIN room_types rt ON r.room_type_id = rt.id" +
                " WHERE r.reservation_number = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reservationNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return mapRow(rs);
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch reservation", ex);
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id, r.reservation_number, r.check_in_date, r.check_out_date, r.created_at, r.status," +
                " g.id AS guest_id, g.full_name, g.address, g.contact_number," +
                " rt.id AS room_type_id, rt.type_name" +
                " FROM reservations r" +
                " JOIN guests g ON r.guest_id = g.id" +
                " JOIN room_types rt ON r.room_type_id = rt.id" +
                " ORDER BY r.created_at DESC";
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservations.add(mapRow(rs));
            }
            return reservations;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to list reservations", ex);
        }
    }

    @Override
    public int countActiveOn(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_in_date <= ? AND check_out_date > ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setDate(2, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to count active reservations", ex);
        }
    }

    @Override
    public int countCheckoutsBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE check_out_date BETWEEN ? AND ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to count checkouts", ex);
        }
    }

    @Override
    public String findMostPopularRoomType() {
        String sql = "SELECT rt.type_name, COUNT(*) AS total " +
                "FROM reservations r JOIN room_types rt ON r.room_type_id = rt.id " +
                "GROUP BY rt.type_name ORDER BY total DESC LIMIT 1";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getString("type_name") : "N/A";
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to find popular room type", ex);
        }
    }

    @Override
    public double sumEstimatedRevenue() {
        String sql = "SELECT SUM(DATEDIFF(r.check_out_date, r.check_in_date) * rt.nightly_rate) AS total " +
                "FROM reservations r JOIN room_types rt ON r.room_type_id = rt.id";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to calculate revenue", ex);
        }
    }

    private Reservation mapRow(ResultSet rs) throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setReservationNumber(rs.getString("reservation_number"));
        reservation.setGuestId(rs.getLong("guest_id"));
        reservation.setGuestName(rs.getString("full_name"));
        reservation.setAddress(rs.getString("address"));
        reservation.setContactNumber(rs.getString("contact_number"));
        reservation.setRoomTypeId(rs.getLong("room_type_id"));
        reservation.setRoomType(rs.getString("type_name"));
        reservation.setCheckInDate(rs.getDate("check_in_date").toLocalDate());
        reservation.setCheckOutDate(rs.getDate("check_out_date").toLocalDate());
        Timestamp created = rs.getTimestamp("created_at");
        reservation.setCreatedAt(created == null ? null : created.toLocalDateTime());
        reservation.setStatus(rs.getString("status"));
        return reservation;
    }
}
