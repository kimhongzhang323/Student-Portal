package com.example.studentportal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A class for managing the connection to a database.
 */
public class DatabaseConnector {
    private static final String PROPERTIES_FILE = "src\\main\\resources\\db.properties"; // Path to the properties file
    private static String URL; // Database URL
    private static String USERNAME; // Database username
    private static String PASSWORD; // Database password
    private Connection connection; // Connection object

    // Static block to load database properties from the configuration file
    static {
        try (InputStream input = DatabaseConnector.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE);
                throw new IOException("Sorry, unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            URL = properties.getProperty("db.url"); // Get database URL from properties
            USERNAME = "kimho"; // Set database username to 'kimho'
            PASSWORD = properties.getProperty("db.password"); // Get database password from properties
        } catch (IOException ex) {
            System.err.println("IOException occurred: " + ex.getMessage());
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Establish the connection
                System.out.println("Connection established.");
            } catch (SQLException e) {
                System.err.println("Failed to connect to the database.");
                throw e; // Re-throw the exception after logging
            }
        }
        return connection; // Return the connection object
    }

    /**
     * Closes the current database connection if it is open.
     *
     * @throws SQLException if a database access error occurs
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Close the connection
        }
    }
}
