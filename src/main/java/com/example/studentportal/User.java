package com.example.studentportal;

import java.util.List;

public class User {
    private String email;
    private final int matricNumber;
    private final String password;
    private List<String> academicSubjects;
    private List<String> coCurricularClubs;
    private byte[] salt;

    public User(String email, int matricNumber, String password, List<String> academicSubjects, List<String> coCurricularClubs, byte[] salt) {
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

    public void setEmail(String email){
        if (email.contains("@siswa.um.edu.my")){
            this.email = email;
        }
        else{
            System.out.println("Invalid email format. Please enter a valid email.");
        }
    } 

   @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", matricNumber=" + matricNumber +
                ", password='" + password + '\'' +
                ", academicSubjects=" + academicSubjects +
                ", coCurricularClubs=" + coCurricularClubs +
                '}';
    } 
}