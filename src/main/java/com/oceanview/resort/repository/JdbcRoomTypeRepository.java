package com.oceanview.resort.repository;

import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.util.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcRoomTypeRepository implements RoomTypeDao {
    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public List<RoomType> findAll() {
        String sql = "SELECT id, type_name, nightly_rate, max_occupancy, description FROM room_types ORDER BY type_name";
        List<RoomType> items = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(map(rs));
            }
            return items;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to list room types: " + ex.getMessage(), ex);
        }
    }

    @Override
    public RoomType findById(long id) {
        String sql = "SELECT id, type_name, nightly_rate, max_occupancy, description FROM room_types WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch room type: " + ex.getMessage(), ex);
        }
    }

    @Override
    public RoomType findByName(String typeName) {
        String sql = "SELECT id, type_name, nightly_rate, max_occupancy, description FROM room_types WHERE type_name = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch room type: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void create(RoomType roomType) {
        String sql = "INSERT INTO room_types (type_name, nightly_rate, max_occupancy, description) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getNightlyRate());
            stmt.setInt(3, roomType.getMaxOccupancy());
            stmt.setString(4, roomType.getDescription());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create room type: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(RoomType roomType) {
        String sql = "UPDATE room_types SET type_name=?, nightly_rate=?, max_occupancy=?, description=? WHERE id=?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.getTypeName());
            stmt.setBigDecimal(2, roomType.getNightlyRate());
            stmt.setInt(3, roomType.getMaxOccupancy());
            stmt.setString(4, roomType.getDescription());
            stmt.setLong(5, roomType.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to update room type: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean deleteById(long id) {
        String sql = "DELETE FROM room_types WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to delete room type: " + ex.getMessage(), ex);
        }
    }

    private RoomType map(ResultSet rs) throws Exception {
        RoomType type = new RoomType();
        type.setId(rs.getLong("id"));
        type.setTypeName(rs.getString("type_name"));
        type.setNightlyRate(rs.getBigDecimal("nightly_rate"));
        type.setMaxOccupancy(rs.getInt("max_occupancy"));
        type.setDescription(rs.getString("description"));
        return type;
    }
}

