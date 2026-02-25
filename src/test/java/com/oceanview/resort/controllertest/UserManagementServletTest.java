package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.UserManagementServlet;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.UserService;
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
 * TDD pass tests for UserManagementServlet.
 */
public class UserManagementServletTest {

    private UserManagementServlet servlet;
    private UserService userService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setup() throws IOException {
        servlet = new UserManagementServlet();
        userService = mock(UserService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute("user")).thenReturn("admin");
        when(session.getAttribute("role")).thenReturn("ADMIN");
        when(response.getWriter()).thenReturn(printWriter);

        injectUserService();
    }

    private void injectUserService() {
        try {
            Field field = UserManagementServlet.class.getDeclaredField("userService");
            field.setAccessible(true);
            field.set(servlet, userService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject UserService", e);
        }
    }

    private void invokeDoGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = UserManagementServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(servlet, req, resp);
    }

    private void invokeDoPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Method m = UserManagementServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        m.setAccessible(true);
        m.invoke(servlet, req, resp);
    }

    @Test
    public void testDoGet_Admin_ReturnsOK() throws Exception {
        when(userService.listUsers()).thenReturn(Collections.singletonList(new User()));

        invokeDoGet(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_OK, statusCaptor.getValue(),
                "doGet with admin should return 200");
    }

    @Test
    public void testDoPost_CreateUser_ReturnsOK() throws Exception {
        when(request.getParameter("action")).thenReturn("create");
        when(request.getParameter("username")).thenReturn("reception1");
        when(request.getParameter("password")).thenReturn("pass1234");

        invokeDoPost(request, response);

        ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(response).setStatus(statusCaptor.capture());
        assertEquals(HttpServletResponse.SC_OK, statusCaptor.getValue(),
                "doPost create success should return 200");
    }
}
