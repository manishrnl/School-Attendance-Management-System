package com.example.studentattendanceandmanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AdminSignupController {
    @FXML
    private TextField firstNameField, lastNameField, fullNameField, userNameField, emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private TextField showpswdField, showcnfpswdField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button submitButton, cancelButton;
    @FXML
    private Text errorText;

}
