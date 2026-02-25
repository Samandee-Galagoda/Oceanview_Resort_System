package com.oceanview.resort.controller;

import com.oceanview.resort.model.User;
import com.oceanview.resort.service.UserService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserManagementServlet extends BaseServlet {
    private transient UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(DaoFactory.getInstance().getUserDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        List<User> users = userService.listUsers();
        List<Map<String, Object>> items = new ArrayList<>();
        for (User u : users) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId());
            m.put("fullName", u.getFullName());
            m.put("username", u.getUsername());
            m.put("role", u.getRole());
            m.put("password", u.getPassword()); // demo visibility
            items.add(m);
        }
        sendSuccess(response, "Users list", items);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String fullName = request.getParameter("fullName");
                String role = request.getParameter("role");
                // UI only asks for username/password. Default fullName and role when missing.
                if (fullName == null || fullName.trim().isEmpty()) {
                    fullName = username;
                }
                if (role == null || role.trim().isEmpty()) {
                    role = "RECEPTIONIST";
                }
                userService.createUser(fullName, username, password, role);
                sendSuccess(response, "User created", null);
                return;
            }
            if ("update".equalsIgnoreCase(action)) {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String role = request.getParameter("role");
                User existing = userService.findByUsername(username);
                if (existing == null) {
                    throw new IllegalArgumentException("User not found");
                }
                userService.updateUser(
                        existing.getId(),
                        null,                  // keep existing full name
                        username,
                        password,
                        role
                );
                sendSuccess(response, "User updated", null);
                return;
            }
            if ("delete".equalsIgnoreCase(action)) {
                String username = request.getParameter("username");
                User existing = userService.findByUsername(username);
                if (existing == null) {
                    throw new IllegalArgumentException("User not found");
                }
                userService.deleteUser(existing.getId());
                sendSuccess(response, "User deleted", null);
                return;
            }
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        } catch (IllegalArgumentException ex) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed: " + ex.getMessage());
        }
    }
}

