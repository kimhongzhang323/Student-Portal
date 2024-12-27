package com.example.studentportal;

public class Activity {
    private String activity;
    private String clubCode;
    private String description;

    // Constructor
    public Activity(String activity, String clubCode, String description) {
        this.activity = activity;
        this.clubCode = clubCode;
        this.description = description;
    }

    // Getter for the activity name
    public String getActivity() {
        return activity;
    }

    // Getter for the club code
    public String getClubCode() {
        return clubCode;
    }

    // Getter for the description or role in the club
    public String getDescription() {
        return description;
    }

    // Optional: Add setters if needed
}
