package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.Themes.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Add_Students_Controller {

    public Button submitButton, cancelButton;
    public GridPane userForm;
    public TextField showpswdField, showcnfpswdField;
    public Label classText;
    public ComboBox<String> classComboBox;
    public ComboBox<String> roleComboBox;
@FXML private TextField adminCodeField;
private boolean isAdminAuthorised=false;
    @FXML
    private Text errorText;
    @FXML
    private TextField firstNameField, lastNameField, fullNameField, userNameField, emailField,
            phoneNumberField, panNumberField, aadharNumberField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;

    private Stage primaryStage;
    private final List<Stage> openedStages = new ArrayList<>();
    private Optional<ButtonType> response;

    private boolean passwordVisible = false, confirmPasswordVisible = false;
    private boolean emailValid = false, phoneValid = false, panValid = false, aadharValid = false, passwordValid = false;

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{10,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@(gmail|yahoo|outlook|hotmail|edu|gov|company|university|business)\\.(com|org|net|edu|gov|mil|co|ac|in|uk|us|au|ca|de|fr|jp|cn|it|nl|ru)$";
    private static final String PAN_REGEX = "^[A-Za-z]{5}[0-9]{4}[A-Za-z]$";
    private static final String AADHAR_REGEX = "^[0-9]{12}$";

    @FXML
    public void initialize() {

        adminCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                searchAdminCodeFunction(newValue);
            }
        });
        firstNameField.textProperty().addListener((obs, oldVal, newVal) -> updateFullName());
        lastNameField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateFullName();
            generateUsername();
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateEmail());
        phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> validatePhone());
        panNumberField.textProperty().addListener((obs, oldVal, newVal) -> validatePan());
        aadharNumberField.textProperty().addListener((obs, oldVal, newVal) -> validateAadhar());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validatePasswords());
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validatePasswords());

        roleComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isStudent = "Student".equalsIgnoreCase(newVal);
            classComboBox.setVisible(isStudent);
            classText.setVisible(isStudent);
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

    private void showMessage(String color, String message) {
        errorText.setText(message);
        errorText.setStyle("-fx-fill:"+color+";");
    }

    private void updateFullName() {
        String fullName = firstNameField.getText().trim() + " " + lastNameField.getText().trim();
        fullNameField.setText(fullName);
    }

    private void generateUsername() {
        if (userNameField.getText().isEmpty()) {
            String uname = firstNameField.getText().toLowerCase() + LocalTime.now().withNano(0).toString().replace(":", "") + lastNameField.getText().toLowerCase();
            userNameField.setText(uname);
        }
        userNameField.setEditable(true);
    }

    private void validateEmail() {
        emailValid = emailField.getText().matches(EMAIL_REGEX);
        setMessage(emailValid, "Email is valid", "Invalid email. Must be like abc@gmail.com");
    }

    private void validatePhone() {
        phoneValid = phoneNumberField.getText().matches("\\d{10}");
        setMessage(phoneValid, "Phone number is valid", "Phone number must be 10 digits");
    }

    private void validatePan() {
        String pan = panNumberField.getText().toUpperCase();
        panNumberField.setText(pan);
        panValid = pan.matches(PAN_REGEX);
        setMessage(panValid, "Valid PAN number", "PAN must be 10 chars: 5 letters, 4 digits, 1 letter");
    }

    private void validateAadhar() {
        aadharValid = aadharNumberField.getText().matches(AADHAR_REGEX);
        setMessage(aadharValid, "Valid Aadhar number", "Aadhar must be 12 digits");
    }

    private void validatePasswords() {
        String pwd = passwordVisible ? showpswdField.getText() : passwordField.getText();
        String cpwd = confirmPasswordVisible ? showcnfpswdField.getText() : confirmPasswordField.getText();

        passwordValid = pwd.matches(PASSWORD_REGEX) && pwd.equals(cpwd);
        if (!pwd.matches(PASSWORD_REGEX)) {
            setMessage(false, "", "Password must have uppercase, lowercase, digit, special char and min 10 length");
        } else if (!pwd.equals(cpwd)) {
            setMessage(false, "", "Passwords do not match");
        } else {
            setMessage(true, "Password is valid and matches", "");
        }
    }

    private void setMessage(boolean condition, String successMsg, String errorMsg) {
        errorText.setText(condition ? successMsg : errorMsg);
        errorText.setStyle(condition ? "-fx-fill: GREEN; -fx-font-size: 14px" : "-fx-font-size: 14px; -fx-fill: RED");
    }

    public void checkpswd(ActionEvent e) {
        passwordVisible = !passwordVisible;
        togglePasswordField(passwordField, showpswdField, passwordVisible);
        validatePasswords();
    }

    public void checkcnfpswd(ActionEvent e) {
        confirmPasswordVisible = !confirmPasswordVisible;
        togglePasswordField(confirmPasswordField, showcnfpswdField, confirmPasswordVisible);
        validatePasswords();
    }

    private void togglePasswordField(PasswordField pf, TextField tf, boolean visible) {
        if (visible) {
            tf.setText(pf.getText());
            tf.setVisible(true);
            pf.setVisible(false);
        } else {
            pf.setText(tf.getText());
            pf.setVisible(true);
            tf.setVisible(false);
        }
    }

    @FXML
    private void handleSubmit() {
        String username = userNameField.getText().trim();
        String password = passwordVisible ? showpswdField.getText() : passwordField.getText();
        String confirmPassword = confirmPasswordVisible ? showcnfpswdField.getText() : confirmPasswordField.getText();
        String email = emailField.getText().trim();
        String phone = phoneNumberField.getText().trim();
        String pan = panNumberField.getText().trim();
        String aadhar = aadharNumberField.getText().trim();
        String role = roleComboBox.getValue();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || pan.isEmpty() || aadhar.isEmpty() || role == null) {
            showAlert(Alert.AlertType.ERROR, "Empty Fields", "All fields are required", "Please fill all fields.");
            return;
        }

        if ("Student".equalsIgnoreCase(role) && classComboBox.getValue() == null) {
            setMessage(false, "", "Please select class for Student role");
            return;
        }

        if (!emailValid || !phoneValid || !panValid || !aadharValid || !passwordValid) {
            showAlert(Alert.AlertType.ERROR, "Validation Failed", null, "Correct all highlighted fields first.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            setMessage(false, "", "Passwords do not match");
            return;
        }
        if(!isAdminAuthorised){
             setMessage(false, "",
            "Admin Code typed by you does not matched. Retry or get key from admin");
               return;}
        // Insert into database
        String query = "INSERT INTO Users (username,password,first_name,last_name,email,phone_number,pan_number,aadhar_number) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setString(7, pan);
            stmt.setString(8, aadhar);

            if (stmt.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Welcome " + firstName, "You have successfully registered.");
                errorText.setText("Thank you " + firstName + ", registration complete.");
                errorText.setStyle("-fx-fill: GREEN");
                Stage currentStage = (Stage) userForm.getScene().getWindow();
                currentStage.close();
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", null, ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String msg) {
        Alert alert = new Alert(type);
        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        response = alert.showAndWait();
    }

    @FXML
    private void handleCancel() throws IOException {
        clearForm();
        loadPage("Login.fxml", "Login Page");
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        fullNameField.clear();
        userNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        emailField.clear();
        phoneNumberField.clear();
        panNumberField.clear();
        aadharNumberField.clear();
        roleComboBox.setValue(null);
        classComboBox.setValue(null);
    }

    private void loadPage(String page, String title) throws IOException {
        Stage currentStage = (Stage) userForm.getScene().getWindow();
        currentStage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentattendanceandmanagementsystem/" + page));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        stage.setTitle(title);
        ThemeManager.applyTheme(scene);

        openedStages.add(stage);
        stage.show();
    }

    public void gotoLoginPage(ActionEvent event) throws IOException {
        loadPage("Login.fxml", "Login Page");
    }

}