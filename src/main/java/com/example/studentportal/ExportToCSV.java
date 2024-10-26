package com.example.studentportal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class for exporting data from database tables to CSV files.
 */
public class ExportToCSV {

    /**
     * The main method to execute the export process.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        DatabaseConnector connector = new DatabaseConnector(); // Create a database connector

        try (Connection connection = connector.getConnection()) { // Establish database connection
            System.out.println("Connected to the database.");

            // Export each table to a CSV file
            exportTableToCSV(connection, "achievements", "achievements.csv");
            exportTableToCSV(connection, "activity_levels", "activity_levels.csv");
            exportTableToCSV(connection, "basic_info", "basic_info.csv");
            exportTableToCSV(connection, "positions", "positions.csv");
            exportTableToCSV(connection, "users", "users.csv");

            System.out.println("Data exported successfully.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage()); // Log connection errors
        }
    }

    /**
     * Exports a specified database table to a CSV file.
     *
     * @param connection the database connection
     * @param tableName  the name of the table to export
     * @param fileName   the name of the CSV file to create
     */
    public static void exportTableToCSV(Connection connection, String tableName, String fileName) {
        String query = "SELECT * FROM " + tableName; // SQL query to select all data from the table

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            // Get the number of columns
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Write the CSV header
            for (int i = 1; i <= columnCount; i++) {
                writer.write(resultSet.getMetaData().getColumnName(i)); // Write column name
                if (i < columnCount) writer.write(","); // Separate columns with a comma
            }
            writer.newLine(); // New line after header

            // Write the data rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(resultSet.getString(i)); // Write the value
                    if (i < columnCount) writer.write(","); // Separate columns with a comma
                }
                writer.newLine(); // New line after each row
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error exporting table " + tableName + ": " + e.getMessage()); // Log errors
        }
    }
}
