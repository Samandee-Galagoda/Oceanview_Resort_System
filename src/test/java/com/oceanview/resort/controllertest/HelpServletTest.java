package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.HelpServlet;
import com.oceanview.resort.service.HelpService;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

/**
 * TDD pass tests for HelpServlet.
 */
public class HelpServletTest {

    private HelpServlet servlet;
    private HelpService helpService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setup() throws IOException {
        servlet = new HelpServlet();
        helpService = mock(HelpService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn("reception");
        when(response.getWriter()).thenReturn(printWriter);
        when(helpService.getGuidelines()).thenReturn(Collections.singletonList("Help line"));

        injectHelpService();
    }

    private void injectHelpService() {
        try {
            Field field = HelpServlet.class.getDeclaredField("helpService");
            field.setAccessible(true);
            field.set(servlet, helpService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject HelpService", e);
        }
    }

    private void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = HelpServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(servlet, req, resp);
    }

    @Test
    public void testDoGet_Authenticated_ReturnsOK() throws Exception {
        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_OK, statusCaptor.getValue(),
                "doGet authenticated should return 200");
    }
}
