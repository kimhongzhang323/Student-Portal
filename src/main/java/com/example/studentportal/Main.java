package com.example.studentportal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Main class is the entry point for the student portal application,
 * handling user sign-up and integrating the CSVAutoUpdater functionality.
 */
public class Main {
    private static Instant lastUpdated = Instant.now(); // Tracks the last update time for the CSV file

    /**
     * The main method initializes the application, sets up a database connection,
     * and schedules CSV updates.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // Scheduler for periodic tasks
        DatabaseConnector connector = new DatabaseConnector(); // Database connector instance
        Connection conn = null; // Database connection
        Scanner scanner = new Scanner(System.in); // Scanner for user input
    
        try {
            conn = connector.getConnection(); // Attempt to establish a database connection
            if (conn != null) {
                UserDAO userDAO = new UserDAO(conn); // Initialize UserDAO with the open connection
                signUp(scanner, userDAO); // Perform user sign-up
            } else {
                System.out.println("Failed to establish a connection to the database.");
            }
    
            // Schedule CSV update task to run periodically
            Runnable csvUpdateTask = () -> {
                try (Connection connection = connector.getConnection()) { // Get a fresh connection for each update
                    if (connection != null) {
                        Instant dbLastModified = getLastModifiedFromDB(connection);
                        if (dbLastModified.isAfter(lastUpdated)) {
                            ExportToCSV.exportTableToCSV(connection, "users", "users.csv");
                            lastUpdated = dbLastModified; // Update last updated timestamp
                            System.out.println("CSV updated successfully.");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error during CSV update check: " + e.getMessage());
                }
            };
    
            // Run CSV update task every minute
            scheduler.scheduleAtFixedRate(csvUpdateTask, 0, 1, TimeUnit.MINUTES);
    
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner to prevent resource leaks
            scheduler.shutdown(); // Initiate shutdown of the scheduler
            try {
                if (!scheduler.awaitTermination(1, TimeUnit.MINUTES)) { // Wait for existing tasks to terminate
                    scheduler.shutdownNow(); // Forcefully shutdown if tasks do not complete in time
                    System.out.println("Scheduler forcefully terminated.");
                }
            } catch (InterruptedException e) {
                System.err.println("Scheduler termination interrupted: " + e.getMessage());
            }
            
            // Close the main connection if it’s still open
            if (conn != null) {
                try {
                    conn.close(); // Close the database connection
                } catch (SQLException ex) {
                    System.err.println("Failed to close main connection: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Handles user sign-up process, including email and password validation,
     * and storing user details in the database.
     *
     * @param scanner   the Scanner for user input
     * @param userDAO   the UserDAO for database operations
     */
    private static void signUp(Scanner scanner, UserDAO userDAO) {
        System.out.println("=== User Sign-Up ===");

        String email;
        while (true) {
            System.out.print("Enter your email (format: MatricID@siswa.um.edu.my): ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }

        String studentId = email.substring(0, 8); // Extract student ID from email
        int matricNumber = Integer.parseInt(studentId); // Convert student ID to integer

        System.out.print("Enter your matric number (must match the 8-digit student ID in the email): ");
        int inputMatricNumber = Integer.parseInt(scanner.nextLine());

        // Validate matric number against extracted student ID
        while (inputMatricNumber != matricNumber) {
            System.out.println("Matric number does not match the student ID in the email. Please try again.");
            System.out.print("Enter your matric number: ");
            inputMatricNumber = Integer.parseInt(scanner.nextLine());
        }

        String password;
        while (true) {
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                break;
            } else {
                System.out.println("Password must be at least 8 characters long and contain at least one digit and one letter.");
            }
        }

        byte[] salt = PasswordHashing.generateSalt(); // Generate salt for password hashing
        String hashedPassword = null;

        try {
            hashedPassword = PasswordHashing.hashPassword(password, salt); // Hash the password with salt
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return; // Exit if password hashing fails
        }

        // Display academic subjects and collect user input
        displayAcademicSubjects("AcademicSubjects.txt");
        System.out.print("Enter your academic subjects (comma-separated): ");
        String subjectsInput = scanner.nextLine();
        List<String> academicSubjects = Arrays.asList(subjectsInput.split("\\s*,\\s*")); // Split input by commas

        // Display co-curricular clubs and collect user input
        displayCoCurricularClubs("ClubSocieties.txt");
        System.out.print("Enter your co-curricular clubs (comma-separated): ");
        String clubsInput = scanner.nextLine();
        List<String> coCurricularClubs = Arrays.asList(clubsInput.split("\\s*,\\s*")); // Split input by commas

        User newUser = new User(email, matricNumber, hashedPassword, academicSubjects, coCurricularClubs, salt); // Create new User object

        try {
            boolean signUpSuccess = userDAO.signUp(newUser); // Attempt to sign up the user
            if (signUpSuccess) {
                System.out.println("Sign-Up Successful!");
            } else {
                System.out.println("Sign-Up Failed. Email may already be in use.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during sign-up: " + e.getMessage());
        }
    }

    /**
     * Validates the email format for user sign-up.
     *
     * @param email the email to validate
     * @return true if the email format is valid, false otherwise
     */
    private static boolean isValidEmail(String email) {
        String emailRegex = "^\\d{8}@siswa\\.um\\.edu\\.my$"; // Regex for validating the email format
        return email.matches(emailRegex);
    }

    /**
     * Validates the password based on specific criteria.
     *
     * @param password the password to validate
     * @return true if the password meets the criteria, false otherwise
     */
    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*"); // Validate password criteria
    }

    /**
     * Displays available academic subjects from a file.
     *
     * @param filename the name of the file containing subjects
     */
    private static void displayAcademicSubjects(String filename) {
        List<String> subjects = readSubjectsFromFile(filename); // Read subjects from the specified file
        System.out.println("=== Available Academic Subjects ===");
        if (subjects.isEmpty()) {
            System.out.println("No subjects found.");
        } else {
            for (String subject : subjects) {
                System.out.println("- " + subject); // Print each subject
            }
        }
    }

    /**
     * Displays available co-curricular clubs from a file.
     *
     * @param filename the name of the file containing clubs
     */
    private static void displayCoCurricularClubs(String filename) {
        List<String> clubs = readSubjectsFromFile(filename); // Read clubs from the specified file
        System.out.println("=== Available Co-curricular Clubs ===");
        if (clubs.isEmpty()) {
            System.out.println("No clubs found.");
        } else {
            for (String club : clubs) {
                System.out.println("- " + club); // Print each club
            }
        }
    }

    /**
     * Reads subjects or clubs from a specified file.
     *
     * @param filename the name of the file to read
     * @return a list of items read from the file
     */
    private static List<String> readSubjectsFromFile(String filename) {
        List<String> items = new ArrayList<>(); // List to hold items
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File not found: " + filename); // Notify if file does not exist
            return items;
        }

        // Read items from the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(line.trim()); // Add each trimmed line to the list
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return items; // Return the list of items
    }

    /**
     * Placeholder method for retrieving the last modified timestamp from the database.
     *
     * @param connection the database connection
     * @return the current timestamp (replace with actual DB retrieval logic)
     * @throws SQLException if a database access error occurs
     */
    private static Instant getLastModifiedFromDB(Connection connection) throws SQLException {
        return Instant.now(); // Replace with actual DB modification timestamp retrieval
    }
}
