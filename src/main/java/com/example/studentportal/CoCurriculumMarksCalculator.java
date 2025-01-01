package com.example.studentportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class CoCurriculumMarksCalculator {

    // Database connection details (URL, username, and password)
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/student_info";
    private static final String USER = "root";
    private static final String PASSWORD = "buku3b3B";

    // Main entry point of the program
    public static void main(String[] args) throws Exception {
        // Get the matric number from the application (simulated here for now)
        String matricNumber = getMatricNumberFromApplication();

        // If matric number is null or empty, show an error message and exit
        if (matricNumber == null || matricNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Matric number is not available. Exiting the program.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create an instance of the calculator and generate the transcript for the student
        CoCurriculumMarksCalculator calculator = new CoCurriculumMarksCalculator();
        String transcript = calculator.generateTranscript(matricNumber);

        // Show the generated transcript in a dialog box
        JOptionPane.showMessageDialog(null, transcript, "Co-curricular Transcript", JOptionPane.INFORMATION_MESSAGE);
    }

    // Simulated method to get the matric number (In a real application, this would be dynamic)
    private static String getMatricNumberFromApplication() {
        return "s100201";  // Simulated student matric number
    }

    // Method to generate a full transcript for a student based on their matric number
    public String generateTranscript(String matricNumber) throws SQLException {
        // Retrieve the list of clubs the student is involved in
        List<StudentClubPosition> studentClubPositions = getStudentClubPositions(matricNumber);

        // If no clubs are found for the student, return a message saying so
        if (studentClubPositions.isEmpty()) {
            return "No co-curricular activities found for student " + matricNumber;
        }

        // StringBuilder to construct the transcript output
        StringBuilder transcript = new StringBuilder();
        transcript.append("Co-curriculum Transcript for ").append(matricNumber).append("\n");
        transcript.append("============================================================================\n");

        int totalMarks = 0;  // Initialize total marks
        List<Integer> clubTotalMarksList = new ArrayList<>();  // To store the total marks for each club

        // Loop through each club to get position, activity, and marks
        for (StudentClubPosition position : studentClubPositions) {
            // Get the position for the club (e.g., President, Active Member)
            String clubPosition = getPositionForCategory(matricNumber, position.clubCode);

            // If club position is null, handle it
            if (clubPosition == null) {
                clubPosition = "None";  // Assign default value if position is not found
            }

            // Get the activity details from the activities_log table
            ActivityDetails activityDetails = getActivityDetails(matricNumber, position.clubCode);

            // Calculate the marks for the position
            int positionMarks = calculateActivityMarks(position.clubCode, clubPosition);

            // Calculate marks for activity level and achievement level
            int activityLevelMarks = getActivityLevelMarks(activityDetails.activityLevel);
            int achievementMarks = getAchievementMarks(activityDetails.achievementLevel);

            // Fixed attendance marks (50/50)
            int attendanceMarks = 50;

            // Calculate the total marks for this club
            int clubTotalMarks = positionMarks + activityLevelMarks + achievementMarks + attendanceMarks;

            // Add the club details to the transcript
            transcript.append("[").append(position.clubCode).append(" - ").append(position.clubName).append("]\n");

            // Add the attendance line with full marks
            transcript.append("Attendance: assume full ------------> 50/50 marks\n");

            // Add the position and its marks
            transcript.append("Position: ").append(clubPosition).append(" ------------> ").append(positionMarks).append("/10 marks\n");

            // Add the selected activity details
            transcript.append("Selected Activity: ").append(activityDetails.selectedActivity).append("\n");
            transcript.append("Level of Activities: ").append(activityDetails.activityLevel).append(" ------------> ").append(activityLevelMarks).append("/20 marks\n");
            transcript.append("Achievement Level: ").append(activityDetails.achievementLevel).append(" ------------> ").append(achievementMarks).append("/20 marks\n");

            // Add the total marks for the club
            transcript.append("Total Marks for Club: ").append(clubTotalMarks).append("/100 marks\n");

            // Add a separator
            transcript.append("============================================================================\n");

            // Add this club's marks to the total marks and store the total marks for final calculation
            totalMarks += clubTotalMarks;
            clubTotalMarksList.add(clubTotalMarks);  // Store total marks for this club
        }

        // Calculate the final marks (average of two highest marks)
        double finalMarks = calculateFinalMarks(clubTotalMarksList);

        // Add the final total marks for the student
        transcript.append("Total Marks for Student: ").append(totalMarks).append("/100 marks\n");
        transcript.append("Final Marks (Based on two highest scores): ").append(finalMarks).append("/100 marks\n");

        // Return the generated transcript
        return transcript.toString();
    }

    // Method to calculate the final marks as the average of the two highest scores
    private double calculateFinalMarks(List<Integer> clubTotalMarksList) {
        // Sort the list in descending order to find the two highest marks
        clubTotalMarksList.sort((a, b) -> Integer.compare(b, a));

        // Take the top two scores
        int highest = clubTotalMarksList.get(0);
        int secondHighest = clubTotalMarksList.get(1);

        // Calculate the final mark by averaging the two highest scores
        return (highest + secondHighest) / 2.0;
    }

    // Method to retrieve the student's clubs from the database
    private List<StudentClubPosition> getStudentClubPositions(String matricNumber) throws SQLException {
        List<StudentClubPosition> positions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            String userQuery = "SELECT cocurricular_clubs FROM users WHERE matric_number = ?";

            try (PreparedStatement stmt = conn.prepareStatement(userQuery)) {
                stmt.setString(1, matricNumber);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String cocurricularClubs = rs.getString("cocurricular_clubs");

                        // Step 2: Split the cocurricular_clubs into individual club codes
                        String[] clubCodes = cocurricularClubs.split(",");

                        // Step 3: Query the clubs table to retrieve information about each club
                        for (String clubCode : clubCodes) {
                            String clubQuery = "SELECT club_code, club_name, category FROM clubs WHERE club_code = ?";

                            try (PreparedStatement clubStmt = conn.prepareStatement(clubQuery)) {
                                clubStmt.setString(1, clubCode.trim());  // Trim any extra spaces around club code

                                try (ResultSet clubRs = clubStmt.executeQuery()) {
                                    if (clubRs.next()) {
                                        String clubName = clubRs.getString("club_name");
                                        String category = clubRs.getString("category");
                                        positions.add(new StudentClubPosition(clubCode, clubName, category));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return positions;
    }

    // Method to get details of the selected activity, level, and achievement level
    private ActivityDetails getActivityDetails(String matricNumber, String clubCode) throws SQLException {
        String query = "SELECT a.activity_name, a.activity_level, a.achievement_level " +
                    "FROM activities_log a " +
                    "JOIN clubs c ON a.club_code = c.club_code " +
                    "WHERE a.matric_number = ? AND a.club_code = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, matricNumber); // Bind the matric number
                stmt.setString(2, clubCode); // Bind the club code

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String activityName = rs.getString("activity_name");
                        String activityLevel = rs.getString("activity_level");
                        String achievementLevel = rs.getString("achievement_level");
                        return new ActivityDetails(activityName, activityLevel, achievementLevel);
                    }
                }
            }
        }
        return new ActivityDetails("None", "None", "None");
    }

    // Helper method to retrieve the position from the student_positions table
    private String getPositionForCategory(String matricNumber, String clubCode) throws SQLException {
        String clubCategory = getClubCategory(clubCode);
        String positionColumn = getPositionColumnForCategory(clubCategory);

        if (positionColumn == null) {
            throw new SQLException("Invalid club category: " + clubCategory);
        }

        String query = "SELECT " + positionColumn + " FROM student_positions WHERE matric_number = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, matricNumber);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(positionColumn);
                    }
                }
            }
        }
        return null;
    }

    // Fetch the category of the club (Society, Uniform Body, or Sports Club) based on the club_code
    private String getClubCategory(String clubCode) throws SQLException {
        String query = "SELECT category FROM clubs WHERE club_code = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD)) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, clubCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("category");
                    }
                }
            }
        }
        return null;
    }

    // Map the club category to the correct position column
    private String getPositionColumnForCategory(String category) {
        if ("Society".equalsIgnoreCase(category)) {
            return "society_position";
        } else if ("Uniform Body".equalsIgnoreCase(category)) {
            return "uniform_body_position";
        } else if ("Sports Club".equalsIgnoreCase(category)) {
            return "sports_club_position";
        }
        return null;
    }

    // Method to calculate the marks based on the position name
    private int calculateActivityMarks(String clubCode, String clubPosition) {
        switch (clubPosition) {
            case "President":
                return 10;
            case "Vice President":
            case "Secretary":
            case "Treasurer":
                return 9;
            case "Vice Secretary":
            case "Vice Treasurer":
                return 8;
            case "Committee":
                return 7;
            case "Active Member":
                return 6;
            default:
                return 0;
        }
    }

    // Helper method to retrieve the marks based on the activity level
    private int getActivityLevelMarks(String activityLevel) {
        switch (activityLevel) {
            case "International": return 20;
            case "National": return 15;
            case "State": return 12;
            case "School": return 10;
            default: return 0;
        }
    }

    // Helper method to retrieve the marks based on the achievement level
    private int getAchievementMarks(String achievementLevel) {
        switch (achievementLevel) {
            case "Gold": return 20;
            case "Silver": return 15;
            case "Bronze": return 10;
            case "Participation": return 0;
            default: return 0;
        }
    }

    // Inner class to represent the marks and details for a single club's activity
    class ClubMarks {
        String clubCode, clubName, position;
        int positionMarks;
        String selectedActivity, activityLevel, achievementLevel;

        ClubMarks(String clubCode, String clubName, String position, int positionMarks,
                  String selectedActivity, String activityLevel, String achievementLevel) {
            this.clubCode = clubCode;
            this.clubName = clubName;
            this.position = position;
            this.positionMarks = positionMarks;
            this.selectedActivity = selectedActivity;
            this.activityLevel = activityLevel;
            this.achievementLevel = achievementLevel;
        }

        @Override
        public String toString() {
            return "[" + clubCode + " - " + clubName + "]\n" +
                    "Position: " + (position == null ? "None" : position) + " ------------> " + positionMarks + "/10 marks\n" +
                    "Selected Activity: " + selectedActivity + " ------------> " + activityLevel + "\n" +
                    "Achievement Level: " + achievementLevel + " ------------> " + "0/20 marks\n" +
                    "============================================================================\n";
        }
    }

    // Inner class to represent the details of the activity
    class ActivityDetails {
        String selectedActivity, activityLevel, achievementLevel;

        ActivityDetails(String selectedActivity, String activityLevel, String achievementLevel) {
            this.selectedActivity = selectedActivity;
            this.activityLevel = activityLevel;
            this.achievementLevel = achievementLevel;
        }
    }

    // Inner class to represent a student's club and activity details
    class StudentClubPosition {
        String clubCode, clubName, category;

        StudentClubPosition(String clubCode, String clubName, String category) {
            this.clubCode = clubCode;
            this.clubName = clubName;
            this.category = category;
        }
    }
}
