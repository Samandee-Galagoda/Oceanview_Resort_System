package com.oceanview.resort.controllertest;

import com.oceanview.resort.controller.LoginServlet;
import com.oceanview.resort.model.User;
import com.oceanview.resort.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoginServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private AuthService authService;
    private LoginServlet loginServlet;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        loginServlet = new LoginServlet();
        // Create mocks manually to avoid Byte Buddy issues with Java 23
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        authService = mock(AuthService.class);
        
        // Reset StringWriter for each test to avoid cross-test contamination
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        // Setup common mocks
        when(request.getSession(true)).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(response.getWriter()).thenReturn(printWriter);
    }

    /**
     * Helper method to inject mock AuthService into LoginServlet using reflection
     */
    private void injectAuthService() {
        try {
            Field authServiceField = LoginServlet.class.getDeclaredField("authService");
            authServiceField.setAccessible(true);
            authServiceField.set(loginServlet, authService);
        } catch (Exception e) {
            fail("Failed to inject mock AuthService: " + e.getMessage());
        }
    }

    /**
     * TDD Test 1: Valid credentials should result in successful login
     * This test verifies that:
     * - authService.login() is called with the provided credentials
     * - When a valid User is returned, session attributes are set
     * - Success response is sent
     * 
     * If you remove the credential checking logic (authService.login call or user null check),
     * this test will FAIL (RED)
     */
    @Test
    void testLoginCredentials_ValidCredentials_ShouldAuthenticateSuccessfully() throws IOException {
        // Arrange
        String username = "admin";
        String password = "admin123";
        
        User validUser = new User();
        validUser.setId(1L);
        validUser.setUsername(username);
        validUser.setPassword(password);
        validUser.setRole("ADMIN");
        validUser.setFullName("Admin User");

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("mode")).thenReturn(null); // JSON mode
        when(request.getContextPath()).thenReturn("/Oceanview_Resort_System");

        injectAuthService();
        // Mock: authService.login() returns a valid user (credentials are correct)
        when(authService.login(username, password)).thenReturn(validUser);

        // Act
        loginServlet.doPost(request, response);

        // Assert - Verify credential checking logic is present and working
        // 1. Verify authService.login() was called with the credentials
        verify(authService, times(1)).login(username, password);
        
        // 2. Verify that when valid user is returned, session is set (authentication succeeded)
        // LoginServlet calls getSession(true) twice and sets one attribute each time
        verify(session, atLeastOnce()).setAttribute(eq("user"), eq(username));
        verify(session, atLeastOnce()).setAttribute(eq("role"), eq("ADMIN"));
        
        // 3. Verify success response is sent
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        
        // 4. Verify response contains success message
        printWriter.flush();
        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("\"success\":true"), 
            "FAILED: Valid credentials should result in success=true. " +
            "If this fails, the credential checking logic (authService.login or user null check) may be missing.");
        assertTrue(responseBody.contains("Successfully logged in"), 
            "FAILED: Valid credentials should result in success message. " +
            "If this fails, the credential validation logic may be removed.");
    }

    /**
     * TDD Test 2: Invalid credentials should result in authentication failure
     * This test verifies that:
     * - authService.login() is called with the provided credentials
     * - When null is returned (invalid credentials), authentication fails
     * - Error response is sent (401 Unauthorized)
     * - Session is NOT set (user is not authenticated)
     * 
     * If you remove the credential checking logic (authService.login call or user null check),
     * this test will FAIL (RED)
     */
    @Test
    void testLoginCredentials_InvalidCredentials_ShouldRejectAuthentication() throws IOException {
        // Arrange
        String username = "admin";
        String wrongPassword = "wrongpassword";
        
        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(wrongPassword);
        when(request.getParameter("mode")).thenReturn(null); // JSON mode
        when(request.getContextPath()).thenReturn("/Oceanview_Resort_System");

        injectAuthService();
        // Mock: authService.login() returns null (credentials are incorrect)
        when(authService.login(username, wrongPassword)).thenReturn(null);

        // Act
        loginServlet.doPost(request, response);

        // Assert - Verify credential checking logic is present and working
        // 1. Verify authService.login() was called with the credentials
        verify(authService, times(1)).login(username, wrongPassword);
        
        // 2. Verify that when null user is returned, session is NOT set (authentication failed)
        verify(session, never()).setAttribute(anyString(), any());
        
        // 3. Verify error response is sent (401 Unauthorized)
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        
        // 4. Verify response contains error message about invalid credentials
        printWriter.flush();
        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("\"success\":false"), 
            "FAILED: Invalid credentials should result in success=false. " +
            "If this fails, the credential checking logic (user null check) may be missing.");
        assertTrue(responseBody.contains("Invalid username or password"), 
            "FAILED: Invalid credentials should result in 'Invalid username or password' error. " +
            "If this fails, the credential validation logic (lines 31-38 in LoginServlet) may be removed.");
    }
}
