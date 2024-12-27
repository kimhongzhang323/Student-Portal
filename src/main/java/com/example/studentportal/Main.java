package com.example.studentportal;

import javax.swing.*;
import com.example.studentportal.CoCurriculum.Activity;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main {
    private static JFrame mainFrame;
    private static JPanel mainPanel;
    
    public static void main(String[] args) {
        // Create and display the login page
        mainFrame = new JFrame("Login Page");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen
        
        mainPanel = new JPanel();
        mainFrame.add(mainPanel);
        placeComponents(mainPanel);
        
        mainFrame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add User label and text field
        JLabel userLabel = new JLabel("User:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        // Add Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);

        // Add Login and Register buttons
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        panel.add(registerButton, gbc);

        // Handle login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());

                // Validate username and password from the file
                if (validateCredentials(user, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    showUserProfileDashboard(user); // Navigate to User Profile Dashboard
                    mainFrame.dispose(); // Close the login page
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                }
            }
        });

        // Handle register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterPage();
            }
        });
    }

    private static void showRegisterPage() {
        JFrame registerFrame = new JFrame("Register Page");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen

        JPanel registerPanel = new JPanel();
        registerFrame.add(registerPanel);
        placeRegisterComponents(registerPanel, registerFrame);

        registerFrame.setVisible(true);
    }

    private static void placeRegisterComponents(JPanel panel, JFrame registerFrame) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add User label and text field
        JLabel userLabel = new JLabel("User:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        // Add Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);

        // Add Confirm Password label and field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordText, gbc);

        // Add Register button and Back button
        JButton registerButton = new JButton("Register");
        gbc.gridy = 3;
        panel.add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 4;
        panel.add(backButton, gbc);

        // Handle register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());
                String confirmPassword = new String(confirmPasswordText.getPassword());

                if (password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(null, "Registration successful for user: " + user);
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                }
            }
        });

        // Handle back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.dispose();
                mainFrame.setVisible(true);
            }
        });
    }

    private static void showUserProfileDashboard(String username) {
        JFrame dashboardFrame = new JFrame("User Profile Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new BorderLayout());

        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(0, 51, 102));
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create TabbedPane for different sections
        JTabbedPane tabbedPane = new JTabbedPane();

        // Academic Subjects Tab
        JPanel academicSubjectsPanel = new JPanel();
        academicSubjectsPanel.setLayout(new GridLayout(0, 1));  // Adjust based on the number of subjects

        // Get the enrolled subjects for the student
        String enrolledSubjects = getEnrolledSubjects(username);

        // Display the enrolled subjects
        if (enrolledSubjects != null && !enrolledSubjects.isEmpty()) {
            String[] subjectCodes = enrolledSubjects.split(",");
            for (String subjectCode : subjectCodes) {
                String subjectName = getSubjectName(subjectCode.trim());
                if (subjectName != null) {
                    academicSubjectsPanel.add(createStyledLabel(subjectName));
                } else {
                    academicSubjectsPanel.add(createStyledLabel("Unknown Subject Code: " + subjectCode));
                }
            }
        } else {
            academicSubjectsPanel.add(createStyledLabel("No subjects enrolled"));
        }
        tabbedPane.addTab("Academic Subjects", new JScrollPane(academicSubjectsPanel));

        // Co-Curricular Clubs Tab
        JPanel coCurricularPanel = new JPanel();
        coCurricularPanel.setLayout(new GridLayout(0, 1));  // Adjust based on the number of clubs
        
        // Retrieve Co-curricular Data (Clubs and Activities) for the logged-in student
        Map<String, String> studentClubs = getStudentClubs(username);  // Retrieve student's clubs
        Map<String, List<String>> studentPositions = getStudentPositions(username);  // Retrieve student's positions in clubs
        List<Activity> activities = getStudentActivities(username);  // Retrieve student's activities

        // Display the co-curricular clubs the student is involved in
        if (!studentClubs.isEmpty()) {
            for (String clubCode : studentClubs.keySet()) {
                String clubName = studentClubs.get(clubCode);
                String position = String.valueOf(studentPositions.get(clubCode));
                String activityDetails = getActivityDetails(activities, clubCode);

                // Add the club information to the co-curricular panel
                coCurricularPanel.add(createStyledLabel("Club: " + clubName));
                coCurricularPanel.add(createStyledLabel("Position: " + position));
                coCurricularPanel.add(createStyledLabel("Activities: " + activityDetails));
                coCurricularPanel.add(new JSeparator());
            }
        } else {
            coCurricularPanel.add(createStyledLabel("No co-curricular clubs found for this student."));
        }

        tabbedPane.addTab("Co-Curricular Clubs", new JScrollPane(coCurricularPanel));

        // Add TabbedPane to the main dashboard
        dashboardPanel.add(tabbedPane, BorderLayout.CENTER);

        // Create a logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(0, 51, 102));
        logoutButton.setPreferredSize(new Dimension(150, 40));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose(); // Close the dashboard
                JFrame loginFrame = new JFrame("Login Page");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                JPanel loginPanel = new JPanel();
                loginFrame.add(loginPanel);
                placeComponents(loginPanel);
                loginFrame.setVisible(true);
            }
        });

        // Add the logout button at the bottom of the dashboard
        JPanel logoutPanel = new JPanel();
        logoutPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutButton);
        dashboardPanel.add(logoutPanel, BorderLayout.SOUTH);

        dashboardFrame.add(dashboardPanel);
        dashboardFrame.setVisible(true);
    }

    // Helper method to create a styled label
    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(new Color(0, 51, 102));
        return label;
    }

    // Retrieve clubs the student is part of
    private static Map<String, String> getStudentClubs(String studentMatricNumber) {
        Map<String, String> studentClubs = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/ClubSocieties.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String clubCode = parts[0].trim();
                String clubName = parts[1].trim();
                if (isClubJoinedByStudent(studentMatricNumber, clubCode)) {
                    studentClubs.put(clubCode, clubName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentClubs;
    }

    // Simulate checking if a student has joined a club by reading it from a database or file
    private static boolean isClubJoinedByStudent(String studentMatricNumber, String clubCode) {
        // In real-world, we would look up the student in a database or file to see if they joined this club
        return true; // Simulate that the student has joined all clubs in this case
    }

    // Retrieve student positions from StudentPositions.txt
    private static Map<String, List<String>> getStudentPositions(String studentMatricNumber) {
        Map<String, List<String>> studentPositions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/StudentPositions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Clean up leading/trailing spaces
                if (line.isEmpty()) {
                    continue;  // Skip empty lines
                }
    
                // Split the line by commas
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;  // Skip malformed lines (less than 2 parts)
                }
    
                String matricNumber = parts[0].trim();  // First part is the student matric number
                if (matricNumber.equals(studentMatricNumber)) {
                    // Iterate through the roles for the various organizations
                    for (int i = 1; i < parts.length; i++) {
                        String roleAndClub = parts[i].trim();
                        studentPositions.computeIfAbsent(matricNumber, k -> new ArrayList<>()).add(roleAndClub);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle file read errors
        }
    
        return studentPositions;
    }

    // Retrieve student activities from ActivitiesLog.txt
    private static List<Activity> getStudentActivities(String studentMatricNumber) {
        List<Activity> activities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/ActivitiesLog.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String matricNumber = parts[0].trim();
                if (matricNumber.equals(studentMatricNumber)) {
                    String activity = parts[1].trim();
                    String clubCode = parts[2].trim();
                    activities.add(new Activity(activity, clubCode, "default")); // Replace "default" with the appropriate third argument
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activities;
    }

    // Helper method to get activity details for the given club
    private static String getActivityDetails(List<Activity> activities, String clubCode) {
        StringBuilder activityDetails = new StringBuilder();
        for (Activity activity : activities) {
            if (activity.getClubCode().equals(clubCode)) {
                activityDetails.append(activity.getActivityLevel()).append(" | ");
            }
        }
        return activityDetails.length() > 0 ? activityDetails.substring(0, activityDetails.length() - 3) : "No activities recorded";
    }
    
    private static boolean validateCredentials(String username, String password) {
        // Specify the path to your user data file (adjust path if needed)
        String filePath = "C://Users//kimho//OneDrive//Documents//STUDY//UM//Y1S1//UM-WIX1002-main//Student-Portal//data//UserData.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentUsername = null;
            String currentPassword = null;
    
            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Remove leading and trailing spaces from each line
                line = line.trim();
    
                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }
    
                // Check for username line (ends with "@student.fop")
                if (line.endsWith("@student.fop")) {
                    currentUsername = line;  // Store the full username (with domain)
                }
    
                // Check for password line (starts with 'pw-')
                if (line.startsWith("pw-")) {
                    currentPassword = line.split("pw-")[1].trim();  // Extract the password after 'pw-'
                }
    
                // If both username and password are found, compare them
                if (currentUsername != null && currentPassword != null) {
                    // Check if the input username and password match the current ones
                    if (username.trim().equals(currentUsername.trim()) && password.trim().equals(currentPassword.trim())) {
                        return true;  // Valid credentials
                    }
    
                    // Reset for next user if no match
                    currentUsername = null;
                    currentPassword = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle any IO exceptions (e.g., file not found)
        }
    
        return false;  // Invalid credentials if we reach here
    }

    private static String getEnrolledSubjects(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C://Users//kimho//OneDrive//Documents//STUDY//UM//Y1S1//UM-WIX1002-main//Student-Portal//data//UserData.txt"))) {
            String line;
            boolean usernameFound = false;
            String enrolledSubjects = null;
    
            while ((line = reader.readLine()) != null) {
                line = line.trim();  // Trim to remove leading/trailing spaces
                if (line.isEmpty()) {
                    continue;
                }
    
                System.out.println("Reading line: " + line); // Debug print
    
                // Check for username line (ends with "@student.fop")
                if (line.endsWith("@student.fop") && line.contains(username)) {
                    usernameFound = true;  // Username match found
                    System.out.println("Found username: " + line); // Debug print
                }
    
                // If we found the username, try to read the next line for subjects
                if (usernameFound) {
                    // The line containing subject codes (numbers) directly without 'Subjects:'
                    if (line.matches("[0-9,]+")) {  // If the line contains only numbers and commas
                        enrolledSubjects = line.trim();
                        System.out.println("Enrolled Subjects: " + enrolledSubjects); // Debug print
                        break;
                    }
                }
            }
    
            return enrolledSubjects;
        } catch (IOException e) {
            e.printStackTrace();  // Handle file reading errors
        }
    
        return null;  // If no enrolled subjects found
    }
    
    

    // Helper method to get the subject name based on subject code from AcademicSubjects.txt
    private static String getSubjectName(String subjectCode) {
        try (BufferedReader reader = new BufferedReader(new FileReader("C://Users//kimho//OneDrive//Documents//STUDY//UM//Y1S1//UM-WIX1002-main//Student-Portal//data//AcademicSubjects.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] subjectData = line.split(",");
                if (subjectData.length == 2 && subjectData[0].trim().equals(subjectCode)) {
                    return subjectData[1].trim();  // Return the subject name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Handle file reading errors
        }

        return null;  // If subject code is not found, return null
    }


}
