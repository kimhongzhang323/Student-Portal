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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private JFrame frame;
    private JPanel panel;
    private JTextField matric_numberField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JButton subjectsButton, clubsButton, positionsButton, activitiesButton, logoutButton;
    private JTextArea resultArea;

    public StudentPortalGUI() {
        frame = new JFrame("Student Portal");
        panel = new JPanel();
        panel.setLayout(new CardLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.add(new JLabel("matric_number:"));
        matric_numberField = new JTextField();
        loginPanel.add(matric_numberField);
        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginPanel.add(loginButton);
        registerButton = new JButton("Register");
        loginPanel.add(registerButton);

        // Create result area
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        // Create menu buttons for logged-in users
        subjectsButton = new JButton("View Enrolled Subjects");
        clubsButton = new JButton("View Clubs");
        positionsButton = new JButton("View Positions");
        activitiesButton = new JButton("View Activities");
        logoutButton = new JButton("Logout");

        // Adding listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());
        subjectsButton.addActionListener(new SubjectsAction());
        clubsButton.addActionListener(new ClubsAction());
        positionsButton.addActionListener(new PositionsAction());
        activitiesButton.addActionListener(new ActivitiesAction());
        logoutButton.addActionListener(new LogoutAction());

        // Set up main panel and add components
        panel.add(loginPanel, "Login");

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(subjectsButton);
        buttonPanel.add(clubsButton);
        buttonPanel.add(positionsButton);
        buttonPanel.add(activitiesButton);
        buttonPanel.add(logoutButton);
        userPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(userPanel, "User");

        frame.add(panel);
        frame.setVisible(true);
    }

    // Action listener for Login
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            String password = new String(passwordField.getPassword());

            try {
                if (validateCredentials(matric_number, password)) {
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

    // Action listener for Register
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            String password = new String(passwordField.getPassword());
            String email = JOptionPane.showInputDialog("Enter your email:");

            if (matric_number.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                return;
            }

            try {
                registerUser(matric_number, password, email);
                JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred during registration.");
            }
        }
    }

    // Action listener for Logout
    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showLoginPanel();
        }
    }

    // Action listener for View Enrolled Subjects
    private class SubjectsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            try {
                resultArea.setText(getEnrolledSubjects(matric_number));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching enrolled subjects.");
            }
        }
    }

    // Action listener for View Clubs
    private class ClubsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            try {
                Map<String, String> clubs = getStudentClubs(matric_number);
                StringBuilder result = new StringBuilder("Clubs:\n");
                for (String code : clubs.keySet()) {
                    result.append(code).append(": ").append(clubs.get(code)).append("\n");
                }
                resultArea.setText(result.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching clubs.");
            }
        }
    }

    // Action listener for View Positions
    private class PositionsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            try {
                Map<String, List<String>> positions = getStudentPositions(matric_number);
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

    // Action listener for View Activities
    private class ActivitiesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matric_number = matric_numberField.getText();
            try {
                Map<String, List<String>> activitiesMap = getStudentActivities(matric_number);
                StringBuilder result = new StringBuilder("Activities:\n");
                for (List<String> activities : activitiesMap.values()) {
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

    private void showLoginPanel() {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, "Login");
    }

    private void showUserPanel() {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, "User");
    }

    // Database Interaction Methods

    private boolean validateCredentials(String matric_number, String password) throws SQLException, Exception {
        String query = "SELECT * FROM users WHERE matric_number = ? AND password = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matric_number);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void registerUser(String matric_number, String password, String email) throws SQLException, Exception {
        String query = "INSERT INTO users (matric_number, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matric_number);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getEnrolledSubjects(String matric_number) throws Exception {
        // Update query to reference the correct field (assuming academic_subjects contains comma-separated subject codes)
        String query = "SELECT s.subject_code, s.subject_name FROM academic_subjects s "
                     + "JOIN users u ON FIND_IN_SET(s.subject_code, u.academic_subjects) > 0 "
                     + "WHERE u.matric_number = ?";
    
        StringBuilder subjects = new StringBuilder();
    
        try (Connection conn = DBHelper.getConnection(); // Assuming DBHelper is correctly set up
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set matric_number parameter in the query
            stmt.setString(1, matric_number);
    
            try (ResultSet rs = stmt.executeQuery()) {
                // Process result set
                while (rs.next()) {
                    String subjectCode = rs.getString("subject_code");
                    String subjectName = rs.getString("subject_name");
                    subjects.append(subjectCode).append(" - ").append(subjectName).append("\n");
                }
            }
    
            // If no subjects were found, append a message
            if (subjects.length() == 0) {
                subjects.append("No enrolled subjects found.");
            }
    
        } catch (SQLException e) {
            // Log and handle SQL exceptions
            e.printStackTrace();
            throw new Exception("An error occurred while fetching enrolled subjects.", e);
        } catch (Exception e) {
            // Catch any other exceptions
            e.printStackTrace();
            throw new Exception("An unexpected error occurred.", e);
        }
    
        return subjects.toString();
    }
    
    
    
    private Map<String, String> getStudentClubs(String matric_number) throws SQLException, Exception {
    String query = "SELECT c.club_code, c.club_name FROM clubs c "
                 + "JOIN users u ON FIND_IN_SET(c.club_code, u.cocurricular_clubs) > 0 "
                 + "WHERE u.matric_number = ?";
    Map<String, String> studentClubs = new HashMap<>();

    try (Connection conn = DBHelper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, matric_number);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                studentClubs.put(rs.getString("club_code"), rs.getString("club_name"));
            }
        }

        if (studentClubs.isEmpty()) {
            studentClubs.put("No clubs", "No clubs found.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return studentClubs;
}

    
    private Map<String, List<String>> getStudentPositions(String matric_number) throws SQLException, Exception {
        // SQL query to get positions of a student directly from the club_positions table
        String query = "SELECT society_position, uniform_body_position, sports_club_position "
                    + "FROM student_positions "
                    + "WHERE matric_number = ?";

        Map<String, List<String>> studentPositions = new HashMap<>();

        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the matric number parameter in the query
            stmt.setString(1, matric_number);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // Check if a result is returned
                if (rs.next()) {
                    // Collect positions by club type (society, uniform body, sports club)
                    addPositionToMap(studentPositions, "Society", rs.getString("society_position"));
                    addPositionToMap(studentPositions, "Uniform Body", rs.getString("uniform_body_position"));
                    addPositionToMap(studentPositions, "Sports Club", rs.getString("sports_club_position"));
                }
            }
        } catch (SQLException e) {
            // Log the error and rethrow if necessary
            System.err.println("Error fetching student positions: " + e.getMessage());
            throw e;
        }

        // If no positions are found, return an empty map
        return studentPositions.isEmpty() ? Collections.emptyMap() : studentPositions;
    }

    // Helper method to add positions to the map if not null
    private void addPositionToMap(Map<String, List<String>> map, String clubType, String position) {
        if (position != null && !position.trim().isEmpty()) {
            map.computeIfAbsent(clubType, k -> new ArrayList<>()).add(position);
        }
    }



    private Map<String, List<String>> getStudentActivities(String matric_number) throws SQLException, Exception {
        // SQL query to fetch activities by matric number from the table with the correct columns
        String query = "SELECT al.club_code, al.activity_name, al.activity_level, al.achievement_level "
                     + "FROM activities_log al "
                     + "JOIN users u ON al.matric_number = u.matric_number "
                     + "WHERE u.matric_number = ?";
        
        Map<String, List<String>> studentActivities = new HashMap<>();
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        
            // Set the matric number parameter in the query
            stmt.setString(1, matric_number);
        
            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and collect activities
                while (rs.next()) {
                    String clubCode = rs.getString("club_code");
                    String activityName = rs.getString("activity_name");
                    String activityLevel = rs.getString("activity_level");
                    String achievementLevel = rs.getString("achievement_level");
        
                    // Create a formatted activity string (you can customize this as needed)
                    String activityInfo = String.format("Activity: %s, Level: %s, Achievement: %s", 
                                                        activityName, activityLevel, achievementLevel);
        
                    // Group activities by club code
                    studentActivities.computeIfAbsent(clubCode, k -> new ArrayList<>()).add(activityInfo);
                }
            }
        } catch (SQLException e) {
            // Log the error and rethrow if necessary
            System.err.println("Error fetching student activities: " + e.getMessage());
            throw e;
        }
        
        // If no activities are found, return an empty map
        return studentActivities.isEmpty() ? Collections.emptyMap() : studentActivities;
    }
    
    

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(StudentPortalGUI::new);
    }
}
