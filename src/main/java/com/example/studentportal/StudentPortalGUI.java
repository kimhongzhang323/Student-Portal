package com.example.studentportal;

import java.awt.BorderLayout; // Importing BorderLayout for layout management where components are arranged in five regions: North, South, East, West, and Center
import java.awt.CardLayout; // Importing CardLayout for managing a container that allows multiple panels to occupy the same space
import java.awt.Color; // Importing Color class for setting colors of components
import java.awt.Dimension; // Importing Dimension to set the size of components
import java.awt.FlowLayout; // Importing FlowLayout for arranging components sequentially from left to right or top to bottom
import java.awt.Font; // Importing Font for setting fonts for text in components
import java.awt.GridBagConstraints; // Importing GridBagConstraints for defining how components are added to a GridBagLayout
import java.awt.GridBagLayout; // Importing GridBagLayout for laying out components in a flexible grid of rows and columns
import java.awt.Insets; // Importing Insets to set the padding for components in layouts
import java.awt.event.ActionEvent; // Importing ActionEvent for handling action events like button clicks
import java.awt.event.ActionListener; // Importing ActionListener to listen for action events from components like buttons
import java.io.BufferedWriter; // Importing BufferedWriter for writing text to a file efficiently
import java.io.FileWriter; // Importing FileWriter for writing data to a file
import java.io.IOException; // Importing IOException to handle input/output exceptions
import java.security.MessageDigest; // Importing MessageDigest for cryptographic hash functions like SHA-256
import java.security.NoSuchAlgorithmException; // Importing NoSuchAlgorithmException for handling errors when the specified algorithm is not available
import java.security.SecureRandom; // Importing SecureRandom for generating cryptographically strong random numbers
import java.sql.Connection; // Importing Connection for connecting to a database
import java.sql.PreparedStatement; // Importing PreparedStatement for executing parameterized SQL queries
import java.sql.ResultSet; // Importing ResultSet for retrieving data from a database after executing a query
import java.sql.SQLException; // Importing SQLException to handle SQL exceptions
import java.sql.Statement; // Importing Statement for executing simple SQL queries
import java.util.ArrayList; // Importing ArrayList for using dynamic arrays that grow in size as needed
import java.util.Base64; // Importing Base64 for encoding and decoding binary data into ASCII characters
import java.util.HashMap; // Importing HashMap for storing key-value pairs in a map
import java.util.List; // Importing List interface for working with lists (dynamic arrays)
import java.util.Map; // Importing Map interface for working with key-value pairs (similar to HashMap)

