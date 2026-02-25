package com.oceanview.resort.controller;

import com.oceanview.resort.dto.ReportResponseDTO;
import com.oceanview.resort.service.ReportService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportServlet extends BaseServlet {
    private transient ReportService reportService;

    @Override
    public void init() throws ServletException {
        reportService = new ReportService(DaoFactory.getInstance().getReservationDao());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return;
        }
        // Admin weekly graph endpoint
        String view = request.getParameter("view");
        try {
            if ("weeklyRoomTypes".equalsIgnoreCase(view)) {
                if (!ensureAdmin(request, response)) {
                    return;
                }
                Map<String, Integer> counts = reportService.getWeeklyRoomTypeCounts();
                Map<String, Object> data = new LinkedHashMap<>();
                data.put("rangeDays", 7);
                data.put("counts", counts);
                sendSuccess(response, "Weekly room type reservations", data);
                return;
            }

            ReportResponseDTO report = reportService.getSummary();
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("totalReservations", report.getTotalReservations());
            data.put("activeReservations", report.getActiveReservations());
            data.put("checkoutsNextSevenDays", report.getCheckoutsNextSevenDays());
            data.put("estimatedRevenue", report.getEstimatedRevenue());
            data.put("mostPopularRoomType", report.getMostPopularRoomType());
            sendSuccess(response, "Report generated", data);
        } catch (Exception ex) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate report");
        }
    }
}
