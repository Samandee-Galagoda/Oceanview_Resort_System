package com.oceanview.resort.configtest;

import com.oceanview.resort.config.AppContextListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentCaptor;

/**
 * TDD GREEN tests for AppContextListener.
 */
public class AppContextListenerTest {

    private AppContextListener listener;
    private ServletContextEvent event;
    private ServletContext servletContext;

    @BeforeEach
    public void setup() {
        listener = new AppContextListener();
        servletContext = mock(ServletContext.class);
        event = new ServletContextEvent(servletContext);
    }

    @Test
    public void testContextInitialized_SetsStartupErrorAttribute() {
        // Act
        listener.contextInitialized(event);

        // Assert: startupError attribute is set (null on success, message on failure)
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(servletContext).setAttribute(eq("startupError"), valueCaptor.capture());
        // In a healthy environment this should be null
        assertNull(valueCaptor.getValue(),
                "startupError should be null when initialization succeeds");
    }
}

