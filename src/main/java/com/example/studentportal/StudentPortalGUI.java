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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
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
    private final JTextArea resultArea;
    private final JPanel userPanel;

    public StudentPortalGUI() throws Exception {
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

        // Add action listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());
        subjectsButton.addActionListener(new SubjectsAction());
        clubsButton.addActionListener(new ClubsAction());
        positionsButton.addActionListener(new PositionsAction());
        activitiesButton.addActionListener(new ActivitiesAction());
        logoutButton.addActionListener(new LogoutAction());

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
        buttonPanel.add(logoutButton);

        userPanel.add(resultScrollPane, BorderLayout.CENTER);
        userPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(loginPanel, "Login");
        panel.add(userPanel, "User");

        frame.add(panel);
        createRegisterPanel();
        frame.setVisible(true);
    }

    private List<String> fetchSubjectsFromDatabase() throws SQLException, Exception {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT subject_name, subject_code FROM academic_subjects"; // Fetch both subject_name and subject_code
        try (Connection conn = DBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String subject = rs.getString("subject_name") + " (" + rs.getString("subject_code") + ")";
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching subjects from the database.", e);
        }
        return subjects;
    }
    
    private List<String> fetchClubsFromDatabase() throws SQLException, Exception {
        List<String> clubs = new ArrayList<>();
        String query = "SELECT club_name, club_code FROM clubs"; // Fetch both club_name and club_code
        try (Connection conn = DBHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String club = rs.getString("club_name") + " (" + rs.getString("club_code") + ")";
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching clubs from the database.", e);
        }
        return clubs;
    }
    
    private void createRegisterPanel() throws Exception {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        registerPanel.setBackground(new Color(238, 238, 238));
    
        // Matric Number Label and Field
        JLabel matricLabel = new JLabel("Matric Number:");
        matricLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        registerPanel.add(matricLabel, gbc);
    
        JTextField matricNumberField = new JTextField(20);
        matricNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        registerPanel.add(matricNumberField, gbc);
    
        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(passwordLabel, gbc);
    
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        registerPanel.add(passwordField, gbc);
    
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        registerPanel.add(emailField, gbc);
    
        // Academic Subjects (Multiple selection)
        JLabel subjectsLabel = new JLabel("Select Subjects:");
        subjectsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10);
        registerPanel.add(subjectsLabel, gbc);
    
        // Fetch subjects from the database and add them as checkboxes
        List<String> subjects = fetchSubjectsFromDatabase();
        JPanel subjectsPanel = new JPanel();
        subjectsPanel.setLayout(new BoxLayout(subjectsPanel, BoxLayout.Y_AXIS));
        JCheckBox[] subjectCheckBoxes = new JCheckBox[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            subjectCheckBoxes[i] = new JCheckBox(subjects.get(i));
            subjectsPanel.add(subjectCheckBoxes[i]);
        }
    
        gbc.gridx = 1;
        gbc.gridy = 4;
        registerPanel.add(subjectsPanel, gbc);
    
        // Clubs (Multiple selection - up to 3)
        JLabel clubsLabel = new JLabel("Select Clubs (up to 3):");
        clubsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 5, 10);
        registerPanel.add(clubsLabel, gbc);
    
        // Fetch clubs from the database and add them as checkboxes
        List<String> clubs = fetchClubsFromDatabase();
        JPanel clubsPanel = new JPanel();
        clubsPanel.setLayout(new BoxLayout(clubsPanel, BoxLayout.Y_AXIS));
        JCheckBox[] clubCheckBoxes = new JCheckBox[clubs.size()];
        for (int i = 0; i < clubs.size(); i++) {
            clubCheckBoxes[i] = new JCheckBox(clubs.get(i));
            clubsPanel.add(clubCheckBoxes[i]);
        }
    
        gbc.gridx = 1;
        gbc.gridy = 6;
        registerPanel.add(clubsPanel, gbc);
    
        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(120, 40));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        registerButton.setFocusable(false);
        registerPanel.add(registerButton, gbc);
    
        // ActionListener for Register Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String matricNumber = matricNumberField.getText();
                String password = new String(passwordField.getPassword());
                String email = matricNumber + "@student.fop"; // Email from matric number
    
                if (matricNumber.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields!");
                    return;
                }
    
                // Get selected subjects
                StringBuilder selectedSubjects = new StringBuilder();
                for (JCheckBox checkBox : subjectCheckBoxes) {
                    if (checkBox.isSelected()) {
                        selectedSubjects.append(checkBox.getText()).append(", ");
                    }
                }
                if (selectedSubjects.length() > 0) selectedSubjects.setLength(selectedSubjects.length() - 2); // Remove last comma
    
                // Get selected clubs (up to 3)
                StringBuilder selectedClubs = new StringBuilder();
                int clubCount = 0;
                for (JCheckBox checkBox : clubCheckBoxes) {
                    if (checkBox.isSelected() && clubCount < 3) { // Allow up to 3 selections
                        selectedClubs.append(checkBox.getText()).append(", ");
                        clubCount++;
                    }
                }
                if (selectedClubs.length() > 0) selectedClubs.setLength(selectedClubs.length() - 2); // Remove last comma
    
                try {
                    // Save to MySQL database
                    registerUser(matricNumber, password, email, selectedSubjects.toString(), selectedClubs.toString());
    
                    // Save to file
                    saveUserToFile(matricNumber, password, email, selectedSubjects.toString(), selectedClubs.toString());
    
                    JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "An error occurred during registration.");
                }
            }
        });
    
        // Add the register panel to the main panel
        panel.add(registerPanel, "Register");
    }
    

    public class AcademicClubData {
        private List<String> subjectsWithCodes;
        private List<String> clubsWithCodes;

        public AcademicClubData(List<String> subjectsWithCodes, List<String> clubsWithCodes) {
            this.subjectsWithCodes = subjectsWithCodes;
            this.clubsWithCodes = clubsWithCodes;
        }

        // Getters for the combined data
        public List<String> getSubjectsWithCodes() {
            return subjectsWithCodes;
        }

        public List<String> getClubsWithCodes() {
            return clubsWithCodes;
        }
    }

    public AcademicClubData fetchAcademicAndClubData() throws SQLException, Exception {
        List<String> subjectsWithCodes = new ArrayList<>();
        List<String> clubsWithCodes = new ArrayList<>();

        String subjectQuery = "SELECT subject_name, subject_code FROM subjects";  // Fetch both subject_name and subject_code
        String clubQuery = "SELECT club_name, club_code FROM clubs";  // Fetch both club_name and club_code

        try (Connection conn = DBHelper.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet subjectRs = stmt.executeQuery(subjectQuery);
            ResultSet clubRs = stmt.executeQuery(clubQuery)) {

            // Fetch subject data and combine name with code
            while (subjectRs.next()) {
                String subjectWithCode = subjectRs.getString("subject_name") + " (" + subjectRs.getString("subject_code") + ")";
                subjectsWithCodes.add(subjectWithCode);
            }

            // Fetch club data and combine name with code
            while (clubRs.next()) {
                String clubWithCode = clubRs.getString("club_name") + " (" + clubRs.getString("club_code") + ")";
                clubsWithCodes.add(clubWithCode);
            }
        } catch (SQLException e) {
            // Handle exception properly
            e.printStackTrace();
            throw new SQLException("Error fetching subjects or clubs from the database.", e);
        }

        return new AcademicClubData(subjectsWithCodes, clubsWithCodes);
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
            showRegisterPanel();
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

    private class ClubsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                resultArea.setText(getStudentClubs(matricNumber));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching clubs.");
            }
        }
    }

    private class PositionsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();
            try {
                resultArea.setText(getStudentPositions(matricNumber));
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
                resultArea.setText(getStudentActivities(matricNumber));
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

    private void showRegisterPanel() {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, "Register");
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

    private void registerUser(String matricNumber, String password, String email, String subjectCodes, String clubCode) throws SQLException, Exception {
        // Automatically generate email from matric number
        email = matricNumber + "@student.fop"; 
    
        // Subject codes and club codes should already be passed as comma-separated values (e.g., "S001,S002,S003")
        String query = "INSERT INTO users (matric_number, password, email, academic_subjects, cocurricular_clubs) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the matric number
            stmt.setString(1, matricNumber);  
            
            // Set the password
            stmt.setString(2, password);      
            
            // Set the generated email
            stmt.setString(3, email);         
        
            // Set the subject codes (comma-separated list of subject codes)
            stmt.setString(4, subjectCodes);  // Pass subject codes directly
            
            // Set the selected club code (single club code)
            stmt.setString(5, clubCode);     // Pass the selected club code
            
            // Execute the query
            stmt.executeUpdate();             
        }
    }
    
    

    private void saveUserToFile(String matricNumber, String password, String email, String subjectCodes, String clubCodes) throws IOException {
        String filePath = "data/UserData.txt";
    
        // Ensure that subjectCodes and clubCodes are non-null and not empty
        if (subjectCodes == null || subjectCodes.isEmpty()) {
            subjectCodes = "No subjects selected";  // Provide a fallback value if no subjects are selected
        }
        if (clubCodes == null || clubCodes.isEmpty()) {
            clubCodes = "No clubs selected";  // Provide a fallback value if no clubs are selected
        }
        
        // Ensure that all arguments are valid (non-null and non-empty)
        if (email == null || email.isEmpty()) {
            email = "No email provided";  // Default value for email if it's empty or null
        }
        if (password == null || password.isEmpty()) {
            password = "No password provided";  // Default value for password if it's empty or null
        }
        
        try (FileWriter fileWriter = new FileWriter(filePath, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
        
            // Ensure correct number of arguments (5 %s in format)
            String userData = String.format("%s\n%s\n%s\n%s\n%s", 
                                            email,        // email (MatricNumber@student.fop)
                                            matricNumber, // matric number
                                            password,     // password
                                            subjectCodes, // comma-separated subject codes
                                            clubCodes);   // comma-separated club codes
        
            // Write the formatted string to the file and add a newline after each user entry
            bufferedWriter.write(userData);
            bufferedWriter.newLine();  // Add a new line after each user's entry for clarity
        }
    }
    
    
    
    

    private String getEnrolledSubjects(String matricNumber) throws SQLException, Exception {
        String query = "SELECT subject_name FROM subjects WHERE matric_number = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder subjects = new StringBuilder("Enrolled Subjects:\n");
                while (rs.next()) {
                    subjects.append(rs.getString("subject_name")).append("\n");
                }
                return subjects.toString();
            }
        }
    }

    private String getStudentClubs(String matricNumber) throws SQLException, Exception {
        String query = "SELECT club_name FROM clubs WHERE matric_number = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder clubs = new StringBuilder("Clubs:\n");
                while (rs.next()) {
                    clubs.append(rs.getString("club_name")).append("\n");
                }
                return clubs.toString();
            }
        }
    }

    private String getStudentPositions(String matricNumber) throws SQLException, Exception {
        String query = "SELECT position_name FROM positions WHERE matric_number = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder positions = new StringBuilder("Positions:\n");
                while (rs.next()) {
                    positions.append(rs.getString("position_name")).append("\n");
                }
                return positions.toString();
            }
        }
    }

    private String getStudentActivities(String matricNumber) throws SQLException, Exception {
        String query = "SELECT activity_name FROM activities WHERE matric_number = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder activities = new StringBuilder("Activities:\n");
                while (rs.next()) {
                    activities.append(rs.getString("activity_name")).append("\n");
                }
                return activities.toString();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new StudentPortalGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while starting the application.");
                }
            }
        });
    }
}
