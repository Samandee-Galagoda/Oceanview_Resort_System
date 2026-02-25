package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.ReportServlet;
import com.oceanview.resort.dto.ReportResponseDTO;
import com.oceanview.resort.service.ReportService;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

/**
 * TDD pass tests for ReportServlet.
 */
public class ReportServletTest {

    private ReportServlet servlet;
    private ReportService reportService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setup() throws IOException {
        servlet = new ReportServlet();
        reportService = mock(ReportService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn("reception");
        when(response.getWriter()).thenReturn(printWriter);

        ReportResponseDTO report = new ReportResponseDTO();
        report.setTotalReservations(5);
        report.setActiveReservations(2);
        report.setCheckoutsNextSevenDays(1);
        report.setEstimatedRevenue(BigDecimal.valueOf(1000));
        report.setMostPopularRoomType("Standard");
        when(reportService.getSummary()).thenReturn(report);

        injectReportService();
    }

    private void injectReportService() {
        try {
            Field field = ReportServlet.class.getDeclaredField("reportService");
            field.setAccessible(true);
            field.set(servlet, reportService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject ReportService", e);
        }
    }

    private void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = ReportServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(servlet, req, resp);
    }

    @Test
    public void testDoGet_Success_ReturnsOK() throws Exception {
        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_OK, statusCaptor.getValue(),
                "doGet success should return 200");
    }
}
