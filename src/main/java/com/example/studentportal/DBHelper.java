    package com.example.studentportal;

    import java.sql.Connection;
    import java.sql.DatabaseMetaData;
    import java.sql.DriverManager;
    import java.sql.ResultSet;

    public class DBHelper {

        private static final String DB_URL = "jdbc:mysql://localhost:3306/student_info";
        private static final String USER = "root";
        private static final String PASS = "buku3b3B";        

        public static Connection getConnection() throws Exception {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (Exception e) {
                throw new Exception("Error getting database connection", e);
            }
        }

        public static void main(String[] args) {
            try {
                Connection connection = getConnection();
                System.out.println("Connected to the database!");

                // Get database metadata
                DatabaseMetaData metaData = connection.getMetaData();

                // Get tables in the database
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[] {"TABLE"});

                // Process the result set and print all table names
                System.out.println("Tables in the database:");
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    System.out.println(tableName);
                }

                // Close connections
                resultSet.close();
                connection.close();
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
