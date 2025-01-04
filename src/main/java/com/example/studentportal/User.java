package com.example.studentportal;

import java.util.List;

public class User {
    private String email;
    private final String matricNumber;
    private final String password;
    private List<String> academicSubjects;
    private List<String> coCurricularClubs;
    private byte[] salt;

    public User(String email, String matricNumber, String password, List<String> academicSubjects, List<String> coCurricularClubs, byte[] salt) {
        this.email = email;
        this.matricNumber = matricNumber;
        this.password = password;
        this.academicSubjects = academicSubjects;
        this.coCurricularClubs = coCurricularClubs;
        this.salt = salt;
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

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
    
}