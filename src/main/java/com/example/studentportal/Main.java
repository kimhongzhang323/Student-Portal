package com.example.studentportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
        academicSubjectsPanel.setLayout(new GridLayout(5, 1));  // Adjust based on the number of subjects you have
        for (int i = 1; i <= 5; i++) {  // Hardcoded for example, replace with actual data
            academicSubjectsPanel.add(new JLabel("Subject " + i));
        }
        tabbedPane.addTab("Academic Subjects", new JScrollPane(academicSubjectsPanel));

        // Co-Curricular Clubs Tab
        JPanel coCurricularPanel = new JPanel();
        coCurricularPanel.setLayout(new GridLayout(5, 1));  // Adjust based on the number of clubs
        for (int i = 1; i <= 5; i++) {  // Hardcoded for example, replace with actual data
            coCurricularPanel.add(new JLabel("Club " + i));
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
    
                // Debug: Print the current line
                System.out.println("Reading line: " + line);
    
                // Check for username line (ends with "@student.fop")
                if (line.endsWith("@student.fop")) {
                    currentUsername = line;  // Store the full username (with domain)
                    System.out.println("Found username: " + currentUsername);  // Debug
                }
    
                // Check for password line (starts with 'pw-')
                if (line.startsWith("pw-")) {
                    currentPassword = line.split("pw-")[1].trim();  // Extract the password after 'pw-'
                    System.out.println("Found password: " + currentPassword);  // Debug
                }
    
                // If both username and password are found, compare them
                if (currentUsername != null && currentPassword != null) {
                    // Debug: Show the credentials being compared
                    System.out.println("Comparing input: ");
                    System.out.println("Username: " + username + " with " + currentUsername);
                    System.out.println("Password: " + password + " with " + currentPassword);
    
                    // Check if the input username and password match the current ones
                    if (username.trim().equals(currentUsername.trim()) && password.trim().equals(currentPassword.trim())) {
                        System.out.println("Credentials match. Login successful!");  // Debug
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
    
        System.out.println("Invalid credentials. Please check your username and password.");  // Debug
        return false;  // Invalid credentials if we reach here
    }
     
}
