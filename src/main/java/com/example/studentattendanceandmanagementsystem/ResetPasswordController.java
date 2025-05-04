package com.example.studentattendanceandmanagementsystem;

import com.example.studentattendanceandmanagementsystem.File_Menu_Controller.Connect_With_Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResetPasswordController {

    @FXML
    private TextField aadharField;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField confirmPassworField;

    @FXML
    private Text errorText;

    @FXML
    private TextField mobileField;

    @FXML
    private TextField panField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;


    public void handleChangePassword(ActionEvent actionEvent) throws SQLException {
        String email = emailField.getText();
        String pan = panField.getText();
        String aadhar = aadharField.getText();
        String mobile = mobileField.getText();
        String password = passwordField.getText();
        String confirm = confirmPassworField.getText();
        boolean passwordUpdated = false;
        if (!password.equals(confirm)) {
            setMessage("RED", "Password and Confirm Password do not match.");
            return;
        }

        if (email.isEmpty() || pan.isEmpty() || aadhar.isEmpty() || mobile.isEmpty()) {
            setMessage("RED", "All fields must be filled.");
            return;
        }
        if (password.equals(confirm)) {
            String query = "UPDATE Users SET password=? WHERE email=? AND pan_number=? AND aadhar_number=? AND phone_number=?";
            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, password);  // new password
                pstmt.setString(2, email);
                pstmt.setString(3, pan);
                pstmt.setString(4, aadhar);
                pstmt.setString(5, mobile);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    passwordUpdated = true;
                    handleClearFields(actionEvent);
                }
            }
        }
        if (passwordUpdated)
            setMessage("GREEN", "Password changed successfully. New Password = " + password);
        else
            setMessage("RED", "No matching user found. Password not changed.");

    }

    private void setMessage(String color, String message) {
        errorText.setText(message);
        errorText.setStyle("-fx-fill:" + color + ";");
    }


    public void gotoLoginPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentattendanceandmanagementsystem/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Login Page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClearFields(ActionEvent actionEvent) {
        emailField.setText("");
        panField.setText("");
        aadharField.setText("");
        mobileField.setText("");
        passwordField.setText("");
        confirmPassworField.setText("");
    }
}
