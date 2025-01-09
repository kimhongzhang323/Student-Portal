package com.example.studentportal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHelper {

    // Database connection details (URL, username, and password)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_info";  // URL of the database
    private static final String USER = "root";  // Database username
    private static final String PASS = "";  // Database password        

    // Method to establish a connection to the database
    public static Connection getConnection() throws Exception {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish the connection to the database
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            // If an exception occurs while connecting to the database, throw a new exception with a message
            throw new Exception("Error getting database connection", e);
        }
    }

    // Main method to test the connection and retrieve database metadata
    public static void main(String[] args) {
        try {
            // Get a connection to the database
            Connection connection = getConnection();
            System.out.println("Connected to the database!");  // Print confirmation message

            // Get the database metadata to retrieve details about the database
            DatabaseMetaData metaData = connection.getMetaData();

            // Retrieve the list of tables in the database
            ResultSet resultSet = metaData.getTables(null, null, "%", new String[] {"TABLE"});

            // Process the result set and print all table names
            System.out.println("Tables in the database:");
            while (resultSet.next()) {
                // Get the name of each table from the result set and print it
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName);
            }

            // Close the result set and connection to release resources
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            // Handle any exceptions that occur and print the error message
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
