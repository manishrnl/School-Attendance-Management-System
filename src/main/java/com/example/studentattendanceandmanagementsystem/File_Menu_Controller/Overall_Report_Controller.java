package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.StageManaging.FileChooserFunction;
import com.example.studentattendanceandmanagementsystem.TableView.OverallReportTable;
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

public class Overall_Report_Controller {
    @FXML
    private TableColumn<OverallReportTable, Double> attendancePercentColumn;
    @FXML
    private TableColumn<OverallReportTable, String> studentNameColumn, classColumn;
    @FXML
    private TableView<OverallReportTable> overallTable;
    @FXML
    private TableColumn<OverallReportTable, Integer> studentIdColumn, totalDaysColumn, presentDaysColumn;
    @FXML
    private Text errorText;
    @FXML
    private ComboBox<String> classComboBox;
    String query;
    int totalDays = 0, daysPresent = 0;
    Double Attandance = 0.0;
    ObservableList<OverallReportTable> data = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        classComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.toString().isEmpty()) {
                loadDataIntoTable();
            } else {
                setMessage("RED", "Please select a class to generate attendance report");
                // Optional: Clear the table when no class is selected
                data.clear();
                overallTable.setItems(data);
            }
        });
        studentIdColumn.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().classProperty());
        totalDaysColumn.setCellValueFactory(cellData -> cellData.getValue().totalDaysProperty().asObject());
        presentDaysColumn.setCellValueFactory(cellData -> cellData.getValue().daysPresentProperty().asObject());
        // For the percentage column
        attendancePercentColumn.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAttendancePercentage()).asObject());

        attendancePercentColumn.setCellFactory(column -> new TableCell<OverallReportTable, Double>() {
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
        data.clear();

        query = "SELECT a.user_id, s.full_name AS student_name, a.classes, " +
                "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) AS present_days, " +
                "COUNT(*) AS total_days FROM Attendance a JOIN Students s ON a.user_id = s" +
                ".user_id " +
                "WHERE a.classes = ? GROUP BY a.user_id, s.full_name, a.classes";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, classSelected);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String studentName = rs.getString("student_name");
                String classes = rs.getString("classes");
                int presentDays = rs.getInt("present_days");
                int totalDays = rs.getInt("total_days");
    
                // Calculate as double (without % formatting)
                double attendancePercentage = 0;
                if (totalDays > 0) {
                    attendancePercentage = ((double) presentDays / totalDays) * 100;
                }
    
                OverallReportTable report = new OverallReportTable(
                        userId, totalDays, presentDays, studentName, classes, attendancePercentage  // Passing double value
                );
                data.add(report);
            }
            setMessage("GREEN", "Calculated total % wise Attandance of " + classSelected);
            overallTable.setItems(data);

        } catch (SQLException e) {
            setMessage("RED", "Database error: " + e.getMessage());
        }
    }

    @FXML
    void handleExportToExcel(ActionEvent event) {
        File file = FileChooserFunction.chooseSaveLocation(
                (Stage) ((Node) event.getSource()).getScene().getWindow(),
                "Overall-Attendance-for-" + classComboBox.getValue() + ".xlsx"
        );

        if (file == null) {
            return; // User cancelled
        }

        String filePath = file.getAbsolutePath();

        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Attendance Data");

            // Assuming your TableView is named `attendanceTableView`
            // and it has some columns defined
            ObservableList<?> items = overallTable.getItems();
            ObservableList<TableColumn<OverallReportTable, ?>> columns = overallTable.getColumns();

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
                    // Use the cellData features to extract values
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
