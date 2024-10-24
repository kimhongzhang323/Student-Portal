package com.example.studentportal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_info";
    private static final String DB_USER = "your_username"; // Keep this if you want to hardcode the username

    public static Connection connect() {
        String dbPassword = System.getenv("DB_PASSWORD"); // Use environment variable for password

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, dbPassword);
            System.out.println("Connection established!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
