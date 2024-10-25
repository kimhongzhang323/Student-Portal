package com.example.studentportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Data Access Object (DAO) class for managing user-related database operations.
 * This class provides methods for user signup, login, and existence check.
 */
public class UserDAO {
    private Connection connection;

    /**
     * Constructor to initialize UserDAO with a database connection.
     *
     * @param connection the database connection
     */
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Signs up a new user by inserting their information into the database.
     *
     * @param user the User object containing user details
     * @return true if the user was successfully added; false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean signUp(User user) throws SQLException {
        String sql = "INSERT INTO users (email, matric_number, password, academic_subjects, co_curricular_clubs) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setInt(2, user.getMatricNumber());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getAcademicSubjects().toString());
        preparedStatement.setString(5, user.getCoCurricularClubs().toString());

        int rows = preparedStatement.executeUpdate();
        return rows > 0; // Return true if at least one row was affected
    }

    /**
     * Checks if a user with the specified email already exists in the database.
     *
     * @param email the email to check
     * @return true if the user exists; false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean userExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // If count is greater than 0, user exists
            }
        }
        return false; // User does not exist
    }

    /**
     * Logs in a user by checking their email and password against the database.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return a User object if login is successful; null otherwise
     * @throws SQLException if a database access error occurs
     */
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
