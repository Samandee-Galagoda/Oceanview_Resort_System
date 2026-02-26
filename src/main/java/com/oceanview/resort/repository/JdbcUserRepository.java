package com.oceanview.resort.repository;

import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.DbConnectionManager;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class JdbcUserRepository implements UserDao {
    private final DbConnectionManager dbManager = DbConnectionManager.getInstance();

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                User user = new User();
                user.setId(rs.getLong("id"));
                if (hasColumn(rs, "full_name")) {
                    user.setFullName(rs.getString("full_name"));
                }
                user.setUsername(rs.getString("username"));
                if (hasColumn(rs, "password")) {
                    user.setPassword(rs.getString("password"));
                }
                if (hasColumn(rs, "password_hash")) {
                    user.setPasswordHash(rs.getString("password_hash"));
                }
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void create(User user) {
        String sql = "INSERT INTO users (full_name, username, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create user", ex);
        }
    }

    @Override
    public boolean existsAny() {
        String sql = "SELECT 1 FROM users LIMIT 1";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to check users", ex);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY role DESC, username ASC";
        List<User> users = new ArrayList<>();
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                if (hasColumn(rs, "full_name")) {
                    user.setFullName(rs.getString("full_name"));
                }
                user.setUsername(rs.getString("username"));
                if (hasColumn(rs, "password")) {
                    user.setPassword(rs.getString("password"));
                }
                if (hasColumn(rs, "password_hash")) {
                    user.setPasswordHash(rs.getString("password_hash"));
                }
                user.setRole(rs.getString("role"));
                users.add(user);
            }
            return users;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to list users: " + ex.getMessage(), ex);
        }
    }

    @Override
    public User findById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                User user = new User();
                user.setId(rs.getLong("id"));
                if (hasColumn(rs, "full_name")) {
                    user.setFullName(rs.getString("full_name"));
                }
                user.setUsername(rs.getString("username"));
                if (hasColumn(rs, "password")) {
                    user.setPassword(rs.getString("password"));
                }
                if (hasColumn(rs, "password_hash")) {
                    user.setPasswordHash(rs.getString("password_hash"));
                }
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to fetch user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET full_name=?, username=?, password=?, role=? WHERE id=?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setLong(5, user.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to update user: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean deleteById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to delete user: " + ex.getMessage(), ex);
        }
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                if (columnName.equalsIgnoreCase(meta.getColumnLabel(i)) || columnName.equalsIgnoreCase(meta.getColumnName(i))) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
