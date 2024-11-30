package com.example.studentportal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class coCurriculumMark {

    static int totalMark = 0;

    public static void fetchUser() {
        try (BufferedReader r = new BufferedReader(new FileReader("user.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String email = line;
                String matricNumber = r.readLine();
                String password = r.readLine();
                String academicSubjects = r.readLine();
                String coCurricularClubs = r.readLine();
                String salt = r.readLine(); // Assuming the salt is on the next line, if not, remove this line

                if (email != null && matricNumber != null && password != null && academicSubjects != null && coCurricularClubs != null) {
                    // Process the user data
                } else {
                    System.out.println("Invalid user entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file");
        }
    }

    public static void GetUserCocurriculum() {
        Map<String, String> clubMap = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new FileReader("coCurricularClubs.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    clubMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading co-curricular clubs file");
        }

        try (BufferedReader r = new BufferedReader(new FileReader("user.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String email = line;
                String matricNumber = r.readLine();
                String password = r.readLine();
                String academicSubjects = r.readLine();
                String coCurricularClubs = r.readLine();
                String salt = r.readLine(); // Assuming the salt is on the next line, if not, remove this line

                if (email != null && matricNumber != null && password != null && academicSubjects != null && coCurricularClubs != null) {
                    String[] clubCodes = coCurricularClubs.split(",");
                    StringBuilder transcript = new StringBuilder();
                    transcript.append("Your Cocurricular Clubs:\n");
                    transcript.append("============================================================================\n");
                    for (String code : clubCodes) {
                        String clubName = clubMap.get(code);
                        if (clubName != null) {
                            if (code.startsWith("P")) {
                                transcript.append("Societies: ").append(code).append(" - ").append(clubName).append("\n");
                            } else if (code.startsWith("B")) {
                                transcript.append("Uniform Body: ").append(code).append(" - ").append(clubName).append("\n");
                            } else if (code.startsWith("S")) {
                                transcript.append("Sports Club: ").append(code).append(" - ").append(clubName).append("\n");
                            }
                        } else {
                            System.out.println("Club code " + code + " not found");
                        }
                    }
                    transcript.append("============================================================================\n");
                    System.out.println(transcript.toString());
                } else {
                    System.out.println("Invalid user entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file");
        }
    }

    public static int positionMark(String position) {
        int mark = 0;
        switch (position) {
            case "President":
                mark = 10;
                break;
            case "Vice President":
                mark = 8;
                break;
            case "Secretary":
                mark = 7;
                break;
            case "Vice Secretary":
                mark = 6;
                break;
            case "Active Member":
                mark = 6;
                break;
            default:
                mark = 0;
                break;
        }
        return mark;
    }

    public static int AchievementLevelMark(String achievementLevel) {
        int mark = 0;
        switch (achievementLevel) {
            case "Gold":
                mark = 20;
                break;
            case "Silver":
                mark = 19;
                break;
            case "Bronze":
                mark = 18;
                break;
            case "Participation":
                mark = 0;
                break;
            default:
                mark = 0;
                break;
        }
        return mark;
    }

    public static void readActivitiesLog() {
        try (BufferedReader r = new BufferedReader(new FileReader("activitiesLog.txt"))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String studentId = parts[0];
                    String activityCode = parts[1];
                    String activityName = parts[2];
                    String levelOfActivities = parts[3];
                    String achievementLevel = parts[4];
                    int mark = positionMark(activityCode) + activitiesParticipatedMark(levelOfActivities) + AchievementLevelMark(achievementLevel);
                    totalMark += mark;
                } else {
                    System.out.println("Invalid log entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading activities log");
        }
    }

    public static int activitiesParticipatedMark(String levelofActivities) {
        int mark = 0;
        switch (levelofActivities) {
            case "International":
                mark = 20;
                break;
            case "National":
                mark = 15;
                break;
            case "State":
                mark = 12;
                break;
            case "School":
                mark = 10;
                break;
            default:
                mark = 0;
                break;
        }
        return mark;
    }

    public static void main(String[] args) {
        GetUserCocurriculum();
    }
}
