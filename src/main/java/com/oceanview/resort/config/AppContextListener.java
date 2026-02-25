package com.oceanview.resort.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            new DatabaseInitializer().initialize();
            sce.getServletContext().setAttribute("startupError", null);
        } catch (Exception ex) {
            // Don't fail the whole deployment (which would surface as a 404).
            // Instead, log and allow the app to load so the UI can show a friendly message.
            String message = "Startup database initialization failed: " + ex.getMessage();
            System.err.println(message);
            ex.printStackTrace();
            sce.getServletContext().setAttribute("startupError", message);
        }
    }
}
