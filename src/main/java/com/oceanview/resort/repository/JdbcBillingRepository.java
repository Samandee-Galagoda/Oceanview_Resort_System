package com.oceanview.resort.repository;

import com.oceanview.resort.dao.BillingDao;
import com.oceanview.resort.model.Billing;
import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcBillingRepository implements BillingDao {

    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public void create(Billing billing) {
        String sql = "INSERT INTO invoices " +
                "(invoice_number, reservation_id, issued_at, nights, nightly_rate, total_amount, payment_status, payment_method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, billing.getInvoiceNumber());
            stmt.setLong(2, billing.getReservationId());
            LocalDateTime issuedAt = billing.getIssuedAt() == null ? LocalDateTime.now() : billing.getIssuedAt();
            stmt.setTimestamp(3, Timestamp.valueOf(issuedAt));
            stmt.setLong(4, billing.getNights());
            stmt.setBigDecimal(5, billing.getNightlyRate());
            stmt.setBigDecimal(6, billing.getTotalAmount());
            stmt.setString(7, billing.getPaymentStatus());
            stmt.setString(8, billing.getPaymentMethod());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    billing.setId(keys.getLong(1));
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create invoice", ex);
        }
    }

    @Override
    public Billing findByInvoiceNumber(String invoiceNumber) {
        String sql = "SELECT id, invoice_number, reservation_id, issued_at, nights, nightly_rate, total_amount, " +
                "payment_status, payment_method " +
                "FROM invoices WHERE invoice_number = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch invoice by number", ex);
        }
    }

    @Override
    public Billing findByReservationId(long reservationId) {
        String sql = "SELECT id, invoice_number, reservation_id, issued_at, nights, nightly_rate, total_amount, " +
                "payment_status, payment_method " +
                "FROM invoices WHERE reservation_id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch invoice by reservation id", ex);
        }
    }

    private Billing mapRow(ResultSet rs) throws Exception {
        Billing billing = new Billing();
        billing.setId(rs.getLong("id"));
        billing.setInvoiceNumber(rs.getString("invoice_number"));
        billing.setReservationId(rs.getLong("reservation_id"));
        Timestamp issued = rs.getTimestamp("issued_at");
        billing.setIssuedAt(issued == null ? null : issued.toLocalDateTime());
        billing.setNights(rs.getLong("nights"));
        billing.setNightlyRate(rs.getBigDecimal("nightly_rate"));
        billing.setTotalAmount(rs.getBigDecimal("total_amount"));
        billing.setPaymentStatus(rs.getString("payment_status"));
        billing.setPaymentMethod(rs.getString("payment_method"));
        return billing;
    }
}

