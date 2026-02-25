package com.oceanview.resort.controller;

import com.oceanview.resort.util.JsonUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {
    protected String getRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object role = session.getAttribute("role");
        return role == null ? null : String.valueOf(role);
    }

    protected boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    protected boolean ensureAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isAuthenticated(request)) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Please login to continue");
            return false;
        }
        return true;
    }

    protected boolean ensureAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return false;
        }
        String role = getRole(request);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            sendError(response, HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return false;
        }
        return true;
    }

    protected void sendSuccess(HttpServletResponse response, String message, Object data) throws IOException {
        writeApiResponse(response, HttpServletResponse.SC_OK, true, message, data);
    }

    protected void sendError(HttpServletResponse response, int status, String message) throws IOException {
        writeApiResponse(response, status, false, message, null);
    }

    protected void writeApiResponse(HttpServletResponse response, int status, boolean success, String message, Object data) throws IOException {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("success", success);
        payload.put("message", message);
        payload.put("data", data);
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JsonUtil.toJson(payload));
    }
}
