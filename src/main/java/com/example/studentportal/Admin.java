package com.example.studentportal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Admin class extends the User class and provides additional
 * functionalities specific to administrative users.
 */
public class Admin extends User {
    private List<String> permissions; // List of administrative permissions
    private final DatabaseConnector databaseConnector; // Database connector for DB access

    /**
     * Constructor for Admin class.
     *
     * @param email             the email of the admin
     * @param matricNumber      the matric number of the admin
     * @param password          the password of the admin
     * @param academicSubjects  list of academic subjects
     * @param coCurricularClubs list of co-curricular clubs
     * @param permissions       list of admin-specific permissions
     * @param databaseConnector database connector for managing DB access
     */
    public Admin(String email, int matricNumber, String password, List<String> academicSubjects,
                 List<String> coCurricularClubs, List<String> permissions, DatabaseConnector databaseConnector, byte[] additionalData) {
        super(email, matricNumber, password, academicSubjects, coCurricularClubs, additionalData);
        this.permissions = permissions;
        this.databaseConnector = databaseConnector;
    }

    /**
     * Gets the list of admin permissions.
     *
     * @return the list of permissions
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * Sets the admin permissions.
     *
     * @param permissions list of permissions to set
     */
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * Adds a new user to the portal.
     *
     * @param user the user to add
     * @return true if the user is added successfully, false otherwise
     */
    public boolean addUser(User user) {
        String sql = "INSERT INTO users (email, matric_number, password, academic_subjects, co_curricular_clubs) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setInt(2, user.getMatricNumber());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, String.join(",", user.getAcademicSubjects()));
            preparedStatement.setString(5, String.join(",", user.getCoCurricularClubs()));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + user.getEmail() + " added by Admin.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception while adding user: " + e.getMessage());
        }
        return false;
    }

    /**
     * Removes a user from the portal.
     *
     * @param email the email of the user to remove
     * @return true if the user is removed successfully, false otherwise
     */
    public boolean removeUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User with email " + email + " removed by Admin.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception while removing user: " + e.getMessage());
        }
        return false;
    }
}
