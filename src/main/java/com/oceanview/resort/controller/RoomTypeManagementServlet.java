package com.oceanview.resort.controller;

import com.oceanview.resort.model.RoomType;
import com.oceanview.resort.service.RoomTypeService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomTypeManagementServlet extends BaseServlet {
    private transient RoomTypeService roomTypeService;

    @Override
    public void init() throws ServletException {
        roomTypeService = new RoomTypeService(DaoFactory.getInstance().getRoomTypeDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        List<RoomType> list = roomTypeService.list();
        List<Map<String, Object>> items = new ArrayList<>();
        for (RoomType t : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", t.getId());
            m.put("typeName", t.getTypeName());
            m.put("nightlyRate", t.getNightlyRate());
            m.put("maxOccupancy", t.getMaxOccupancy());
            m.put("description", t.getDescription());
            items.add(m);
        }
        sendSuccess(response, "Room types list", items);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("create".equalsIgnoreCase(action)) {
                roomTypeService.create(
                        request.getParameter("typeName"),
                        request.getParameter("nightlyRate"),
                        request.getParameter("maxOccupancy"),
                        request.getParameter("description")
                );
                sendSuccess(response, "Room type created", null);
                return;
            }
            if ("update".equalsIgnoreCase(action)) {
                roomTypeService.update(
                        request.getParameter("id"),
                        request.getParameter("typeName"),
                        request.getParameter("nightlyRate"),
                        request.getParameter("maxOccupancy"),
                        request.getParameter("description")
                );
                sendSuccess(response, "Room type updated", null);
                return;
            }
            if ("delete".equalsIgnoreCase(action)) {
                roomTypeService.delete(request.getParameter("id"));
                sendSuccess(response, "Room successfully deleted", null);
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

