package com.oceanview.resort.controller;

import com.oceanview.resort.model.User;
import com.oceanview.resort.service.AuthService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends BaseServlet {
    private transient AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService(DaoFactory.getInstance().getUserDao());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean htmlMode = "html".equalsIgnoreCase(request.getParameter("mode"));
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = authService.login(username, password);
            if (user == null) {
                if (htmlMode) {
                    response.sendRedirect(request.getContextPath() + "/index.jsp?error=" +
                            URLEncoder.encode("Invalid username or password", StandardCharsets.UTF_8.name()));
                } else {
                    sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                }
                return;
            }
            request.getSession(true).setAttribute("user", user.getUsername());
            request.getSession(true).setAttribute("role", user.getRole());
            Map<String, Object> data = new HashMap<>();
            data.put("username", user.getUsername());
            data.put("role", user.getRole());
            if (htmlMode) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin-menu.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/menu.jsp");
                }
            } else {
                sendSuccess(response, "Successfully logged in as " + user.getRole(), data);
            }
        } catch (IllegalArgumentException ex) {
            if (htmlMode) {
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=" +
                        URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8.name()));
            } else {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            }
        } catch (Exception ex) {
            if (htmlMode) {
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=" +
                        URLEncoder.encode("Login failed: " + ex.getMessage(), StandardCharsets.UTF_8.name()));
            } else {
                sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Login failed: " + ex.getMessage());
            }
        }
    }
}
