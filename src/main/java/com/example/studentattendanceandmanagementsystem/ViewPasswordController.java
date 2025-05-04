package com.example.studentattendanceandmanagementsystem;

import com.example.studentattendanceandmanagementsystem.File_Menu_Controller.Connect_With_Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ViewPasswordController {

    public Button resetButton;
    public Text errorText;
    public AnchorPane anchorPane;
    @FXML
    private TextField emailField;
    @FXML
    private TextField panField;
    @FXML
    private TextField aadharField;
    @FXML
    private TextField mobileField;
    public Hyperlink resetHyperlink;

    private void loadPage(String page, String title) {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/studentattendanceandmanagementsystem/" + page);
            if (fxmlUrl == null) {
                System.err.println("FXML not found: " + page);
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.setScene(new Scene(root));
            modalStage.initModality(Modality.APPLICATION_MODAL); // safer and ensures full modal behavior
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load page: " + page);
        }
    }

    @FXML
    private void handleResetPassword() throws SQLException {
        String email = emailField.getText();
        String pan = panField.getText();
        String aadhar = aadharField.getText();
        String mobile = mobileField.getText();
        String userName = "", Password = "", fullName = "";
        boolean dataFound = false;
        if (!email.isEmpty() && !pan.isEmpty() && !aadhar.isEmpty() && !mobile.isEmpty()) {
            setMessage("Green", "Checking details you entered from our database");
            String query = "SELECT username, password ,full_name FROM Users WHERE email=? " +
                    "AND pan_number=? AND aadhar_number=? AND phone_number=?";

            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, email);
                pstmt.setString(2, pan);
                pstmt.setString(3, aadhar);
                pstmt.setString(4, mobile);
                ResultSet affectedRow = pstmt.executeQuery();
                while (affectedRow.next()) {
                    dataFound = true;
                    userName = affectedRow.getString("username");
                    Password = affectedRow.getString("password");
                    fullName = affectedRow.getString("full_name");
                }
            }
        } else
            setMessage("RED", "Fill All fields first to reset your password.All fields are " +
                    "compulsory");
        if (dataFound) {
            errorText.setText(" User Exist , \nuser Name=" + userName + "\nPassword= " + Password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Users Found");
            alert.setHeaderText("Congratulations " + fullName + ", your validation is successful.");
            alert.setContentText("Hi " + fullName + " ! You are a valid user.\nUsername: " + userName + "\n"
                    + "Password: " + Password);

            Optional<ButtonType> response = alert.showAndWait();
            if (response.isPresent() && response.get() == ButtonType.OK) {
                emailField.setText("");
                panField.setText("");
                aadharField.setText("");
                panField.setText("");
                Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                currentStage.close();
                loadPage("Login.fxml", "Login Page");
            }

        } else
            setMessage("RED", "Data not matched with our database.Kindly " +
                    "Cross check all Fields and try again !!");
    }

    public void goBacktoLoginPage(ActionEvent actionEvent) {
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

    private void setMessage(String color, String Message) {
        errorText.setText(Message);
        errorText.setStyle("-fx-fill:" + color + ";");

    }
}
