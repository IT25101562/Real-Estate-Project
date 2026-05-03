package com.realestate.admin.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConnection — Singleton utility class for MS SQL Server connection.
 * Reads configuration from db.properties file.
 *
 * Demonstrates: Singleton Design Pattern
 */
public class DatabaseConnection {

    // Singleton instance
    private static DatabaseConnection instance;

    private String url;
    private String username;
    private String password;
    private String driver;

    /**
     * Private constructor — prevents external instantiation.
     * Loads database properties from the classpath.
     */
    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader()
                    .getResourceAsStream("db.properties");

            if (input != null) {
                props.load(input);
                this.driver = props.getProperty("db.driver");
                this.url = props.getProperty("db.url");
                this.username = props.getProperty("db.username");
                this.password = props.getProperty("db.password");
                input.close();
            } else {
                // Fallback defaults
                this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                this.url = "jdbc:sqlserver://localhost:1433;databaseName=RealEstateDB;encrypt=true;trustServerCertificate=true";
                this.username = "sa";
                this.password = "YourPassword123";
            }

            // Load the JDBC driver
            Class.forName(this.driver);

        } catch (Exception e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Creates and returns a new database connection.
     * Callers are responsible for closing the connection.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * Tests the database connection.
     * Returns true if connection is successful.
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
