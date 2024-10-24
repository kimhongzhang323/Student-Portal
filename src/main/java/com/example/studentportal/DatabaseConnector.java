package com.example.studentportal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    // Load database configuration from a properties file in the classpath
    static {
        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Sorry, unable to find db.properties");
                throw new IllegalStateException("Sorry, unable to find db.properties");
            }
            Properties prop = new Properties();
            prop.load(input);
            DB_URL = prop.getProperty("db.url");
            DB_USER = prop.getProperty("db.user");
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("Error loading database configuration!");
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