import javax.swing.BoxLayout; // Importing BoxLayout for arranging components vertically or horizontally
import javax.swing.JButton; // Importing JButton for creating buttons in the GUI
import javax.swing.JCheckBox; // Importing JCheckBox for creating checkbox components in the GUI
import javax.swing.JFrame; // Importing JFrame for creating the main window/frame for the application
import javax.swing.JLabel; // Importing JLabel for displaying text or images in the GUI
import javax.swing.JOptionPane; // Importing JOptionPane for displaying standard dialog boxes for user interactions
import javax.swing.JPanel; // Importing JPanel for creating containers for other components
import javax.swing.JPasswordField; // Importing JPasswordField for entering passwords in a secure way
import javax.swing.JScrollPane; // Importing JScrollPane for adding scroll functionality to components like text areas
import javax.swing.JTextArea; // Importing JTextArea for displaying or editing multiline text
import javax.swing.JTextField; // Importing JTextField for creating single-line text fields
import javax.swing.SwingUtilities; // Importing SwingUtilities for performing tasks in the Event Dispatch Thread (EDT)



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
        List<String> subjects = new ArrayList<>(); // Create a list to store subject details
        String query = "SELECT subject_name, subject_code FROM academic_subjects"; // SQL query to fetch subject names and codes from the database
    
        // Use try-with-resources to ensure resources are closed automatically
        try (Connection conn = DBHelper.getConnection();  // Establish connection to the database
             Statement stmt = conn.createStatement();      // Create a statement to execute the query
             ResultSet rs = stmt.executeQuery(query)) {    // Execute the query and store the result in ResultSet
             
            // Iterate over the result set to fetch each row
            while (rs.next()) {
                // Combine subject name and code into a single string and add it to the subjects list
                String subject = rs.getString("subject_name") + " (" + rs.getString("subject_code") + ")";
                subjects.add(subject);
            }
        } catch (SQLException e) {
            // Handle SQL exception and print stack trace
            e.printStackTrace();
            // Rethrow exception to notify the caller of the error
            throw new SQLException("Error fetching subjects from the database.", e);
        }
    
        // Return the list of subjects
        return subjects;
    }
    
    
    private List<String> fetchClubsFromDatabase() throws SQLException, Exception {
        List<String> clubs = new ArrayList<>(); // Create a list to store club details
        String query = "SELECT club_name, club_code FROM clubs"; // SQL query to fetch club names and codes from the database
    
        // Use try-with-resources to ensure resources are closed automatically
        try (Connection conn = DBHelper.getConnection();  // Establish connection to the database
             Statement stmt = conn.createStatement();      // Create a statement to execute the query
             ResultSet rs = stmt.executeQuery(query)) {    // Execute the query and store the result in ResultSet
             
            // Iterate over the result set to fetch each row
            while (rs.next()) {
                // Combine club name and code into a single string and add it to the clubs list
                String club = rs.getString("club_name") + " (" + rs.getString("club_code") + ")";
                clubs.add(club);
            }
        } catch (SQLException e) {
            // Handle SQL exception and print stack trace
            e.printStackTrace();
            // Rethrow exception to notify the caller of the error
            throw new SQLException("Error fetching clubs from the database.", e);
        }
    
        // Return the list of clubs
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

        String subjectQuery = "SELECT subject_name, subject_code FROM academic_subjects";  // Fetch both subject_name and subject_code
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

        // Validate that matric number is not empty
        if (matricNumber == null || matricNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Matric number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            CoCurriculumMarksCalculator calculator = new CoCurriculumMarksCalculator();

            // Fetch the student's clubs
            List<CoCurriculumMarksCalculator.StudentClubPosition> clubs = calculator.getStudentClubPositions(matricNumber);

            if (clubs.isEmpty()) {
                resultArea.setText("No clubs found for matric number: " + matricNumber);
                return;
            }

            StringBuilder clubsText = new StringBuilder();
            for (CoCurriculumMarksCalculator.StudentClubPosition club : clubs) {
                clubsText.append(club.clubCode).append(" - ").append(club.clubName).append("\n");
            }
            

            // Display the list of clubs in the result area
            resultArea.setText(clubsText.toString());

            // Ask the user if they want to generate the full transcript
            int option = JOptionPane.showConfirmDialog(frame,
                    "Would you like to generate the full co-curricular transcript?",
                    "Generate Transcript", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Generate the transcript
                String transcript = calculator.generateTranscript(matricNumber);

                // Display the transcript
                JOptionPane.showMessageDialog(frame, transcript, "Co-curricular Transcript", JOptionPane.INFORMATION_MESSAGE);

                // Ask if the user wants to email the transcript
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    private class PositionsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();  // Retrieve the matric number from the text field
            try {
                // Call a method to get the positions for the student based on the matric number
                Map<String, List<String>> positions = getStudentPositions(matricNumber);
                
                // StringBuilder to build the text to display the positions
                StringBuilder positionsText = new StringBuilder();
                // Iterate over each entry (position category and the corresponding positions)
                for (Map.Entry<String, List<String>> entry : positions.entrySet()) {
                    // Append the position category and the list of positions (comma-separated)
                    positionsText.append(entry.getKey()).append(": ").append(String.join(", ", entry.getValue())).append("\n");
                }
                
                // Display the formatted positions in the result area
                resultArea.setText(positionsText.toString());
            } catch (Exception ex) {
                ex.printStackTrace();  // Log the exception for debugging
                // Show an error dialog if an exception occurs while fetching positions
                JOptionPane.showMessageDialog(frame, "An error occurred while fetching positions.");
            }
        }
    }


    private class ActivitiesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String matricNumber = matricNumberField.getText();  // Retrieve the matric number from the text field
            try {
                // Call a method to get the activities for the student based on the matric number
                Map<String, List<String>> activities = getStudentActivities(matricNumber);
                
                // StringBuilder to build the text to display the activities
                StringBuilder activitiesText = new StringBuilder();
                // Iterate over each entry (activity category and the list of activities)
                for (Map.Entry<String, List<String>> entry : activities.entrySet()) {
                    activitiesText.append(entry.getKey()).append(":\n");
                    // Iterate through the activities and add each activity to the string
                    for (String activity : entry.getValue()) {
                        activitiesText.append("  - ").append(activity).append("\n");
                    }
                }
                
                // Display the formatted activities in the result area
                resultArea.setText(activitiesText.toString());
            } catch (Exception ex) {
                ex.printStackTrace();  // Log the exception for debugging
                // Show an error dialog if an exception occurs while fetching activities
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

    // Method to validate the entered credentials (matricNumber and password)
    private boolean validateCredentials(String matricNumber, String enteredPassword) throws Exception {
        // SQL query to get the stored password for a given matric number
        String query = "SELECT password FROM users WHERE matric_number = ?";

        try (Connection conn = DBHelper.getConnection();  // Get database connection
            PreparedStatement stmt = conn.prepareStatement(query)) {  // Prepare SQL statement

            // Set the matric number parameter in the query
            stmt.setString(1, matricNumber);

            try (ResultSet rs = stmt.executeQuery()) {  // Execute the query
                if (rs.next()) {  // Check if a result is returned (i.e., user found)
                    // Retrieve the stored password from the database
                    String storedPassword = rs.getString("password");

                    // Check if the password is in the hashed format (salt:hashedPassword)
                    if (storedPassword != null && storedPassword.contains(":")) {
                        // Split the stored password into salt and hash
                        String[] parts = storedPassword.split(":");
                        if (parts.length < 2) {
                            return false; // Invalid format, fail authentication
                        }

                        // Extract the stored salt and hashed password from the parts
                        String storedSalt = parts[0];  // Salt part
                        String storedHash = parts[1];  // Hashed password part

                        // Decode the salt from Base64 format
                        byte[] salt = Base64.getDecoder().decode(storedSalt);

                        // Hash the entered password using the stored salt
                        String enteredHash = hashPassword(enteredPassword, salt);

                        // Compare the entered hash with the stored hash
                        return storedHash.equals(enteredHash);  // Return true if hashes match, else false

                    } else {
                        // If the stored password is not in the hashed format, compare directly with the entered password
                        return storedPassword.equals(enteredPassword);  // Return true if passwords match
                    }
                }
            }
        }

        // Return false if no user found or if credentials are invalid
        return false;
    }

    // Method to generate a random salt for password hashing
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();  // Secure random generator for salt
        byte[] salt = new byte[16];  // 16 bytes of salt (128 bits)
        random.nextBytes(salt);  // Generate random bytes for the salt
        return salt;  // Return the generated salt
    }

    // Hash the password with the salt
    private static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt); // Add the salt to the hash computation
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    // Method to register a new user
    public void registerUser(String matricNumber, String password, String email, String subjectCodes, String clubCode) throws SQLException, Exception {
        // Automatically generate email from matric number if email is not provided
        if (email == null || email.isEmpty()) {
            email = matricNumber + "@student.fop";  // Default email format
        }

        // Generate salt for password security
        byte[] salt = generateSalt();  // Generate a random salt
        // Hash the password using the generated salt
        String hashedPassword = hashPassword(password, salt);

        // Combine the salt and hashed password for secure storage in the database
        String combinedPassword = Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword;

        // SQL query to insert the user data into the 'users' table
        String query = "INSERT INTO users (matric_number, password, email, academic_subjects, cocurricular_clubs) VALUES (?, ?, ?, ?, ?)";

        // Open a connection to the database and prepare the statement
        try (Connection conn = DBHelper.getConnection();  // Get database connection
            PreparedStatement stmt = conn.prepareStatement(query)) {  // Prepare the query

            // Set the parameters for the SQL query
            stmt.setString(1, matricNumber);  // Set matric number
            stmt.setString(2, combinedPassword); // Set the combined salt and hashed password
            stmt.setString(3, email);         // Set email
            stmt.setString(4, subjectCodes);  // Set academic subject codes (comma-separated)
            stmt.setString(5, clubCode);      // Set club code (comma-separated)

            // Execute the query to insert the user data into the database
            stmt.executeUpdate();
        }
    }

    // Method to save user data to a file
    public void saveUserToFile(String matricNumber, String password, String email, String subjectCodes, String clubCodes) throws IOException {
        // Define the path to the file where user data will be saved
        String filePath = "data/UserData.txt";
        
        // Ensure that subjectCodes and clubCodes are non-null and not empty
        if (subjectCodes == null || subjectCodes.isEmpty()) {
            subjectCodes = "No subjects selected";  // Provide a fallback value if no subjects are selected
        }
        if (clubCodes == null || clubCodes.isEmpty()) {
            clubCodes = "No clubs selected";  // Provide a fallback value if no clubs are selected
        }

        // Ensure that email and password are non-null and properly formatted
        if (email == null || email.isEmpty()) {
            email = "No email provided";  // Default value for email if it's empty or null
        }
        if (password == null || password.isEmpty()) {
            password = "No password provided";  // Default value for password if it's empty or null
        }

        // Format the email correctly as matricNumber@student.fop if it doesn't already contain "@"
        if (!email.contains("@")) {
            email = matricNumber + "@student.fop";  // Ensure the email is formatted correctly
        }

        // Open the file in append mode, so data is added without overwriting existing entries
        try (FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            // Remove any extra spaces between subject and club codes
            subjectCodes = subjectCodes.replaceAll("\\s+", "");  // Remove any extra spaces between codes
            clubCodes = clubCodes.replaceAll("\\s+", "");  // Remove any extra spaces between codes

            // Format the data in the required structure (matching the example format)
            String userData = String.format("%s\n%s\n%s\n%s\n%s", 
                                            email,        // email (MatricNumber@student.fop)
                                            matricNumber, // matric number
                                            password,     // password
                                            subjectCodes, // comma-separated subject codes
                                            clubCodes);   // comma-separated club codes

            // Write the formatted user data to the file
            bufferedWriter.write(userData);
            bufferedWriter.newLine();  // Add a new line after each user's entry for clarity
        }
    }

    // Method to get the list of subjects a student is enrolled in from the database
    private String getEnrolledSubjects(String matricNumber) throws Exception {
        
        // SQL query to fetch the subject code and name for subjects the student is enrolled in
        String query = "SELECT s.subject_code, s.subject_name FROM academic_subjects s "
                    + "JOIN users u ON FIND_IN_SET(s.subject_code, u.academic_subjects) > 0 "
                    + "WHERE u.matric_number = ?";
        
        // List to store the subjects as pairs of subject code and subject name
        List<String[]> subjects = new ArrayList<>();
        
        // Establish connection to the database and prepare the query
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the matric number parameter in the query
            stmt.setString(1, matricNumber);
            
            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add each subject (code and name) to the list
                while (rs.next()) {
                    subjects.add(new String[] { rs.getString("subject_code"), rs.getString("subject_name") });
                }
            }
        }
        
        // Sort the subjects alphabetically by subject name using bubble sort
        bubbleSortByName(subjects);
        
        // Create a StringBuilder to construct a formatted string of the sorted subjects
        StringBuilder subjectsText = new StringBuilder();
        for (String[] subject : subjects) {
            // Append each subject's code and name in the format "subject_code - subject_name"
            subjectsText.append(subject[0])  // subject_code
                        .append(" - ").append(subject[1])  // subject_name
                        .append("\n");
        }
        
        // Return the formatted string of sorted subjects
        return subjectsText.toString();
    }

    // Bubble sort method to sort the list of subjects by subject name in ascending order
    private void bubbleSortByName(List<String[]> subjects) {
        int n = subjects.size();
        // Outer loop for multiple passes over the list
        for (int i = 0; i < n - 1; i++) {
            // Inner loop for comparing adjacent elements
            for (int j = 0; j < n - i - 1; j++) {
                // Compare the subject names (case-insensitive) to determine if a swap is needed
                if (subjects.get(j)[1].compareToIgnoreCase(subjects.get(j + 1)[1]) > 0) {
                    // Swap the subjects if they are in the wrong order
                    String[] temp = subjects.get(j);
                    subjects.set(j, subjects.get(j + 1));
                    subjects.set(j + 1, temp);
                }
            }
        }
    }

    // Method to retrieve the clubs a student is a member of from the database
    private Map<String, String> getStudentClubs(String matricNumber) throws SQLException, Exception {
        
        // SQL query to fetch the club code and name for clubs that the student is part of
        String query = "SELECT c.club_code, c.club_name FROM clubs c "
                    + "JOIN users u ON FIND_IN_SET(c.club_code, u.cocurricular_clubs) > 0 "
                    + "WHERE u.matric_number = ?";
        
        // Map to store the club code and name (key = club_code, value = club_name)
        Map<String, String> studentClubs = new HashMap<>();
        
        // Establish connection to the database and prepare the query
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the matric number parameter in the query
            stmt.setString(1, matricNumber);
            
            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                // Iterate through the result set and add each club to the map
                while (rs.next()) {
                    studentClubs.put(rs.getString("club_code"), rs.getString("club_name"));
                }
            }
        }
        
        // Return the map containing the student's club codes and names
        return studentClubs;
    }

    // Method to retrieve student positions from the database
    private Map<String, java.util.List<String>> getStudentPositions(String matricNumber) throws SQLException, Exception {
        
        // SQL query to retrieve the positions from the database
        String query = "SELECT society_position, uniform_body_position, sports_club_position "
                    + "FROM student_positions WHERE matric_number = ?";
        
        // Map to store the positions by category (Society, Uniform Body, Sports Club)
        Map<String, java.util.List<String>> positions = new HashMap<>();
        
        // Establish connection to the database and prepare the query
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the matric number parameter in the query
            stmt.setString(1, matricNumber);
            
            // Execute the query and process the result set
            try (ResultSet rs = stmt.executeQuery()) {
                // If there is a result for the matric number
                if (rs.next()) {
                    // Add each position to the map, grouped by category
                    addPositionToMap(positions, "Society", rs.getString("society_position"));
                    addPositionToMap(positions, "Uniform Body", rs.getString("uniform_body_position"));
                    addPositionToMap(positions, "Sports Club", rs.getString("sports_club_position"));
                }
            }
        }
        
        // Return the map containing the positions for the student
        return positions;
    }

    // Method to add a position (value) to the map under the specified key
    private void addPositionToMap(Map<String, java.util.List<String>> map, String key, String value) {
        
        // Check if the value is not null or empty
        if (value != null && !value.isEmpty()) {
            
            // If the key does not exist in the map, create a new list and add the value to it
            // If the key exists, simply add the value to the existing list
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
    }

    // Method to retrieve student activities based on their matric number
    private Map<String, java.util.List<String>> getStudentActivities(String matricNumber) throws SQLException, Exception {  
    
    // SQL query to get the activities, club codes, activity names, levels, and achievements for the student
    String query = "SELECT al.club_code, al.activity_name, al.activity_level, al.achievement_level "
                 + "FROM activities_log al "
                 + "JOIN users u ON al.matric_number = u.matric_number "
                 + "WHERE u.matric_number = ?";
    
    // Initialize a Map to store the activities grouped by club code
    Map<String, java.util.List<String>> activities = new HashMap<>();
    
        // Establish a connection to the database and prepare the query
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the matric number as a parameter in the query
            stmt.setString(1, matricNumber);
            
            // Execute the query and obtain the result set
            try (ResultSet rs = stmt.executeQuery()) {
                
                // Iterate through the result set
                while (rs.next()) {
                    // Format the activity details (activity name, level, achievement)
                    String activityInfo = String.format("Activity: %s, Level: %s, Achievement: %s",
                            rs.getString("activity_name"), rs.getString("activity_level"), rs.getString("achievement_level"));
                    
                    // Add the formatted activity info to the map under the appropriate club code
                    activities.computeIfAbsent(rs.getString("club_code"), k -> new ArrayList<>()).add(activityInfo);
                }
            }
        }
        
        // Return the map containing activities grouped by club code
        return activities;
    }

    public static void main(String[] args) {
        // SwingUtilities.invokeLater is used to ensure that the GUI creation happens on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a new instance of StudentPortalGUI (the main GUI of the application)
                    new StudentPortalGUI();
                } catch (Exception e) {
                    // If an error occurs during the creation of the GUI, it is caught here
                    e.printStackTrace(); // Print the exception's stack trace for debugging purposes
                    // Show an error message dialog to the user if the application couldn't start
                    JOptionPane.showMessageDialog(null, "An error occurred while starting the application.");
                }
            }
        });
    }
    
}
