package com.example.studentportal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExportToCSV {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_info";
    private static final String USERNAME = "kimho";
    private static final String PASSWORD = "buku3b3B";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected to the database.");

            // Export each table
            exportTableToCSV(connection, "achievements", "achievements.csv");
            exportTableToCSV(connection, "activity_levels", "activity_levels.csv");
            exportTableToCSV(connection, "basic_info", "basic_info.csv");
            exportTableToCSV(connection, "positions", "positions.csv");
            exportTableToCSV(connection, "users", "users.csv");

            System.out.println("Data exported successfully.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    private static void exportTableToCSV(Connection connection, String tableName, String fileName) {
        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            // Get the number of columns
            int columnCount = resultSet.getMetaData().getColumnCount();

            // Write header
            for (int i = 1; i <= columnCount; i++) {
                writer.write(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) writer.write(","); // Separate columns with a comma
            }
            writer.newLine(); // New line after header

            // Write data
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.write(resultSet.getString(i));
                    if (i < columnCount) writer.write(","); // Separate columns with a comma
                }
                writer.newLine(); // New line after each row
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error exporting table " + tableName + ": " + e.getMessage());
        }
    }
}

