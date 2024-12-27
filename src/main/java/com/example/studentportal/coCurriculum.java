package com.example.studentportal;

import java.io.*;
import java.util.*;

public class CoCurriculum {
    public static void main(String[] args) {
        String studentMatricNumber = "s100201"; // Example student
        Map<String, String> studentClubs = getStudentClubs(studentMatricNumber); // Get clubs for the student
        Map<String, Integer> studentPositions = getStudentPositions(studentMatricNumber); // Get student positions
        List<Activity> activities = getStudentActivities(studentMatricNumber); // Get student's activities

        // Calculate marks for each club
        Map<String, Double> clubMarks = new HashMap<>();
        for (String clubCode : studentClubs.keySet()) {
            double marks = calculateClubMarks(clubCode, studentPositions.get(clubCode), activities);
            clubMarks.put(clubCode, marks);
        }

        // Print transcript for the student
        printTranscript(studentMatricNumber, clubMarks);
    }

    // Get the clubs the student is involved in, from the ClubSocieties.txt
    private static Map<String, String> getStudentClubs(String studentMatricNumber) {
        Map<String, String> studentClubs = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("ClubSocieties.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String clubCode = parts[0].trim();
                String clubName = parts[1].trim();
                if (isClubJoinedByStudent(studentMatricNumber, clubCode)) {
                    studentClubs.put(clubCode, clubName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentClubs;
    }

    // Check if the student is part of this club
    private static boolean isClubJoinedByStudent(String studentMatricNumber, String clubCode) {
        // Simulating that the student has joined these clubs
        return true; // Replace with real check from user data
    }

    // Get student positions from StudentPositions.txt
    private static Map<String, Integer> getStudentPositions(String studentMatricNumber) {
        Map<String, Integer> studentPositions = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("StudentPositions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String matricNumber = parts[0].trim();
                if (matricNumber.equals(studentMatricNumber)) {
                    studentPositions.put("P82", getPositionMarks(parts[1].trim())); // Position for P82
                    studentPositions.put("B07", getPositionMarks(parts[2].trim())); // Position for B07
                    studentPositions.put("S01", getPositionMarks(parts[3].trim())); // Position for S01
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentPositions;
    }

    // Convert position to marks
    private static int getPositionMarks(String position) {
        switch (position) {
            case "President": return 10;
            case "Vice President": return 9;
            case "Secretary": return 9;
            case "Treasurer": return 9;
            case "Active Member": return 6;
            case "Committee": return 7;
            default: return 0; // Invalid or no position
        }
    }

    // Get student's activities from ActivitiesLog.txt
    private static List<Activity> getStudentActivities(String studentMatricNumber) {
        List<Activity> activities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("ActivitiesLog.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String matricNumber = parts[0].trim();
                if (matricNumber.equals(studentMatricNumber)) {
                    String clubCode = parts[1].trim();
                    String activityLevel = parts[3].trim();
                    String achievementLevel = parts[4].trim();
                    activities.add(new Activity(clubCode, activityLevel, achievementLevel));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activities;
    }

    // Calculate marks for a specific club
    private static double calculateClubMarks(String clubCode, int positionMarks, List<Activity> activities) {
        // Default attendance marks
        double totalMarks = 50;

        // Get the activity for this club
        Activity activity = activities.stream()
                .filter(a -> a.getClubCode().equals(clubCode))
                .findFirst()
                .orElse(null);

        // Add position marks
        totalMarks += positionMarks;

        if (activity != null) {
            // Add activity level marks
            totalMarks += getActivityLevelMarks(activity.getActivityLevel());

            // Add achievement marks
            totalMarks += getAchievementLevelMarks(activity.getAchievementLevel());
        }

        return totalMarks;
    }

    // Convert activity level to marks
    private static int getActivityLevelMarks(String level) {
        switch (level) {
            case "International": return 20;
            case "National": return 15;
            case "State": return 12;
            case "School": return 10;
            default: return 0; // Invalid or no activity
        }
    }

    // Convert achievement level to marks
    private static int getAchievementLevelMarks(String achievement) {
        switch (achievement) {
            case "Gold": return 20;
            case "Silver": return 19;
            case "Bronze": return 18;
            default: return 0; // Participation or no achievement
        }
    }

    // Print the transcript for the student
    private static void printTranscript(String studentMatricNumber, Map<String, Double> clubMarks) {
        System.out.println("Co-curriculum Transcript for " + studentMatricNumber);
        System.out.println("============================================================================");
        
        List<Map.Entry<String, Double>> clubMarksList = new ArrayList<>(clubMarks.entrySet());
        clubMarksList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder())); // Sort by marks, descending

        for (Map.Entry<String, Double> entry : clubMarksList) {
            String clubCode = entry.getKey();
            double marks = entry.getValue();
            System.out.println("[" + clubCode + "] TOTAL: " + marks + "/100 marks");
        }

        // Calculate final marks (average of top 2 clubs)
        double finalMarks = 0;
        int count = 0;
        for (int i = 0; i < 2 && i < clubMarksList.size(); i++) {
            finalMarks += clubMarksList.get(i).getValue();
            count++;
        }
        finalMarks /= count;
        
        System.out.println("============================================================================");
        System.out.println("FINAL MARKS: " + finalMarks + " marks");
    }

    // Activity class to hold activity information
    static class Activity {
        private String clubCode;
        private String activityLevel;
        private String achievementLevel;

        public Activity(String clubCode, String activityLevel, String achievementLevel) {
            this.clubCode = clubCode;
            this.activityLevel = activityLevel;
            this.achievementLevel = achievementLevel;
        }

        public String getClubCode() {
            return clubCode;
        }

        public String getActivityLevel() {
            return activityLevel;
        }

        public String getAchievementLevel() {
            return achievementLevel;
        }
    }
}
