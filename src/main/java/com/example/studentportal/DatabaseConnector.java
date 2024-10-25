package com.example.studentportal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static final String PROPERTIES_FILE = "src\\main\\resources\\db.properties";
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private Connection connection;
    // Load the properties file
    static {
        try (InputStream input = DatabaseConnector.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE);
                throw new IOException("Sorry, unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            URL = properties.getProperty("db.url");
            USERNAME = "kimho"; // Ensure this is set to 'kimho'
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException ex) {
            System.err.println("IOException occurred: " + ex.getMessage());
        }
    }
    // Get the connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection established.");
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database.");
                throw e; // Re-throw the exception after logging
            }
        }
        return connection;
    }
    // Close the connection
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
