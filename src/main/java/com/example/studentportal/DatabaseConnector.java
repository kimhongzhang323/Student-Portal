package com.example.studentportal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROPERTIES_FILE = "/db.properties"; // Use '/' for cross-platform compatibility
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private Connection connection;

    static {
        try (InputStream input = DatabaseConnector.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE);
                throw new IOException("Unable to find the database properties file.");
            }
            properties.load(input);
            URL = properties.getProperty("db.url");
            USERNAME = properties.getProperty("db.username");
            PASSWORD = properties.getProperty("db.password");
        } catch (IOException ex) {
            System.err.println("IOException occurred while loading properties: " + ex.getMessage());
        }
    }

    // Method to establish a connection
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection established.");
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database: " + e.getMessage());
                throw e; // Re-throw the exception after logging
            }
        }
        return connection;
    }

    // Method to close the connection
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error while closing connection: " + e.getMessage());
            }
        }
    }
}
