package com.example.studentportal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

public class Main {
    private static Instant lastUpdated = Instant.now(); 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 
        Main main = new Main();
        System.out.println("-------------------------");
        System.out.println("Welcome to the Student Portal!");
        System.out.println("-------------------------");
        System.out.println("Please select an option:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {
            System.out.println("Enter your email:");
            String email = sc.nextLine();
            System.out.println("Enter your password:");
            String password = sc.nextLine();
            main.Login(email, password);
        } else if (option == 2) {
            System.out.println("Enter your email:");
            String email = sc.nextLine();
            System.out.println("Enter your matric number:");
            int matricNumber = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter your password:");
            String password = sc.nextLine();
            main.Register(email, matricNumber, password);
        } else if (option == 3) {
            System.out.println("Exiting Student Portal...");
            System.exit(0);
        } else {
            System.out.println("Invalid option. Please select a valid option.");
        }

    }


    public boolean Login(String email, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"))) {
            String line = reader.readLine();

            while (line != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts[0].equals(email) && parts[2].equals(password)) {
                        System.out.println("Login successful!");
                        return true;
                    }
                    line = reader.readLine();
                } catch (IOException e) {
                    System.out.println("Error reading file");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        return false;
    }

    public void Register(String email, int matricNumber, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("UserData.txt"))) {
            String line = reader.readLine();

            while (line != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts[0].equals(email)) {
                        System.out.println("Email already exists. Please use a different email.");
                        return;
                    }
                    line = reader.readLine();
                } catch (IOException e) {
                    System.out.println("Error reading file");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        try {
            User newUser = new User(email, matricNumber, password, null, null, PasswordHashing.generateSalt());
        } catch (Exception e) {
            System.out.println("Error creating user");
        }
    }
}
