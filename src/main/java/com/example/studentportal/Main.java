package com.example.studentportal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            // Initialize the database connection
            DatabaseConnector dbConnection = new DatabaseConnector();
            Connection conn = dbConnection.getConnection();

            if (conn != null) {
                UserDAO userDAO = new UserDAO(conn);
                
                // Call the signUp method
                signUp(scanner, userDAO);

                dbConnection.closeConnection();
            } else {
                System.out.println("Failed to establish a connection to the database.");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } finally {
            scanner.close(); // Close the scanner to avoid resource leaks
        }
    }

    private static void signUp(Scanner scanner, UserDAO userDAO) {
        System.out.println("=== User Sign-Up ===");

        // Prompt for user input
        String email;
        while (true) {
            System.out.print("Enter your email (format: MatricID@siswa.um.edu.my): ");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                break; // Exit loop if valid
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }

        // Extract student ID from email
        String studentId = email.substring(0, 8);
        int matricNumber = Integer.parseInt(studentId);

        // Verify that the matric number is the same as the student ID in the email
        System.out.print("Enter your matric number (must match the 8-digit student ID in the email): ");
        int inputMatricNumber = Integer.parseInt(scanner.nextLine());

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
                break; // Exit loop if valid
            } else {
                System.out.println("Password must be at least 8 characters long and contain at least one digit and one letter.");
            }
        }

        // Hash the password before storing it
        String hashedPassword = PasswordUtil.hashPassword(password);

        // Display academic subjects before prompting for input
        displayAcademicSubjects("src/main/java/com/example/studentportal/AcademicSubjects.txt");

        System.out.print("Enter your academic subjects (comma-separated): ");
        String subjectsInput = scanner.nextLine();
        List<String> academicSubjects = Arrays.asList(subjectsInput.split("\\s*,\\s*")); // Split by commas and trim spaces

        displayCoCurricularClubs("src/main/java/com/example/studentportal/ClubSocieties.txt");
        System.out.print("Enter your co-curricular clubs (comma-separated): ");
        String clubsInput = scanner.nextLine();
        List<String> coCurricularClubs = Arrays.asList(clubsInput.split("\\s*,\\s*")); // Split by commas and trim spaces

        // Create a new user object with hashed password
        User newUser = new User(email, matricNumber, hashedPassword, academicSubjects, coCurricularClubs);

        try {
            boolean signUpSuccess = userDAO.signUp(newUser);
            if (signUpSuccess) {
                System.out.println("Sign-Up Successful!");
            } else {
                System.out.println("Sign-Up Failed. Email may already be in use.");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during sign-up: " + e.getMessage());
        }
    }

    // Other methods remain unchanged...

    // Method to validate email format
    private static boolean isValidEmail(String email) {
        String emailRegex = "^\\d{8}@siswa\\.um\\.edu\\.my$";  // Regex for studentid(8digits)@siswa.um.edu.my
        return email.matches(emailRegex);
    }

    // Method to validate password strength
    private static boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }

    private static void displayAcademicSubjects(String filename) {
        List<String> subjects = readSubjectsFromFile(filename);

        // Display the subjects
        System.out.println("=== Available Academic Subjects ===");
        if (subjects.isEmpty()) {
            System.out.println("No subjects found.");
        } else {
            for (String subject : subjects) {
                System.out.println("- " + subject);
            }
        }
        System.out.println(); // Add a newline for better formatting
    }

    private static void displayCoCurricularClubs(String filename) {
        List<String> clubs = readSubjectsFromFile(filename);

        // Display the clubs
        System.out.println("=== Available Co-curricular Clubs ===");
        if (clubs.isEmpty()) {
            System.out.println("No clubs found.");
        } else {
            for (String club : clubs) {
                System.out.println("- " + club);
            }
        }
        System.out.println(); // Add a newline for better formatting
    }

    private static List<String> readSubjectsFromFile(String filename) {
        List<String> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(line.trim()); // Add the subject to the list and trim whitespace
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return items;
    }
}
