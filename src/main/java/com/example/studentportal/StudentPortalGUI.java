package com.example.studentportal;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StudentPortalGUI {
    private final JFrame frame;
    private final JPanel panel;
    private final JTextField matricNumberField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JButton subjectsButton, clubsButton, positionsButton, activitiesButton, logoutButton;
    private final JButton generateTranscriptButton;
    private final JTextArea resultArea;
    private final JPanel userPanel;
    private final ClubsAction clubsAction;

    public StudentPortalGUI() {
        frame = new JFrame("Student Portal");
        panel = new JPanel();
        panel.setLayout(new CardLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // Center the window

        // Create login panel with improved styling
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        loginPanel.setBackground(new Color(238, 238, 238));

        JLabel matricLabel = new JLabel("Matric Number:");
        matricLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(matricLabel, gbc);

        matricNumberField = new JTextField(20);
        matricNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        loginPanel.add(matricNumberField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginButton.setFocusable(false);
        loginPanel.add(loginButton, gbc);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);

        // Create result area with scroll pane
        resultArea = new JTextArea(12, 40);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Menu buttons for logged-in users
        subjectsButton = new JButton("View Enrolled Subjects");
        clubsButton = new JButton("View Clubs");
        positionsButton = new JButton("View Positions");
        activitiesButton = new JButton("View Activities");
        logoutButton = new JButton("Logout");

        generateTranscriptButton = new JButton("Generate Transcript");

        // Initialize ClubsAction and set its required fields
        clubsAction = new ClubsAction();
        clubsAction.setFrame(frame);
        clubsAction.setMatricNumberField(matricNumberField);
        clubsAction.setResultArea(resultArea);

        // Add action listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());
        subjectsButton.addActionListener(new SubjectsAction());
        clubsButton.addActionListener(clubsAction);
        positionsButton.addActionListener(new PositionsAction());
        activitiesButton.addActionListener(new ActivitiesAction());
        logoutButton.addActionListener(new LogoutAction());
        generateTranscriptButton.addActionListener(new GenerateTranscriptAction());

        // User panel with result area and buttons
        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBackground(new Color(240, 240, 240));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(subjectsButton);
        buttonPanel.add(clubsButton);
        buttonPanel.add(positionsButton);
        buttonPanel.add(activitiesButton);
        buttonPanel.add(generateTranscriptButton);
        buttonPanel.add(logoutButton);

        userPanel.add(resultScrollPane, BorderLayout.CENTER);
        userPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(loginPanel, "Login");
        panel.add(userPanel, "User");

        frame.add(panel);
        frame.setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (validateCredentials(matricNumber, password)) {
                    showUserPanel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid login credentials!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while validating credentials.");
            }
        }
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            String password = new String(passwordField.getPassword());
            String email = JOptionPane.showInputDialog("Enter your email:");

            if (matricNumber.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            try {
                registerUser(matricNumber, password, email);
                JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred during registration.");
            }
        }
    }

    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showLoginPanel();
        }
    }

    private class SubjectsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                resultArea.setText(getEnrolledSubjects(matricNumber));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching enrolled subjects.");
            }
        }
    }

    private class PositionsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                Map<String, java.util.List<String>> positions = getStudentPositions(matricNumber);
                StringBuilder result = new StringBuilder("Positions:\n");
                for (String club : positions.keySet()) {
                    result.append(club).append(": ").append(String.join(", ", positions.get(club))).append("\n");
                }
                resultArea.setText(result.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching positions.");
            }
        }
    }

    private class ActivitiesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                Map<String, java.util.List<String>> activitiesMap = getStudentActivities(matricNumber);
                StringBuilder result = new StringBuilder("Activities:\n");
                for (java.util.List<String> activities : activitiesMap.values()) {
                    for (String activity : activities) {
                        result.append(activity).append("\n");
                    }
                }
                resultArea.setText(result.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching activities.");
            }
        }
    }

    private class GenerateTranscriptAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                clubsAction.generateTranscript(matricNumber);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while generating the transcript.");
            }
        }
    }

    private void showLoginPanel() {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, "Login");
    }

    private void showUserPanel() {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, "User");
    }

    private boolean validateCredentials(String matricNumber, String password) throws Exception {
        String query = "SELECT * FROM users WHERE matric_number = ? AND password = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void registerUser(String matricNumber, String password, String email) throws SQLException, Exception {
        String query = "INSERT INTO users (matric_number, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
        }
    }

    private String getEnrolledSubjects(String matricNumber) throws Exception {
        String query = "SELECT s.subject_code, s.subject_name FROM academic_subjects s "
                     + "JOIN users u ON FIND_IN_SET(s.subject_code, u.academic_subjects) > 0 "
                     + "WHERE u.matric_number = ?";
        StringBuilder subjects = new StringBuilder();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subjects.append(rs.getString("subject_code"))
                            .append(" - ").append(rs.getString("subject_name"))
                            .append("\n");
                }
            }
        }
        return subjects.toString();
    }

    private Map<String, String> getStudentClubs(String matricNumber) throws SQLException, Exception {
        String query = "SELECT c.club_code, c.club_name FROM clubs c "
                     + "JOIN users u ON FIND_IN_SET(c.club_code, u.cocurricular_clubs) > 0 "
                     + "WHERE u.matric_number = ?";
        Map<String, String> studentClubs = new HashMap<>();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    studentClubs.put(rs.getString("club_code"), rs.getString("club_name"));
                }
            }
        }
        return studentClubs;
    }

    private Map<String, java.util.List<String>> getStudentPositions(String matricNumber) throws SQLException, Exception {
        String query = "SELECT society_position, uniform_body_position, sports_club_position "
                     + "FROM student_positions WHERE matric_number = ?";
        Map<String, java.util.List<String>> positions = new HashMap<>();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    addPositionToMap(positions, "Society", rs.getString("society_position"));
                    addPositionToMap(positions, "Uniform Body", rs.getString("uniform_body_position"));
                    addPositionToMap(positions, "Sports Club", rs.getString("sports_club_position"));
                }
            }
        }
        return positions;
    }

    private void addPositionToMap(Map<String, java.util.List<String>> map, String key, String value) {
        if (value != null && !value.isEmpty()) {
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
    }

    private Map<String, java.util.List<String>> getStudentActivities(String matricNumber) throws SQLException, Exception {
        String query = "SELECT al.club_code, al.activity_name, al.activity_level, al.achievement_level "
                     + "FROM activities_log al "
                     + "JOIN users u ON al.matric_number = u.matric_number "
                     + "WHERE u.matric_number = ?";
        Map<String, java.util.List<String>> activities = new HashMap<>();
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String activityInfo = String.format("Activity: %s, Level: %s, Achievement: %s",
                            rs.getString("activity_name"), rs.getString("activity_level"), rs.getString("achievement_level"));
                    activities.computeIfAbsent(rs.getString("club_code"), k -> new ArrayList<>()).add(activityInfo);
                }
            }
        }
        return activities;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentPortalGUI::new);
    }
}
