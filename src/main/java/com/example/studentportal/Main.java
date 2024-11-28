package com.example.studentportal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Periodic timestamp update (not critical for function, but kept for completeness)
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Instant.now();
            }
        }, 0, 3600 * 1000); // 1 hour

        System.out.println("-------------------------");
        System.out.println("Welcome to the Student Portal!");
        System.out.println("-------------------------");

        while (true) {
            System.out.println("Please select an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int option = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.println("Enter your email:");
                    String loginEmail = sc.nextLine();
                    System.out.println("Enter your password:");
                    String loginPassword = sc.nextLine();
                    if (StudentPortal.login(loginEmail, loginPassword)) {
                        System.out.println("Login successful!");
                        userDashboard(sc, loginEmail);
                    } else {
                        System.out.println("Invalid email or password.");
                    }
                    break;

                case 2:
                    StudentPortal.signUp(sc);
                    break;

                case 3:
                    System.out.println("Exiting Student Portal...");
                    sc.close();
                    timer.cancel();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }

    public static void userDashboard(Scanner scanner, String userEmail) {
        while (true) {
            System.out.println("\n=== User Dashboard ===");
            System.out.println("Welcome, " + userEmail);
            System.out.println("1. View Profile");
            System.out.println("2. My Academic");
            System.out.println("3. My Co-Curricular");
            System.out.println("4. Logout");
            System.out.print("Select an option: ");

            int dashboardOption = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (dashboardOption) {

                case 2:
                    StudentPortal.academicPage();
                    break;

                case 3:
                    System.out.println("Displaying co-curricular information for: " + userEmail);
                    // Placeholder for co-curricular logic
                    break;

                case 4:
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }
}


class StudentPortal {

    public static void signUp(Scanner scanner) {
        System.out.println("=== User Sign-Up ===");

        String email;
        int matricNumber;

        do {
            System.out.println("Enter your Matric Number (only numbers):");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid numeric Matric Number:");
                scanner.next();
            }
            matricNumber = scanner.nextInt();
            scanner.nextLine();

            email = matricNumber + "@siswa.um.edu.my";

            if (!email.matches("\\d+@siswa\\.um\\.edu\\.my")) {
                System.out.println("Generated email is invalid. Please try again.");
            } else {
                break; 
            }
        } while (true);

        System.out.println("Enter a password:");
        String password = scanner.nextLine();

        System.out.println("Confirm your password:");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try signing up again.");
            return; 
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("UserData.txt", true))) {
            writer.write(email + "," + matricNumber + "," + password);
            writer.newLine();
            System.out.println("Sign-up successful! Your email is: " + email);
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public static boolean login(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(email) && parts[2].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found. Please register first.");
        } catch (IOException e) {
            System.out.println("Error reading user data file: " + e.getMessage());
        }
        return false;
    }

    public static void academicPage() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== Academic Information ===");
            System.out.println("1. View Subjects");
            System.out.println("2. Back to Dashboard");
        
            int academicOption = sc.nextInt();
            sc.nextLine(); // Consume newline
        
            switch (academicOption) {
                case 1:
                    System.out.println("Displaying subjects...");
                    displaySubjects();
                    break;
                case 2:
                    return; // Return to the previous menu or dashboard
                default:
                    System.out.println("Invalid option. Please select a valid option.");
            }
        }
    }
    
    private static void displaySubjects() {
        Map<String, String> subjects = new TreeMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("AcademicSubjects.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String code = parts[0].trim();
                    String subject = parts[1].trim();
                    subjects.put(subject, code);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading academic subjects file: " + e.getMessage());
            return;
        }
    
        System.out.println("Enrolled Subjects :");
        System.out.println("==================================");
        for (Map.Entry<String, String> entry : subjects.entrySet()) {
            System.out.println(entry.getValue() + " - " + entry.getKey());
        }

    }
}