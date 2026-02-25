package com.oceanview.resort.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionManager {
    private static final DbConnectionManager INSTANCE = new DbConnectionManager();

    private final String url;
    private final String user;
    private final String password;

    private DbConnectionManager() {
        Properties properties = new Properties();
        try (InputStream input = DbConnectionManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            properties.load(input);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load database properties", ex);
        }

        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");

        String driver = properties.getProperty("db.driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Database driver not found: " + driver, ex);
        }
    }

    public static DbConnectionManager getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
