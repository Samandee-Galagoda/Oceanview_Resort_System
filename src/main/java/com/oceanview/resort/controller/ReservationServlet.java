package com.oceanview.resort.controller;

import com.oceanview.resort.dto.ReservationRequestDTO;
import com.oceanview.resort.dto.ReservationResponseDTO;
import com.oceanview.resort.mapper.ReservationDetailMapper;
import com.oceanview.resort.mapper.ReservationMapper;
import com.oceanview.resort.service.ReservationService;
import com.oceanview.resort.util.DaoFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReservationServlet extends BaseServlet {
    private transient ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        reservationService = new ReservationService(
                DaoFactory.getInstance().getReservationDao(),
                DaoFactory.getInstance().getGuestDao(),
                DaoFactory.getInstance().getRoomTypeDao(),
                new ReservationMapper(),
                DaoFactory.getInstance().getReservationDetailDao(),
                new ReservationDetailMapper()
        );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return;
        }
        try {
            String action = request.getParameter("action");
            if ("cancel".equalsIgnoreCase(action)) {
                String reservationNumber = request.getParameter("reservationNumber");
                reservationService.cancelReservation(reservationNumber);
                sendSuccess(response, "Reservation cancelled", null);
                return;
            }

            ReservationRequestDTO dto = new ReservationRequestDTO();
            dto.setReservationNumber(request.getParameter("reservationNumber"));
            dto.setGuestName(request.getParameter("guestName"));
            dto.setAddress(request.getParameter("address"));
            dto.setContactNumber(request.getParameter("contactNumber"));
            dto.setRoomType(request.getParameter("roomType"));
            dto.setCheckInDate(request.getParameter("checkInDate"));
            dto.setCheckOutDate(request.getParameter("checkOutDate"));
            ReservationResponseDTO created = reservationService.addReservation(dto);
            sendSuccess(response, "Reservation added successfully", toMap(created));
        } catch (IllegalArgumentException ex) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add reservation");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ensureAuthenticated(request, response)) {
            return;
        }
        String reservationNumber = request.getParameter("reservationNumber");
        String guestName = request.getParameter("guestName");
        try {
            // search by guest name (available to any authenticated user)
            if (guestName != null && !guestName.trim().isEmpty()) {
                List<ReservationResponseDTO> reservations = reservationService.searchReservationsByGuestName(guestName);
                List<Map<String, Object>> items = new ArrayList<>();
                for (ReservationResponseDTO dto : reservations) {
                    items.add(toMap(dto));
                }
                sendSuccess(response, "Reservations matching guest name", items);
                return;
            }

            if (reservationNumber == null || reservationNumber.trim().isEmpty()) {
                // Listing all reservations is admin-only
                if (!ensureAdmin(request, response)) {
                    return;
                }
                List<ReservationResponseDTO> reservations = reservationService.listReservations();
                List<Map<String, Object>> items = new ArrayList<>();
                for (ReservationResponseDTO dto : reservations) {
                    items.add(toMap(dto));
                }
                sendSuccess(response, "Reservations list", items);
                return;
            }
            ReservationResponseDTO reservation = reservationService.getReservation(reservationNumber);
            if (reservation == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "Reservation not found");
                return;
            }
            sendSuccess(response, "Reservation found", toMap(reservation));
        } catch (IllegalArgumentException ex) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch reservation");
        }
    }

    private Map<String, Object> toMap(ReservationResponseDTO dto) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("reservationNumber", dto.getReservationNumber());
        map.put("guestName", dto.getGuestName());
        map.put("address", dto.getAddress());
        map.put("contactNumber", dto.getContactNumber());
        map.put("roomType", dto.getRoomType());
        map.put("checkInDate", dto.getCheckInDate());
        map.put("checkOutDate", dto.getCheckOutDate());
        map.put("createdAt", dto.getCreatedAt());
        return map;
    }
}
