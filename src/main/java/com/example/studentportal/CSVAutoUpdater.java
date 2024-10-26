package com.example.studentportal;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The CSVAutoUpdater class is responsible for automatically updating a CSV file
 * when changes are detected in the database. It uses a scheduled task to periodically
 * check for updates and refreshes the CSV file if new changes are detected.
 */
public class CSVAutoUpdater {

    private static Instant lastUpdated = Instant.now(); // Tracks the last update time for the CSV file

    /**
     * The main method starts the scheduled task to check for database updates and refresh the CSV.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // Scheduler for periodic checks
        DatabaseConnector connector = new DatabaseConnector(); // Database connector instance

        // Define the task to check for updates and export data to CSV
        Runnable task = () -> {
            try (Connection connection = connector.getConnection()) {
                // Retrieve the last modified time from the database
                Instant dbLastModified = getLastModifiedFromDB(connection);
                
                // If the database has changes after the last CSV update, export the table to CSV
                if (dbLastModified.isAfter(lastUpdated)) {
                    ExportToCSV.exportTableToCSV(connection, "users", "users.csv");
                    lastUpdated = dbLastModified; // Update the lastUpdated time
                    System.out.println("CSV updated successfully.");
                }
            } catch (SQLException e) {
                System.err.println("Error during CSV update check: " + e.getMessage()); // Log SQL exceptions
            }
        };

        // Schedule the task to run every minute, starting immediately
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Retrieves the last modified timestamp of the relevant data in the database.
     * 
     * This method should be customized to retrieve the actual last modification time
     * from the database by querying a timestamp column or metadata. 
     * 
     * @param connection the database connection
     * @return the timestamp of the last modification
     * @throws SQLException if a database access error occurs
     */
    private static Instant getLastModifiedFromDB(Connection connection) throws SQLException {
        // Placeholder implementation; replace with SQL logic to get actual last modification time
        return Instant.now();
    }
}
