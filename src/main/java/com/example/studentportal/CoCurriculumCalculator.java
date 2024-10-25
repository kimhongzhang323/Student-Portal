package com.example.studentportal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Activity {
    String matricNumber;
    String clubCode;
    String activityName;
    String activityLevel;
    String achievement;

    public Activity(String matricNumber, String clubCode, String activityName, String activityLevel, String achievement) {
        this.matricNumber = matricNumber;
        this.clubCode = clubCode;
        this.activityName = activityName;
        this.activityLevel = activityLevel;
        this.achievement = achievement;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public String getClubCode() {
        return clubCode;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getActivityName() {
        return activityName;
    }
}

class StudentPosition {
    String matricNumber;
    List<String> positions;

    public StudentPosition(String matricNumber, List<String> positions) {
        this.matricNumber = matricNumber;
        this.positions = positions;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public List<String> getPositions() {
        return positions;
    }
}

public class CoCurriculumCalculator {

    private Map<String, Integer> positionMarks = new HashMap<>();
    private Map<String, Integer> activityLevelMarks = new HashMap<>();
    private Map<String, Integer> achievementLevelMarks = new HashMap<>();

    private static final int MAX_ATTENDANCE = 50;

    public CoCurriculumCalculator() {
        loadMarksReference("positions.csv", positionMarks);
        loadMarksReference("activity_levels.csv", activityLevelMarks);
        loadMarksReference("achievements.csv", achievementLevelMarks);
    }

    private void loadMarksReference(String filename, Map<String, Integer> marksMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) {
                    System.err.println("Invalid line format: " + line);
                    continue; // Skip this line
                }
                String key = parts[1].trim(); // Adjust this based on your CSV format
                try {
                    int marks = Integer.parseInt(parts[2].trim()); // Getting the marks
                    marksMap.put(key, marks);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for key: " + key + " with value: " + parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
        }
    }
    

    private static List<Activity> readActivities(String filename) throws IOException {
        List<Activity> activities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.err.println("Invalid line format: " + line);
                    continue; // Skip this line
                }
                activities.add(new Activity(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        }
        return activities;
    }

    private static List<StudentPosition> readPositions(String filename) throws IOException {
        List<StudentPosition> positions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    System.err.println("Invalid line format: " + line);
                    continue; // Skip this line
                }
                List<String> positionList = Arrays.asList(parts).subList(1, parts.length);
                positions.add(new StudentPosition(parts[0], positionList));
            }
        }
        return positions;
    }

    public void calculateMarks(String activitiesFile, String positionsFile) throws IOException {
        List<Activity> activities = readActivities(activitiesFile);
        List<StudentPosition> positions = readPositions(positionsFile);

        // Create a map to store the marks for each student
        Map<String, List<Integer>> studentMarks = new HashMap<>();
        // Calculate marks for each student
        for (StudentPosition sp : positions) {
            String matricNumber = sp.getMatricNumber();
            List<Integer> marksList = new ArrayList<>();

            // Calculate marks for each club
            for (String position : sp.getPositions()) {
                int totalMarks = calculateClubMarks(matricNumber, position, activities);
                marksList.add(totalMarks);
            }

            studentMarks.put(matricNumber, marksList);
            generateTranscript(matricNumber, marksList);
        }
    }

    private int calculateClubMarks(String matricNumber, String clubCode, List<Activity> activities) {
        // Initialize marks
        int attendanceMarks = MAX_ATTENDANCE; // Assuming full attendance
        int positionMarksScore = getPositionMarks(matricNumber, clubCode);

        int activityMarks = 0;
        int achievementMarks = 0;
        String selectedActivityName = "None";
        String selectedLevel = "None";
        String selectedAchievement = "None";

        // Get activity details for the student in the specific club
        for (Activity activity : activities) {
            if (activity.getMatricNumber().equals(matricNumber) && activity.getClubCode().equals(clubCode)) {
                selectedActivityName = activity.getActivityName();
                selectedLevel = activity.getActivityLevel();
                selectedAchievement = activity.getAchievement();
                activityMarks = activityLevelMarks.getOrDefault(selectedLevel, 0);
                achievementMarks = achievementLevelMarks.getOrDefault(selectedAchievement, 0);
                break; // We only expect one activity per club
            }
        }

        // Total marks calculation
        int totalMarks = attendanceMarks + positionMarksScore + activityMarks + achievementMarks;
        return totalMarks;
    }

    private int getPositionMarks(String matricNumber, String clubCode) {
        // Retrieve the position for the given student in the specific club
        // For simplicity, let's assume you can derive it from the student's positions
        return positionMarks.getOrDefault("Committee", 0); // Default value
    }

    private void generateTranscript(String matricNumber, List<Integer> marksList) {
        System.out.println("Transcript for student " + matricNumber);
        System.out.println("============================================================");
        for (int i = 0; i < marksList.size(); i++) {
            System.out.println("Club " + (i + 1) + " Total Marks: " + marksList.get(i) + "/100");
        }

        // Calculate final marks (average of top 2 scores)
        if (marksList.size() >= 2) {
            Collections.sort(marksList, Collections.reverseOrder());
            double finalMarks = (marksList.get(0) + marksList.get(1)) / 2.0;
            System.out.printf("FINAL MARKS: %.2f marks\n", finalMarks);
        } else {
            System.out.println("Not enough marks to calculate final marks.");
        }
        System.out.println("============================================================\n");
    }

    public static void main(String[] args) {
        try {
            CoCurriculumCalculator calculator = new CoCurriculumCalculator();
            calculator.calculateMarks("ActivitiesLog.txt", "StudentPositions.txt");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
