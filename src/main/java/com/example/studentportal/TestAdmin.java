package com.example.studentportal;

import java.util.Arrays;
import java.util.List;

public class TestAdmin {
    public static void main(String[] args) {
        // Setup database connection
        DatabaseConnector databaseConnector = new DatabaseConnector();

        // Create sample academic subjects and clubs
        List<String> subjects = Arrays.asList("Math", "Science");
        List<String> clubs = Arrays.asList("Chess Club", "Robotics Club");
        List<String> permissions = Arrays.asList("ADD_USER", "REMOVE_USER");

        // Create an Admin object
        Admin admin = new Admin("admin@example.com", 12345678, "adminPass123",
                subjects, clubs, permissions, databaseConnector, new byte[0]);

        // Test addUser method
        User newUser = new User("testuser@example.com", 87654321, "userPass123", subjects, clubs, new byte[0]);
        boolean isAdded = admin.addUser(newUser);
        System.out.println("User added: " + isAdded);

        // Test removeUser method
        boolean isRemoved = admin.removeUser("testuser@example.com");
        System.out.println("User removed: " + isRemoved);
    }
}
