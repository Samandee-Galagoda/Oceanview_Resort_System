package com.oceanview.resort.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean htmlMode = "html".equalsIgnoreCase(request.getParameter("mode"));
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        if (htmlMode) {
            // create a fresh session just to carry a logout success message
            request.getSession(true).setAttribute(
                    "flashMessage",
                    "You have been logged out successfully."
            );
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            sendSuccess(response, "Logged out", null);
        }
    }
}
