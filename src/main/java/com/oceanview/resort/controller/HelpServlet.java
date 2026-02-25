package com.oceanview.resort.controller;

import com.oceanview.resort.service.HelpService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelpServlet extends BaseServlet {
    private transient HelpService helpService;

    @Override
    public void init() throws ServletException {
        helpService = new HelpService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("guidelines", helpService.getGuidelines());
        sendSuccess(response, "Help guidelines", data);
    }
}
