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

/**
 * Represents an activity that a student participates in.
 */
class Activity {
    String matricNumber; // Student's matriculation number
    String clubCode; // Code for the club
    String activityName; // Name of the activity
    String activityLevel; // Level of the activity
    String achievement; // Achievement related to the activity

    /**
     * Constructor for Activity.
     *
     * @param matricNumber  the student's matriculation number
     * @param clubCode      the code for the club
     * @param activityName  the name of the activity
     * @param activityLevel the level of the activity
     * @param achievement    the achievement related to the activity
     */
    public Activity(String matricNumber, String clubCode, String activityName, String activityLevel, String achievement) {
        this.matricNumber = matricNumber;
        this.clubCode = clubCode;
        this.activityName = activityName;
        this.activityLevel = activityLevel;
        this.achievement = achievement;
    }

    // Getters for Activity properties
    public String getMatricNumber() { return matricNumber; }
    public String getClubCode() { return clubCode; }
    public String getActivityLevel() { return activityLevel; }
    public String getAchievement() { return achievement; }
    public String getActivityName() { return activityName; }
}

/**
 * Represents a student's position in various activities.
 */
class StudentPosition {
    String matricNumber; // Student's matriculation number
    List<String> positions; // List of positions held by the student

    /**
     * Constructor for StudentPosition.
     *
     * @param matricNumber the student's matriculation number
     * @param positions    list of positions held by the student
     */
    public StudentPosition(String matricNumber, List<String> positions) {
        this.matricNumber = matricNumber;
        this.positions = positions;
    }

    // Getters for StudentPosition properties
    public String getMatricNumber() { return matricNumber; }
    public List<String> getPositions() { return positions; }
}

/**
 * Calculates co-curricular marks based on student activities and positions.
 */
public class CoCurriculumCalculator {

    // Maps to store marks references
    private Map<String, Integer> positionMarks = new HashMap<>();
    private Map<String, Integer> activityLevelMarks = new HashMap<>();
    private Map<String, Integer> achievementLevelMarks = new HashMap<>();

    private static final int MAX_ATTENDANCE = 50; // Maximum attendance marks

    /**
     * Constructor for CoCurriculumCalculator. Initializes marks references.
     */
    public CoCurriculumCalculator() {
        loadMarksReference("positions.csv", positionMarks);
        loadMarksReference("activity_levels.csv", activityLevelMarks);
        loadMarksReference("achievements.csv", achievementLevelMarks);
    }

    /**
     * Loads marks reference data from a CSV file into the provided map.
     *
     * @param filename   the name of the CSV file
     * @param marksMap   the map to store the loaded marks
     */
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
                String key = parts[1].trim(); // Key (position, activity level, or achievement)
                try {
                    int marks = Integer.parseInt(parts[2].trim()); // Marks value
                    marksMap.put(key, marks);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format for key: " + key + " with value: " + parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Reads activities from a CSV file.
     *
     * @param filename the name of the CSV file
     * @return a list of Activity objects
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Reads student positions from a CSV file.
     *
     * @param filename the name of the CSV file
     * @return a list of StudentPosition objects
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Calculates marks for students based on activities and positions.
     *
     * @param activitiesFile the filename containing activity records
     * @param positionsFile  the filename containing student position records
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Calculates total marks for a student in a specific club.
     *
     * @param matricNumber the student's matriculation number
     * @param clubCode     the club code
     * @param activities   the list of activities for the students
     * @return the total marks for the student in the club
     */
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

    /**
     * Retrieves the marks for a student's position in a specific club.
     *
     * @param matricNumber the student's matriculation number
     * @param clubCode     the club code
     * @return the marks for the position, or 0 if not found
     */
    private int getPositionMarks(String matricNumber, String clubCode) {
        // Retrieve the position for the given student in the specific club
        // For simplicity, let's assume you can derive it from the student's positions
        return positionMarks.getOrDefault("Committee", 0); // Default value
    }

    /**
     * Generates a transcript for a student and prints it to the console.
     *
     * @param matricNumber the student's matriculation number
     * @param marksList    the list of marks for the student
     */
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

    /**
     * Main method to run the CoCurriculumCalculator.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            CoCurriculumCalculator calculator = new CoCurriculumCalculator();
            calculator.calculateMarks("ActivitiesLog.txt", "StudentPositions.txt");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
