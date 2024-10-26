package com.example.studentportal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The ExportToExcel class is responsible for exporting data from a specified
 * database table into an Excel file format using Apache POI library.
 */
public class ExportToExcel {

    /**
     * Exports the contents of a database table to an Excel file.
     *
     * @param connection the database connection used to retrieve data
     * @param tableName  the name of the database table to export
     * @param filePath   the path where the Excel file will be saved
     */
    public static void exportTableToExcel(Connection connection, String tableName, String filePath) {
        // SQL query to select all data from the specified table
        String query = "SELECT * FROM " + tableName;

        // Try-with-resources to ensure proper resource management
        try (Statement statement = connection.createStatement(); // Create a Statement for executing the query
             ResultSet resultSet = statement.executeQuery(query); // Execute the query and obtain results
             Workbook workbook = new XSSFWorkbook()) { // Create a new Excel workbook

            // Create a new sheet in the workbook with the same name as the table
            Sheet sheet = workbook.createSheet(tableName);

            // Write the header row based on column names from the ResultSet
            Row headerRow = sheet.createRow(0); // Create the first row for headers
            int columnCount = resultSet.getMetaData().getColumnCount(); // Get the number of columns
            for (int col = 1; col <= columnCount; col++) {
                Cell cell = headerRow.createCell(col - 1); // Create a cell for each column
                cell.setCellValue(resultSet.getMetaData().getColumnName(col)); // Set the column name as cell value
            }

            // Write data rows from the ResultSet into the Excel sheet
            int rowCount = 1; // Start from the second row since the first row is the header
            while (resultSet.next()) { // Iterate through the ResultSet
                Row row = sheet.createRow(rowCount++); // Create a new row for each record
                for (int col = 1; col <= columnCount; col++) {
                    Cell cell = row.createCell(col - 1); // Create a cell for each column in the row
                    cell.setCellValue(resultSet.getString(col)); // Set the cell value from the ResultSet
                }
            }

            // Write the workbook to the specified file path
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) { // Create a FileOutputStream to write to file
                workbook.write(fileOut); // Write the workbook contents to the file
            }
            System.out.println("Data successfully exported to Excel file."); // Confirm successful export

        } catch (SQLException e) { // Handle SQL exceptions
            System.err.println("SQL error during Excel export: " + e.getMessage());
        } catch (IOException e) { // Handle I/O exceptions
            System.err.println("File I/O error during Excel export: " + e.getMessage());
        }
    }
}
