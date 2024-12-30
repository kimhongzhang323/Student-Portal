package com.example.studentportal;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
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
        frame.setSize(600, 400);

        // Create login panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Matric Number:"));
        matricNumberField = new JTextField();
        loginPanel.add(matricNumberField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        registerButton = new JButton("Register");
        loginPanel.add(registerButton);

        // Create result area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);

        // Create menu buttons for logged-in users
        subjectsButton = new JButton("View Enrolled Subjects");
        clubsButton = new JButton("View Clubs");
        positionsButton = new JButton("View Positions");
        activitiesButton = new JButton("View Activities");
        logoutButton = new JButton("Logout");

        // Button for generating transcript
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

        // Set up main panel and add components
        panel.add(loginPanel, "Login");

        userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(subjectsButton);
        buttonPanel.add(clubsButton);
        buttonPanel.add(positionsButton);
        buttonPanel.add(activitiesButton);
        buttonPanel.add(generateTranscriptButton);
        buttonPanel.add(logoutButton);
        userPanel.add(buttonPanel, BorderLayout.SOUTH);

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
                Map<String, java.util.List<String>> positions = StudentPortalGUI.this.getStudentPositions(matricNumber);
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

    private boolean validateCredentials(String matricNumber, String password) throws SQLException, Exception {
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

    private String getEnrolledSubjects(String matricNumber) throws SQLException, Exception {
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
        return subjects.length() > 0 ? subjects.toString() : "No enrolled subjects found.";
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
