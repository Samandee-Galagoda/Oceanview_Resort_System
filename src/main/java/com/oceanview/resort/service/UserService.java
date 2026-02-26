package com.oceanview.resort.service;

import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> listUsers() {
        return userDao.findAll();
    }

    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        return userDao.findByUsername(username.trim());
    }

    public void createUser(String fullName, String username, String password, String role) {
        List<String> errors = new ArrayList<>();
        ValidationUtil.requireNonBlank(fullName, "Full name", errors);
        ValidationUtil.requireNonBlank(username, "Username", errors);
        ValidationUtil.requireNonBlank(password, "Password", errors);
        ValidationUtil.requireNonBlank(role, "Role", errors);
        if (password != null && password.trim().length() < 4) {
            errors.add("Password must be at least 4 characters");
        }
        if (username != null && !username.matches("[A-Za-z0-9_]{3,20}")) {
            errors.add("Username must be 3-20 chars (letters/numbers/underscore)");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }
        if (userDao.findByUsername(username.trim()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        String normalizedRole = normalizeRole(role);
        if ("ADMIN".equalsIgnoreCase(normalizedRole)) {
            // Single admin constraint
            for (User u : userDao.findAll()) {
                if ("ADMIN".equalsIgnoreCase(u.getRole())) {
                    throw new IllegalArgumentException("Only one admin is allowed");
                }
            }
        }
        User user = new User();
        user.setFullName(fullName.trim());
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setRole(normalizedRole);
        userDao.create(user);
    }

    public void updateUser(long id, String fullName, String username, String password, String role) {
        User existing = userDao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("User not found");
        }
        String normalizedRole = role == null ? existing.getRole() : normalizeRole(role);
        if ("ADMIN".equalsIgnoreCase(normalizedRole) && !"ADMIN".equalsIgnoreCase(existing.getRole())) {
            for (User u : userDao.findAll()) {
                if ("ADMIN".equalsIgnoreCase(u.getRole())) {
                    throw new IllegalArgumentException("Only one admin is allowed. Cannot promote user to ADMIN.");
                }
            }
        }
        if (password != null && !password.trim().isEmpty() && password.trim().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        if (fullName != null && !fullName.trim().isEmpty()) {
            existing.setFullName(fullName.trim());
        }
        if (username != null && !username.trim().isEmpty()) {
            User byUsername = userDao.findByUsername(username.trim());
            if (byUsername != null && byUsername.getId() != existing.getId()) {
                throw new IllegalArgumentException("Username already exists");
            }
            existing.setUsername(username.trim());
        }
        if (password != null && !password.trim().isEmpty()) {
            existing.setPassword(password.trim());
        }
        existing.setRole(normalizedRole);
        userDao.update(existing);
    }

    public void deleteUser(long id) {
        User existing = userDao.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("User not found");
        }
        if ("ADMIN".equalsIgnoreCase(existing.getRole())) {
            throw new IllegalArgumentException("Admin user cannot be deleted");
        }
        boolean deleted = userDao.deleteById(id);
        if (!deleted) {
            throw new IllegalStateException("Delete failed");
        }
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "RECEPTIONIST";
        }
        String r = role.trim().toUpperCase();
        if ("STAFF".equals(r) || "RECEPTIONIST".equals(r)) {
            return "RECEPTIONIST";
        }
        if ("ADMIN".equals(r)) {
            return "ADMIN";
        }
        throw new IllegalArgumentException("Role must be STAFF or ADMIN");
    }
}

