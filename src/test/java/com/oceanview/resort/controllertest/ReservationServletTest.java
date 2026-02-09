package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.ReservationServlet;
import com.oceanview.resort.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

public class ReservationServletTest {

    private ReservationServlet reservationServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ReservationService reservationService;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setup() throws IOException {
        reservationServlet = new ReservationServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        reservationService = mock(ReservationService.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn("reception");
        when(session.getAttribute("role")).thenReturn("STAFF");
        when(response.getWriter()).thenReturn(printWriter);

        injectReservationService();
    }

    private void injectReservationService() {
        try {
            Field field = ReservationServlet.class.getDeclaredField("reservationService");
            field.setAccessible(true);
            field.set(reservationServlet, reservationService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock ReservationService", e);
        }
    }

    private void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = ReservationServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(reservationServlet, req, resp);
    }

    private void invokeDoPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = ReservationServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(reservationServlet, req, resp);
    }

    /**
     * TDD pass test 1: GET with non-existent reservation number returns 404 Not Found.
     */
    @Test
    public void testGetReservationWhenNotFound_ReturnsNotFound() throws Exception {
        when(request.getParameter("reservationNumber")).thenReturn("NONEXISTENT123");
        when(reservationService.getReservation("NONEXISTENT123")).thenReturn(null);

        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        int status = statusCaptor.getValue();
        assertEquals(HttpServletResponse.SC_NOT_FOUND, status, "GET for non-existent reservation should return 404");
    }

    /**
     * TDD pass test 2: POST with missing guest name returns 400 Bad Request.
     */
    @Test
    public void testPostReservationWithMissingGuestName_ReturnsBadRequest() throws Exception {
        when(request.getParameter("reservationNumber")).thenReturn("RES001");
        when(request.getParameter("guestName")).thenReturn(null);
        when(request.getParameter("address")).thenReturn("123 Beach Rd");
        when(request.getParameter("contactNumber")).thenReturn("0771234567");
        when(request.getParameter("roomType")).thenReturn("Standard");
        when(request.getParameter("checkInDate")).thenReturn("2025-03-01");
        when(request.getParameter("checkOutDate")).thenReturn("2025-03-03");
        when(reservationService.addReservation(any())).thenThrow(new IllegalArgumentException("Guest name is required"));

        invokeDoPost(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        int status = statusCaptor.getValue();
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, status, "POST with missing guest name should return 400");
    }
}
