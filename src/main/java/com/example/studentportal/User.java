package com.example.studentportal;

import java.util.List;

public class User {
    private String email;
    private int matricNumber;
    private String password;
    private List<String> academicSubjects;
    private List<String> coCurricularClubs;

    // Constructor
    public User(String email, int matricNumber, String password, List<String> academicSubjects, List<String> coCurricularClubs) {
        this.email = email;
        this.matricNumber = matricNumber;
        this.password = password;
        this.academicSubjects = academicSubjects;
        this.coCurricularClubs = coCurricularClubs;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMatricNumber() {
        return matricNumber;
    }

    public void setMatricNumber(int matricNumber) {
        this.matricNumber = matricNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAcademicSubjects() {
        return academicSubjects;
    }

    public void setAcademicSubjects(List<String> academicSubjects) {
        this.academicSubjects = academicSubjects;
    }

    public List<String> getCoCurricularClubs() {
        return coCurricularClubs;
    }

    public void setCoCurricularClubs(List<String> coCurricularClubs) {
        this.coCurricularClubs = coCurricularClubs;
    }

    // Display User Information (optional method)
    public void displayUserInfo() {
        System.out.println("Email: " + email);
        System.out.println("Matric Number: " + matricNumber);
        System.out.println("Academic Subjects: " + academicSubjects);
        System.out.println("Co-Curricular Clubs: " + coCurricularClubs);
    }
}

