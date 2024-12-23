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
        loadAcademicSubjects();
        loadCoCurricularClubs();

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

        JLabel userLabel = new JLabel("User:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 1;
        panel.add(registerButton, gbc);

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

        JLabel userLabel = new JLabel("User:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordText, gbc);

        JLabel academicSubjectsDescription = new JLabel("Hold Ctrl (Cmd on Mac) to select multiple subjects:");
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(academicSubjectsDescription, gbc);

        JList<String> academicSubjectsList = new JList<>(academicSubjects.toArray(new String[0]));
        academicSubjectsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        academicSubjectsList.setVisibleRowCount(5);
        JScrollPane academicSubjectsScroll = new JScrollPane(academicSubjectsList);
        gbc.gridy = 4;
        panel.add(academicSubjectsScroll, gbc);

        JLabel coCurricularClubsDescription = new JLabel("Hold Ctrl (Cmd on Mac) to select multiple clubs:");
        gbc.gridy = 5;
        panel.add(coCurricularClubsDescription, gbc);

        JList<String> coCurricularClubsList = new JList<>(coCurricularClubs.toArray(new String[0]));
        coCurricularClubsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        coCurricularClubsList.setVisibleRowCount(5);
        JScrollPane coCurricularClubsScroll = new JScrollPane(coCurricularClubsList);
        gbc.gridy = 6;
        panel.add(coCurricularClubsScroll, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridy = 7;
        panel.add(registerButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridy = 8;
        panel.add(backButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());
                String confirmPassword = new String(confirmPasswordText.getPassword());
                List<String> selectedAcademicSubjects = academicSubjectsList.getSelectedValuesList();
                List<String> selectedCoCurricularClubs = coCurricularClubsList.getSelectedValuesList();

                if (password.equals(confirmPassword)) {
                    if (!selectedAcademicSubjects.isEmpty() && !selectedCoCurricularClubs.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Registration successful\nUser: " + user +
                                "\nAcademic Subjects: " + String.join(", ", selectedAcademicSubjects) +
                                "\nCo-Curricular Clubs: " + String.join(", ", selectedCoCurricularClubs));
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select at least one Academic Subject and one Co-Curricular Club.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                }
            }
        });

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

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        JTextArea dashboardContent = new JTextArea(
            "This is your dashboard. \nHere you can access your academic subjects, clubs, and other features."
        );
        dashboardContent.setFont(new Font("Arial", Font.PLAIN, 16));
        dashboardContent.setEditable(false);
        dashboardPanel.add(new JScrollPane(dashboardContent), BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
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

        dashboardPanel.add(logoutButton, BorderLayout.SOUTH);

        dashboardFrame.add(dashboardPanel);
        dashboardFrame.setVisible(true);
    }

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
