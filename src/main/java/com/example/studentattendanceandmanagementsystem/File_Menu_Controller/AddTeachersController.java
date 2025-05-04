package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.SessionManager.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddTeachersController {

    public ComboBox specialisationComboBox, departmentComboBox, subjectTeachingComboBox;
    public Label errorLabel;
    public TextField adminCodeField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;


    @FXML
    private DatePicker hireDatePicker;

    @FXML
    private TextField experienceField;

    @FXML
    private DatePicker lastTaughtPicker;
    String fName, lName, department, subjectTeaching, hireDate, experience, lastTaught, specialisation;
    String userIDLoggedIN;
boolean isAdminAuthorised=false;
    @FXML
    void initialize() {
        adminCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                searchAdminCodeFunction(newValue);
            }
        });
    }

    private void searchAdminCodeFunction(String newValue) {
        String query = "SELECT * FROM Admin WHERE Admin_Code = ?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newValue);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
               showMessage("GREEN","Admin Code matches perfectly with our Database" +
                       "Code");
                isAdminAuthorised = true;
            } else {
              showMessage("RED","Invalid Admin Code");
                isAdminAuthorised = false;
            }

        } catch (SQLException e) {
           showMessage("RED","Error in Database while Adding Teachers: " + e.getMessage());
        }
    }

    public void handleSave(ActionEvent actionEvent) throws SQLException {
        fName = firstNameField.getText();
        lName = lastNameField.getText();
        department = (String) departmentComboBox.getValue();
        subjectTeaching = (String) subjectTeachingComboBox.getValue();
        experience = experienceField.getText();
        specialisation = (String) specialisationComboBox.getValue();

        // Initialize these as null-safe
        LocalDate hireDateValue = hireDatePicker.getValue();
        LocalDate lastTaughtValue = lastTaughtPicker.getValue();
        hireDate = (hireDateValue != null) ? hireDateValue.toString() : "";
        lastTaught = (lastTaughtValue != null) ? lastTaughtValue.toString() : "";

        if (!fName.isEmpty() &&
                !lName.isEmpty() &&
                department != null && !department.isEmpty() &&
                subjectTeaching != null && !subjectTeaching.isEmpty() &&
                !hireDate.isEmpty() &&
                !experience.isEmpty() &&
                !lastTaught.isEmpty() &&
                specialisation != null && !specialisation.isEmpty() && isAdminAuthorised) {


            String query = "INSERT INTO Teachers(user_id, first_name, last_name, department, subject_specialization, hire_date, years_of_experience, last_teached_in_school, subject_teaching) " +
                    "VALUES " +
                    "(?,?,?,?,?,?,?,?,?)";
            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query);) {
                userIDLoggedIN = String.valueOf(SessionManager.getInstance().getUserID());
                pstmt.setString(1, userIDLoggedIN);
                pstmt.setString(2, fName);
                pstmt.setString(3, lName);
                pstmt.setString(4, department);
                pstmt.setString(5, specialisation);
                pstmt.setString(6, hireDate);
                pstmt.setString(7, experience);
                pstmt.setString(8, lastTaught);
                pstmt.setString(9, subjectTeaching);
                int response = pstmt.executeUpdate();
                if (response > 0) {
                     showMessage("GREEN","Data Inserted into our Database Successfully");

                }
            } catch (SQLException e) {
                showMessage("RED","Something went wrong while connecting to the database : " +
                        "Message : " + e.getMessage());

            }
        } else {
           showMessage("RED","Either you had not filled all details or you have not entered " +
                   "and valid authorised admin keyword to add Teacher into our database");

        }
    }
void showMessage(String color,String message){
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill:"+color+";");
}
    public void handleClear(ActionEvent actionEvent) {
        firstNameField.clear();
        lastNameField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        subjectTeachingComboBox.getSelectionModel().clearSelection();
        hireDatePicker.setValue(null);
        experienceField.clear();
        lastTaughtPicker.setValue(null);
        specialisationComboBox.getSelectionModel().clearSelection();
    }

    public void handleClose(ActionEvent actionEvent) {
        handleClear(actionEvent);
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
