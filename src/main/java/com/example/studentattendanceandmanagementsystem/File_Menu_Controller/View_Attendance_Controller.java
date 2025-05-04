package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.TableView.ViewAttandanceTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class View_Attendance_Controller {

    public ComboBox studentClassComboBox, studentIDComboBox;
    
    public Text errorText;
    public Label studentsNameText;

    @FXML
    private ComboBox<String> studentComboBox;

    @FXML
    private DatePicker startDateField, endDateField;

    @FXML
    private TableView<ViewAttandanceTable> attendanceTable;

    @FXML
    private TableColumn<ViewAttandanceTable, String> statusColumn, remarksColumn, dateColumn;

    private ObservableList<ViewAttandanceTable> attendanceList;
    ObservableList<ViewAttandanceTable> list = FXCollections.observableArrayList();
    boolean found = false;
 int count=0;

    @FXML
    public void initialize() {
        startDateField.setValue(LocalDate.now());
        endDateField.setValue(LocalDate.now());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().attendanceStatusProperty());
        remarksColumn.setCellValueFactory(cellData -> cellData.getValue().remarksProperty());
        studentClassComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            getStudentIDLoaded();
        });
        studentIDComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String value = (String) studentIDComboBox.getValue();
                getSelectedStudentsDetails(value);
                System.out.println("View_Attendance_Controller.initialize"+value);
            }
        });
        endDateField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && startDateField
                    .getValue() != null && endDateField.getValue() != null) {

                handleSearch(new ActionEvent());
            }
        });
    }

    public void handleSearch(ActionEvent actionEvent) {
         count = 0;
        attendanceTable.getItems().clear();
        String value = (String) studentIDComboBox.getValue();
        String StudentClass = (String) studentClassComboBox.getValue();
        String startDate = startDateField.getValue().toString();
        String endDate = endDateField.getValue().toString();
        String query = "SELECT * FROM Attendance WHERE user_id = ? AND AttandanceDate BETWEEN ? AND ?";

        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Integer.parseInt(value));
            pstmt.setString(2, startDate); // or pstmt.setDob(1, java.sql.Date.valueOf(startDate));
            pstmt.setString(3, endDate);   // or pstmt.setDob(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                count++;
                found = true;
                String date = rs.getString("AttandanceDate");
                String status = rs.getString("status");
                String remarks = rs.getString("remarks");
                ViewAttandanceTable record = new ViewAttandanceTable(date, status, remarks);
                list.add(record);


            }

            errorText.setText("");
            errorText.setText("Date Fetched for student CLASS= : " + StudentClass + " , Student ID = " + value + " .Total Row Count = " + count);
            errorText.setStyle("-fx-fill: GREEN; -fx-font-size: 14px");
            attendanceTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace(); // or showAlert("Error", e.getMessage());
        }
        if (!found) {
            errorText.setText("No data for the selected Date Range : " + startDate + " AND " + endDate);
            errorText.setStyle("-fx-fill: RED; -fx-font-size: 14px");
        }
    }

    private void getSelectedStudentsDetails(String value) {    count = 0;
        attendanceTable.getItems().clear();
        String StudentClass = (String) studentClassComboBox.getValue();
        attendanceTable.getItems().clear();
        String query = "select AttandanceDate,status,remarks from Attendance where user_id =?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);) {
            pstmt.setInt(1, Integer.parseInt(value));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                count++;
                String date = rs.getString("AttandanceDate");
                String status = rs.getString("status");
                String remarks = rs.getString("remarks");
                String full_name = getStudentName(String.valueOf(Integer.parseInt(value)));
                studentsNameText.setText("Students Name : "+full_name);
                ViewAttandanceTable record = new ViewAttandanceTable(date, status, remarks);
                list.add(record);
                errorText.setText("");
                errorText.setText("Date Fetched for student CLASS= : " + StudentClass + " , Student ID = " + value + " .Total Row count = " + count);
                errorText.setStyle("-fx-fill: GREEN; -fx-font-size: 14px");
            }

            attendanceTable.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getStudentIDLoaded() {
        attendanceTable.getItems().clear();
        String studentClass = (String) studentClassComboBox.getValue();

        if (studentClass != null && !studentClass.isEmpty()) {
            studentIDComboBox.getItems().clear();
            String query = "SELECT DISTINCT user_id FROM Attendance WHERE classes = ?";

            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, studentClass);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String id = rs.getString("user_id");
                    String full_name = getStudentName(id);

                    // Either add ID or name depending on your use case
                    studentIDComboBox.getItems().add(id);  // or add(full_name);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Class selection is null or empty");
        }
    }


    private String getStudentName(String id) {
        String query = "select first_name,last_name from Students where student_id = ?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);) {
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                return firstName + " " + lastName;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


}
