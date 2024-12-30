package com.example.studentportal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClubsAction implements ActionListener {

    // Method to get position marks
    public int getPositionMarks(String position) {
        switch (position.toLowerCase()) {
            case "president":
                return 10;
            case "vice president":
                return 8;
            case "secretary":
                return 6;
            case "treasurer":
                return 5;
            case "member":
                return 3;
            default:
                return 0;
        }
    }

    // Method to get achievement marks
    public int getAchievementMarks(Activity activity) {
        switch (activity.getAchievementLevel().toLowerCase()) {
            case "gold":
                return 20;
            case "silver":
                return 15;
            case "bronze":
                return 10;
            default:
                return 0;
        }
    }

    // Method to get activity marks
    public int getActivityMarks(Activity activity) {
        switch (activity.getActivityLevel().toLowerCase()) {
            case "international":
                return 20;
            case "national":
                return 15;
            case "state":
                return 10;
            case "university":
                return 5;
            default:
                return 0;
        }
    }

    private JFrame frame;
    private JTextField matric_numberField;
    private JTextArea resultArea;
    private JButton generateTranscriptButton;
    
    private List<ClubTranscript> clubTranscripts = new ArrayList<>();
    
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setMatricNumberField(JTextField matric_numberField) {
        this.matric_numberField = matric_numberField;
    }

    public void setResultArea(JTextArea resultArea) {
        this.resultArea = resultArea;
    }

    public void setGenerateTranscriptButton(JButton generateTranscriptButton) {
        this.generateTranscriptButton = generateTranscriptButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String matric_number = matric_numberField.getText();
        try {
            // Fetch co-curricular clubs for the student
            List<String> clubs = getStudentClubs(matric_number);
            StringBuilder result = new StringBuilder("Your Co-curricular Clubs:\n");
            result.append("============================================================================\n");

            // Initialize the list of club transcripts
            clubTranscripts.clear();

            for (String clubCode : clubs) {
                // Fetch club details
                ClubDetails clubDetails = getClubDetails(clubCode);
                result.append(clubDetails.getCategory()).append(": ").append(clubDetails.getClubCode())
                      .append(" - ").append(clubDetails.getClubName()).append("\n");

                // Store the club's transcript data
                clubTranscripts.add(new ClubTranscript(this, clubDetails, "None", "None", "None", "Member", 0));
                result.append("============================================================================\n");
            }

            result.append("Would you like to generate the transcript?\n");

            // Show the list of clubs in the result area
            resultArea.setText(result.toString());

            // Enable the generateTranscriptButton only after displaying clubs
            if (generateTranscriptButton != null) {
                generateTranscriptButton.setEnabled(true);
            }

            // If the button is clicked, generate the transcript
            generateTranscriptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Call the generateTranscript method when the button is clicked
                    generateTranscript(matric_number);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred.");
        }
    }

    // Method to handle generate transcript action
    public void generateTranscript(String matric_number) {
        try {
            StringBuilder result = new StringBuilder("Transcript for student ").append(matric_number).append("\n")
                    .append("Co-curriculum Transcript for ").append(matric_number).append("\n")
                    .append("============================================================================\n");

            double finalMarks = 0;
            int count = 0;

            // Sort the clubs based on total marks and pick the top 2
            clubTranscripts.sort((c1, c2) -> Integer.compare(c2.getTotalMarks(), c1.getTotalMarks()));

            for (int i = 0; i < Math.min(2, clubTranscripts.size()); i++) {
                ClubTranscript club = clubTranscripts.get(i);
                result.append(club.generateTranscript());
                finalMarks += club.getTotalMarks();
                count++;
            }

            finalMarks /= count;  // Calculate average marks

            result.append("============================================================================\n");
            result.append("FINAL MARKS: ").append(finalMarks).append(" marks\n");

            // Show the generated transcript in the result area
            resultArea.setText(result.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An unexpected error occurred while generating the transcript.");
        }
    }

    private List<String> getStudentClubs(String matric_number) throws SQLException, Exception {
        List<String> clubs = new ArrayList<>();
        String query = "SELECT cocurricular_clubs FROM users WHERE matric_number = ?";
        
        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matric_number);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String cocurricularClubs = rs.getString("cocurricular_clubs");
                    if (cocurricularClubs != null) {
                        clubs.addAll(Arrays.asList(cocurricularClubs.split(",")));
                    }
                }
            }
        }
        return clubs;
    }

    private ClubDetails getClubDetails(String clubCode) throws SQLException, Exception {
        String query = "SELECT club_name, category FROM clubs WHERE club_code = ?";
        try (Connection conn = DBHelper.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clubCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ClubDetails(clubCode, rs.getString("club_name"), rs.getString("category"));
                }
            }
        }
        return null;
    }

    private static class ClubDetails {
        private String clubCode;
        private String clubName;
        private String category;

        public ClubDetails(String clubCode, String clubName, String category) {
            this.clubCode = clubCode;
            this.clubName = clubName;
            this.category = category;
        }

        public String getClubCode() {
            return clubCode;
        }

        public String getClubName() {
            return clubName;
        }

        public String getCategory() {
            return category;
        }
    }

    private static class ClubTranscript {
        private ClubDetails clubDetails;
        private String selectedActivity;
        private String activityLevel;
        private String achievementLevel;
        private String position;
        private int totalMarks;

        private ClubsAction clubsAction;

        public ClubTranscript(ClubsAction clubsAction, ClubDetails clubDetails, String selectedActivity, String activityLevel, String achievementLevel, String position, int totalMarks) {
            this.clubsAction = clubsAction;
            this.clubDetails = clubDetails;
            this.selectedActivity = selectedActivity;
            this.activityLevel = activityLevel;
            this.achievementLevel = achievementLevel;
            this.position = position;
            this.totalMarks = totalMarks;
        }

        public int getTotalMarks() {
            return totalMarks;
        }

        public String generateTranscript() {
            return "[" + clubDetails.getClubCode() + " - " + clubDetails.getClubName() + "]\n" +
                   "Attendance: assume full ------------> 50/50 marks\n" +
                   "Position: " + position + " ------------> " + clubsAction.getPositionMarks(position) + "/10 marks\n" +
                   "Selected Activity: " + selectedActivity + "\n" +
                   "Level of Activities: " + activityLevel + " ------------> " + clubsAction.getActivityMarks(new Activity(selectedActivity, activityLevel, achievementLevel)) + "/20 marks\n" +
                   "Achievement Level: " + achievementLevel + " ------------> " + clubsAction.getAchievementMarks(new Activity(selectedActivity, activityLevel, achievementLevel)) + "/20 marks\n" +
                   "============================================================================\n" +
                   "TOTAL: " + totalMarks + "/100 marks\n" +
                   "============================================================================\n";
        }
    }

    private static class Activity {
        private String activityName;
        private String activityLevel;
        private String achievementLevel;

        public Activity(String activityName, String activityLevel, String achievementLevel) {
            this.activityName = activityName;
            this.activityLevel = activityLevel;
            this.achievementLevel = achievementLevel;
        }

        public String getActivityName() {
            return activityName;
        }

        public String getActivityLevel() {
            return activityLevel;
        }

        public String getAchievementLevel() {
            return achievementLevel;
        }
    }
}
