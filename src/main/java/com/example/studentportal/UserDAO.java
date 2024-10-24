package com.example.studentportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class UserDAO{
    private Connection connection;

    public UserDAO(Connection connection){
        this.connection = connection;
    }
    // Implement the signUp method
    public boolean signUp(User user) throws SQLException {
        // Implement the signUp method
        String sql = "INSERT INTO users (email, matric_number, password,academic_subjects,co_curricular_clubs) VALUES (?, ?, ?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setInt(2, user.getMatricNumber());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getAcademicSubjects().toString());
        preparedStatement.setString(5, user.getCoCurricularClubs().toString());

        int rows = preparedStatement.executeUpdate();
        if(rows > 0){
            return true;
        }
        return false;
    }

    // Implement the userExists method
    public boolean userExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // If count is greater than 0, user exists
            }
        }
        return false;
    }
    
    // Implement the login method
    public User login(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);   // Set the email parameter
            statement.setString(2, password); // Set the password parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Create and return User object based on the retrieved data
                return new User(
                    resultSet.getString("email"),
                    resultSet.getInt("matric_number"),
                    resultSet.getString("password"),
                    Arrays.asList(resultSet.getString("academic_subjects").split(",")),
                    Arrays.asList(resultSet.getString("co_curricular_clubs").split(","))
                );
            }
        }
        return null; // Return null if no user is found
    }
}