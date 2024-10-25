package com.example.studentportal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the student portal.
 * This class holds user information and provides methods to manage user data.
 */
public class User {
    private String email;
    private String name;
    private int matricNumber;
    private String password;
    private List<String> academicSubjects;
    private List<String> coCurricularClubs;

    /**
     * Constructor to initialize a User object.
     *
     * @param email              the user's email address
     * @param matricNumber       the user's matric number
     * @param password           the user's password
     * @param academicSubjects    list of academic subjects the user is enrolled in
     * @param coCurricularClubs   list of co-curricular clubs the user is part of
     */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name; // Allows setting the user's name
    }

    public void setEmail(String email) {
        this.email = email; // Allows updating the user's email
    }

    public int getMatricNumber() {
        return matricNumber;
    }

    public void setMatricNumber(int matricNumber) {
        this.matricNumber = matricNumber; // Allows updating the user's matric number
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password; // Allows updating the user's password
    }

    public List<String> getAcademicSubjects() {
        return academicSubjects; // Returns the list of academic subjects
    }

    public void setAcademicSubjects(List<String> academicSubjects) {
        this.academicSubjects = academicSubjects; // Allows updating the list of academic subjects
    }

    public List<String> getCoCurricularClubs() {
        return coCurricularClubs; // Returns the list of co-curricular clubs
    }

    public void setCoCurricularClubs(List<String> coCurricularClubs) {
        this.coCurricularClubs = coCurricularClubs; // Allows updating the list of co-curricular clubs
    }

    /**
     * Displays user information in a readable format.
     */
    public void displayUserInfo() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Matric Number: " + matricNumber);
        System.out.println("Academic Subjects: " + academicSubjects);
        System.out.println("Co-Curricular Clubs: " + coCurricularClubs);
    }

    /**
     * Fetches users from the database and displays their information.
     * This method establishes a database connection, executes a query to retrieve user data,
     * and creates User objects based on the result set.
     */
    public void fetchUsers() {
        DatabaseConnector connector = new DatabaseConnector();
        List<User> userList = new ArrayList<>(); // List to store fetched users

        try (Connection connection = connector.getConnection()) {
            // Define the SQL query to fetch users
            String query = "SELECT email, name, matric_number, password FROM users"; // Adjust the query based on your table structure
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                // Iterate through the result set and create User objects
                while (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String name = resultSet.getString("name");
                    int matricNumber = resultSet.getInt("matric_number");
                    String password = resultSet.getString("password");

                    // Create a User object and add it to the userList
                    User user = new User(email, matricNumber, password, new ArrayList<>(), new ArrayList<>());
                    userList.add(user);
                }

                System.out.println("Users fetched from the database successfully.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }

        // Optionally, you can return the list of users or print them
        System.out.println("Fetched Users:");
        for (User user : userList) {
            user.displayUserInfo(); // Display user information
        }
    }
}
