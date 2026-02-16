package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.BillingServlet;
import com.oceanview.resort.dto.BillResponseDTO;
import com.oceanview.resort.service.BillingService;
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
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

public class BillingServletTest {

    private BillingServlet billingServlet;
    private BillingService billingService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setup() throws IOException {
        billingServlet = new BillingServlet();
        billingService = mock(BillingService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn("reception");
        when(session.getAttribute("role")).thenReturn("STAFF");
        when(response.getWriter()).thenReturn(printWriter);

        injectBillingService();
    }

    private void injectBillingService() {
        try {
            Field field = BillingServlet.class.getDeclaredField("billingService");
            field.setAccessible(true);
            field.set(billingServlet, billingService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject BillingService into BillingServlet", e);
        }
    }

    private void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = BillingServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(billingServlet, req, resp);
    }

    /**
     * TDD pass test 1: Successful bill generation should return HTTP 200 and a success message.
     */
    @Test
    public void testGenerateBill_Success() throws Exception {
        // Arrange
        when(request.getParameter("reservationNumber")).thenReturn("RES100");
        BillResponseDTO bill = new BillResponseDTO();
        bill.setReservationNumber("RES100");
        bill.setRoomType("Standard");
        bill.setNights(3L);
        bill.setNightlyRate(new BigDecimal("100.00"));
        bill.setTotalAmount(new BigDecimal("300.00"));
        when(billingService.generateBill("RES100")).thenReturn(bill);

        // Act
        invokeDoGet(request, response);

        // Assert - capture status and expect 200 OK
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        int status = statusCaptor.getValue();
        assertEquals(HttpServletResponse.SC_OK, status,
                "Successful bill generation should return HTTP 200");
    }

    /**
     * TDD pass test 2: When reservation is not found, servlet should return 404 Not Found.
     */
    @Test
    public void testGenerateBill_ReservationNotFound() throws Exception {
        // Arrange
        when(request.getParameter("reservationNumber")).thenReturn("RES999");
        when(billingService.generateBill(anyString())).thenReturn(null);

        // Act
        invokeDoGet(request, response);

        // Assert - capture status and expect 404 Not Found
        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        int status = statusCaptor.getValue();
        assertEquals(HttpServletResponse.SC_NOT_FOUND, status,
                "Non-existent reservation should return 404 Not Found");
    }

    @Test
    public void testGenerateBill_Success_ReturnsOK() throws Exception {
        when(request.getParameter("reservationNumber")).thenReturn("RES100");
        BillResponseDTO bill = new BillResponseDTO();
        bill.setReservationNumber("RES100");
        bill.setRoomType("Standard");
        bill.setNights(3L);
        bill.setNightlyRate(new BigDecimal("100.00"));
        bill.setTotalAmount(new BigDecimal("300.00"));
        when(billingService.generateBill("RES100")).thenReturn(bill);

        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_OK, statusCaptor.getValue(),
                "Successful bill generation should return 200");
    }

    @Test
    public void testGenerateBill_ReservationNotFound_ReturnsNotFound() throws Exception {
        when(request.getParameter("reservationNumber")).thenReturn("RES999");
        when(billingService.generateBill(anyString())).thenReturn(null);

        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_NOT_FOUND, statusCaptor.getValue(),
                "Reservation not found should return 404");
    }
}

