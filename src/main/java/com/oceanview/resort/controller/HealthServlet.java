package com.oceanview.resort.controller;

import com.oceanview.resort.util.DbConnectionManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HealthServlet extends BaseServlet {
    @Override
    public void init() throws ServletException {
        // no-op
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String startupError = (String) getServletContext().getAttribute("startupError");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", startupError == null ? "UP" : "DEGRADED");
        data.put("startupError", startupError);

        // DB diagnostics (helps debug "login failed" due to DB connectivity/schema issues)
        try (Connection connection = DbConnectionManager.getInstance().getConnection()) {
            data.put("dbConnected", true);
            try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM users");
                 ResultSet rs = stmt.executeQuery()) {
                data.put("usersCount", rs.next() ? rs.getInt(1) : 0);
            }
            try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE role='ADMIN'");
                 ResultSet rs = stmt.executeQuery()) {
                data.put("adminCount", rs.next() ? rs.getInt(1) : 0);
            }
            // Show available columns in users table (debug schema mismatches)
            List<String> columns = new ArrayList<>();
            try (PreparedStatement stmt = connection.prepareStatement("SHOW COLUMNS FROM users");
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    columns.add(rs.getString("Field"));
                }
            }
            data.put("usersColumns", columns);
        } catch (Exception ex) {
            data.put("dbConnected", false);
            data.put("dbError", ex.getMessage());
        }
        sendSuccess(response, "Health check", data);
    }
}
