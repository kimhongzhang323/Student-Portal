package com.example.studentportal;

import java.util.List;
import java.util.Map;

/**
 * Teacher class extends the User class and provides functionalities
 * specific to teachers, such as managing students and subjects.
 */
public class Teacher extends User {
    private List<String> subjectsTaught; // List of subjects taught by the teacher
    private Map<Integer, List<Integer>> studentGrades; // Mapping of student matric numbers to a list of grades

    /**
     * Constructor for Teacher class.
     *
     * @param email             the email of the teacher
     * @param matricNumber      the matric number of the teacher
     * @param password          the password of the teacher
     * @param academicSubjects  list of academic subjects
     * @param coCurricularClubs list of co-curricular clubs
     * @param subjectsTaught    list of subjects taught by the teacher
     */
    public Teacher(String email, int matricNumber, String password, List<String> academicSubjects, 
                   List<String> coCurricularClubs, List<String> subjectsTaught) {
        super(email, matricNumber, password, academicSubjects, coCurricularClubs, new byte[0]);
        this.subjectsTaught = subjectsTaught;
    }

    /**
     * Gets the list of subjects taught by the teacher.
     *
     * @return the list of subjects taught
     */
    public List<String> getSubjectsTaught() {
        return subjectsTaught;
    }

    /**
     * Sets the subjects taught by the teacher.
     *
     * @param subjectsTaught the list of subjects to set
     */
    public void setSubjectsTaught(List<String> subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }

    /**
     * Adds grades for a specific student.
     *
     * @param studentMatricNumber the matric number of the student
     * @param grades              the list of grades to add
     */
    public void addGrades(int studentMatricNumber, List<Integer> grades) {
        studentGrades.put(studentMatricNumber, grades);
        System.out.println("Grades added for student with matric number: " + studentMatricNumber);
    }

    /**
     * Retrieves grades of a specific student.
     *
     * @param studentMatricNumber the matric number of the student
     * @return the list of grades for the student, or null if no grades are found
     */
    public List<Integer> getGrades(int studentMatricNumber) {
        return studentGrades.getOrDefault(studentMatricNumber, null);
    }
}
