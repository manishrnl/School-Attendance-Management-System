package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.StageManaging.FileChooserFunction;
import com.example.studentattendanceandmanagementsystem.TableView.MonthlyReportTable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

public class Monthly_Report_Controller {

    @FXML
    private TextField yearField;
    @FXML
    private Text totaRecords;
    @FXML
    private ComboBox<String> UserIDCombo;
    @FXML
    private ComboBox<String> classComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private TableView<MonthlyReportTable> monthlyReportTable;
    @FXML
    private TableColumn<MonthlyReportTable, String> studentNameColumn, classColumn, monthColumn;
    @FXML
    private TableColumn<MonthlyReportTable, Integer> studentIdColumn, totalDaysColumn, presentDaysColumn;
    @FXML
    private TableColumn<MonthlyReportTable, Double> attendancePercentColumn;
    @FXML
    private Text errorText;

    private String query;
    private final ObservableList<MonthlyReportTable> data = FXCollections.observableArrayList();
    private final List<String> monthNames = Arrays.asList(
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
    );

    @FXML
    void initialize() {
        // Set up month combo box
        monthComboBox.setItems(FXCollections.observableArrayList(monthNames));
        
        // Default to current year
        yearField.setText(String.valueOf(Year.now().getValue()));
        
        // Add listeners
        monthComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !classComboBox.getSelectionModel().isEmpty()) {
                loadDataIntoTable();
            }
        });
        
        classComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !monthComboBox.getSelectionModel().isEmpty()) {
                loadDataIntoTable();
            }
        });
        
        yearField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && 
                    !classComboBox.getSelectionModel().isEmpty() && 
                    !monthComboBox.getSelectionModel().isEmpty()) {
                try {
                    Integer.parseInt(newValue);
                    loadDataIntoTable();
                } catch (NumberFormatException e) {
                    setMessage("RED", "Please enter a valid year");
                }
            }
        });
        
        // Set up table columns
        studentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIdProperty().asObject());
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().classProperty());
        monthColumn.setCellValueFactory(cellData -> cellData.getValue().monthProperty());
        totalDaysColumn.setCellValueFactory(cellData -> cellData.getValue().totalDaysProperty().asObject());
        presentDaysColumn.setCellValueFactory(cellData -> cellData.getValue().presentDaysProperty().asObject());
        
        // For the percentage column
        attendancePercentColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAttendancePercentage()).asObject());

        attendancePercentColumn.setCellFactory(column -> new TableCell<MonthlyReportTable, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f%%", item));
                }
            }
        });
    }

    private void loadDataIntoTable() {
        String classSelected = classComboBox.getValue();
        String monthSelected = monthComboBox.getValue();
        String yearValue = yearField.getText();
        
        if (classSelected == null || monthSelected == null || yearValue.isEmpty()) {
            setMessage("RED", "Please select class, month and year");
            return;
        }
        
        int year;
        try {
            year = Integer.parseInt(yearValue);
        } catch (NumberFormatException e) {
            setMessage("RED", "Please enter a valid year");
            return;
        }
        
        int monthNumber = monthNames.indexOf(monthSelected) + 1;
        
        data.clear();

        // Query to get monthly attendance data using user_id
        query = "SELECT a.user_id, s.full_name AS student_name, a.classes, " +
                "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_days, " +
                "COUNT(*) AS total_days FROM Attendance a JOIN Students s ON a.user_id = s.user_id " +
                "WHERE a.classes = ? AND MONTH(a.AttandanceDate) = ? AND YEAR(a.AttandanceDate) = ? " +
                "GROUP BY a.user_id, s.full_name, a.classes";
                
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, classSelected);
            pstmt.setInt(2, monthNumber);
            pstmt.setInt(3, year);
            
            ResultSet rs = pstmt.executeQuery();
            int recordCount = 0;

            while (rs.next()) {
                recordCount++;
                int userId = rs.getInt("user_id");
                String studentName = rs.getString("student_name");
                String classes = rs.getString("classes");
                int presentDays = rs.getInt("present_days");
                int totalDays = rs.getInt("total_days");

                // Calculate percentage
                double attendancePercentage = 0;
                if (totalDays > 0) {
                    attendancePercentage = ((double) presentDays / totalDays) * 100;
                }

                MonthlyReportTable report = new MonthlyReportTable(
                        userId, studentName, classes, monthSelected, totalDays, presentDays, attendancePercentage
                );
                data.add(report);
            }
            
            monthlyReportTable.setItems(data);
            totaRecords.setText("Total Records: " + recordCount);
            
            if (recordCount > 0) {
                setMessage("GREEN", "Loaded monthly attendance for " + classSelected + " - " + monthSelected + " " + year);
            } else {
                setMessage("RED", "No attendance records found for " + classSelected + " - " + monthSelected + " " + year);
            }

        } catch (SQLException e) {
            setMessage("RED", "Database error: " + e.getMessage());
        }
    }

    @FXML
    void handleExportToExcel(ActionEvent event) {
        if (data.isEmpty()) {
            setMessage("RED", "No data to export");
            return;
        }
        
        File file = FileChooserFunction.chooseSaveLocation(
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                "Monthly-Attendance-" + classComboBox.getValue() + "-" + 
                        monthComboBox.getValue() + "-" + yearField.getText() + ".xlsx"
        );

        if (file == null) {
            return; // User cancelled
        }

        String filePath = file.getAbsolutePath();

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Monthly Attendance");

            ObservableList<?> items = monthlyReportTable.getItems();
            ObservableList<TableColumn<MonthlyReportTable, ?>> columns = monthlyReportTable.getColumns();

            // Write header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < columns.size(); col++) {
                String columnHeader = columns.get(col).getText();
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columnHeader);
            }

            // Write data rows
            for (int rowIndex = 0; rowIndex < items.size(); rowIndex++) {
                Row excelRow = sheet.createRow(rowIndex + 1);
                Object rowItem = items.get(rowIndex);

                for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
                    TableColumn column = columns.get(colIndex);
                    Object cellData = column.getCellObservableValue(rowItem).getValue();
                    String cellValue = cellData == null ? "" : cellData.toString();

                    Cell excelCell = excelRow.createCell(colIndex);
                    excelCell.setCellValue(cellValue);
                }
            }

            // Auto-size columns
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            workbook.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText(null);
            alert.setContentText("Data exported to " + filePath);
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred during export: " + e.getMessage());
            alert.showAndWait();
        }
    }

    void setMessage(String color, String message) {
        errorText.setText(message);
        errorText.setStyle("-fx-fill:" + color + ";");
    }


}
