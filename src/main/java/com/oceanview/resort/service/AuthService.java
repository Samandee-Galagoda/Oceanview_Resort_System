package com.oceanview.resort.service;

import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.model.User;
import com.oceanview.resort.util.PasswordUtil;
import com.oceanview.resort.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User login(String username, String password) {
        List<String> errors = new ArrayList<>();
        ValidationUtil.requireNonBlank(username, "Username", errors);
        ValidationUtil.requireNonBlank(password, "Password", errors);
        if (password != null && password.trim().length() < 4) {
            errors.add("Password must be at least 4 characters");
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("; ", errors));
        }

        User user = userDao.findByUsername(username.trim());
        if (user == null) {
            return null;
        }
        String input = password.trim();
        // Plain-text password (demo/testing)
        if (user.getPassword() != null && input.equals(user.getPassword())) {
            return user;
        }
        // Legacy fallback (if DB still has password_hash from older schema)
        if (user.getPasswordHash() != null) {
            String hashed = PasswordUtil.hash(input);
            return hashed.equals(user.getPasswordHash()) ? user : null;
        }
        return null;
    }
}
