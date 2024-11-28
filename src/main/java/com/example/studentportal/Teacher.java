package com.example.studentportal;

import java.util.List;
import java.util.Map;

public class Teacher extends User {
    private List<String> subjectsTaught; 
    private Map<Integer, List<Integer>> studentGrades; 

    public Teacher(String email, int matricNumber, String password, List<String> academicSubjects, 
                   List<String> coCurricularClubs, List<String> subjectsTaught) {
        super(email, matricNumber, password, academicSubjects, coCurricularClubs, new byte[0]);
        this.subjectsTaught = subjectsTaught;
    }

    public List<String> getSubjectsTaught() {
        return subjectsTaught;
    }
}
