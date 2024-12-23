package com.example.studentportal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private static List<String> academicSubjects = new ArrayList<>();
    private static List<String> coCurricularClubs = new ArrayList<>();

    public static void main(String[] args) {
        loadAcademicSubjects();  // Load academic subjects from file
        loadCoCurricularClubs(); // Load co-curricular clubs from file

        // Create and display the login page
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, frame);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel, JFrame parentFrame) {
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
        
                // Simple hardcoded user authentication
                String predefinedUser = "admin"; // Hardcoded username
                String predefinedPassword = "password123"; // Hardcoded password
        
                if (user.equals(predefinedUser) && password.equals(predefinedPassword)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    showUserProfileDashboard(user); // Navigate to User Profile Dashboard
                    parentFrame.dispose(); // Close the login page
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                }
            }
        });

        // Handle register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterPage(parentFrame);
            }
        });
    }

    private static void showRegisterPage(JFrame parentFrame) {
        JFrame registerFrame = new JFrame("Register Page");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen

        JPanel registerPanel = new JPanel();
        registerFrame.add(registerPanel);
        placeRegisterComponents(registerPanel, registerFrame, parentFrame);

        registerFrame.setVisible(true);
    }

    private static void placeRegisterComponents(JPanel panel, JFrame registerFrame, JFrame parentFrame) {
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
                parentFrame.setVisible(true);
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
        academicSubjectsPanel.setLayout(new GridLayout(academicSubjects.size(), 1));
        ButtonGroup academicButtonGroup = new ButtonGroup();
        for (String subject : academicSubjects) {
            JRadioButton subjectButton = new JRadioButton(subject);
            academicButtonGroup.add(subjectButton);
            academicSubjectsPanel.add(subjectButton);
        }
        tabbedPane.addTab("Academic Subjects", new JScrollPane(academicSubjectsPanel));

        // Co-Curricular Clubs Tab
        JPanel coCurricularPanel = new JPanel();
        coCurricularPanel.setLayout(new GridLayout(coCurricularClubs.size(), 1));
        ButtonGroup coCurricularButtonGroup = new ButtonGroup();
        for (String club : coCurricularClubs) {
            JRadioButton clubButton = new JRadioButton(club);
            coCurricularButtonGroup.add(clubButton);
            coCurricularPanel.add(clubButton);
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
                placeComponents(loginPanel, loginFrame);
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

    // Load academic subjects from file
    private static void loadAcademicSubjects() {
        try (BufferedReader r = new BufferedReader(new FileReader("data/academicSubjects.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                academicSubjects.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading academic subjects file");
        }
    }

    // Load co-curricular clubs from file
    private static void loadCoCurricularClubs() {
        try (BufferedReader r = new BufferedReader(new FileReader("data/ClubSocieties.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                coCurricularClubs.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading co-curricular clubs file");
        }
    }
}
