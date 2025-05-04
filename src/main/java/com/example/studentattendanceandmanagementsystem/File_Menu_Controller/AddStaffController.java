package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.SessionManager.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStaffController {

    public ComboBox positionComboBox, departmentComboBox;
    public Label errorLabel;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker hireDatePicker;

    @FXML
    private Button saveButton;

    @FXML
    private Button clearButton;
    int userIDLoggedIn;

    public void handleSave(ActionEvent actionEvent) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String department = departmentComboBox.getValue().toString();
        String position = positionComboBox.getValue().toString();
        if (firstName.isEmpty() || lastName.isEmpty() || department.isEmpty() || hireDatePicker.getValue() == null) {
            errorLabel.setText("Please enter all the fields. All fields are required.");
        } else {
            String query = "INSERT INTO Staff (first_name, last_name, department, position, hire_date,user_id) VALUES (?,?,?,?,?,?)";
            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query);) {
                userIDLoggedIn = SessionManager.getInstance().getUserID();
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.setString(3, department);
                pstmt.setString(4, position);
                pstmt.setString(5, hireDatePicker.getValue().toString());
                pstmt.setInt(6, userIDLoggedIn);
             int response=  pstmt.executeUpdate();
             if (response >0) {
                 System.out.println("Data Inserted into Staffs");
                 errorLabel.setText("Data Inserted into Staffs");
                 errorLabel.setStyle("-fx-text-fill: GREEN;");
             }
            } catch (SQLException e) {
                System.out.println("Error in add staff" + e.getMessage());
            }
        }

    }

    public void handleClear(ActionEvent actionEvent) {
    }
}
