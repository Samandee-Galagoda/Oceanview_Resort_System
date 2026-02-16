package com.oceanview.resort.controller;

import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.service.BillingService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BillingServlet extends BaseServlet {
    private transient BillingService billingService;

    @Override
    public void init() throws ServletException {
        billingService = new BillingService(
                DaoFactory.getInstance().getReservationDao(),
                DaoFactory.getInstance().getRoomTypeDao(),
                DaoFactory.getInstance().getBillingDao()
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return;
        }
        String reservationNumber = request.getParameter("reservationNumber");
        try {
            BillResponseDTO bill = billingService.generateBill(reservationNumber);
            if (bill == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "Reservation not found");
                return;
            }
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("reservationNumber", bill.getReservationNumber());
            data.put("roomType", bill.getRoomType());
            data.put("nights", bill.getNights());
            data.put("nightlyRate", bill.getNightlyRate());
            data.put("totalAmount", bill.getTotalAmount());
            sendSuccess(response, "Bill generated", data);
        } catch (IllegalArgumentException ex) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate bill");
        }
    }
}
