package com.example.studentattendanceandmanagementsystem;

import com.example.studentattendanceandmanagementsystem.File_Menu_Controller.Connect_With_Database;
import com.example.studentattendanceandmanagementsystem.SessionManager.SessionManager;
import com.example.studentattendanceandmanagementsystem.Themes.ThemeManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Label errorLabel;

    @FXML
    public ComboBox SignupAsCombo;
    public Hyperlink resetHyperlink;

    private Stage primaryStage;
    private Stage activeModalStage = null;

    Optional<ButtonType> response;
    private static final Map<String, Stage> openedStages = new HashMap<>();
    int userIDLoggedIN;

    public void handleLoginAction(ActionEvent actionEvent) throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        // Validate user inputs
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter a valid username/password");
            return;
        }
        String query = "SELECT user_id FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstm = conn.prepareStatement(query)) {
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                userIDLoggedIN = (rs.getInt("user_id"));
                SessionManager.getInstance().setUser(userIDLoggedIN, username);
                System.out.println("User Logged In: " + userIDLoggedIN);
                Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                currentStage.close();

                loadPage("Student-Attendance-And-Management-System.fxml", "Student Management System");
            } else {
                errorLabel.setText("Invalid username or password");
                errorLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (SQLException | IOException e) {
            errorLabel.setText("Database error occurred: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void gotoSignupPage(ActionEvent actionEvent) throws IOException {
        String pageAllocated = SignupAsCombo.getValue().toString();

        if (pageAllocated.equals("Students")) {
            closeCurrentStage(actionEvent);
            loadPage("File_Menu_FXML/AddStudents.fxml", "Signup");
        } else if (pageAllocated.equals("Teachers")) {
            closeCurrentStage(actionEvent);
            loadPage("File_Menu_FXML/addTeachers.fxml", "Signup");
        } else if (pageAllocated.equals("Staff")) {
            closeCurrentStage(actionEvent);
            loadPage("File_Menu_FXML/addStaff.fxml", "Signup");
        }
    }

    private void closeCurrentStage(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }

    private void loadPage(String page, String title) throws IOException {
        if (activeModalStage != null && activeModalStage.isShowing()) {
            // Frame already open — shake and beep
            activeModalStage.toFront();
            shakeStage(activeModalStage);
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentattendanceandmanagementsystem/" + page));
        Parent root = loader.load();

        Scene currentScene = (openedStages.isEmpty() ? new Scene(root) : openedStages.values().iterator().next().getScene());
        ThemeManager.applyTheme(currentScene);  // Apply theme to current scene

        Stage modalStage = new Stage();
        modalStage.setTitle(title);
        modalStage.setScene(currentScene);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        modalStage.setX(screenBounds.getMinX());
        modalStage.setY(screenBounds.getMinY());
        modalStage.setWidth(screenBounds.getWidth());
        modalStage.setHeight(screenBounds.getHeight());

        // Set as modal
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(primaryStage);

        activeModalStage = modalStage;

        // Clear on close
        modalStage.setOnHidden(e -> activeModalStage = null);

        modalStage.showAndWait();
    }

    private void shakeStage(Stage stage) {
        final int cycleCount = 6;
        final int distance = 10;

        TranslateTransition tt = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        tt.setFromX(0);
        tt.setByX(distance);
        tt.setAutoReverse(true);
        tt.setCycleCount(cycleCount);
        tt.play();
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        // Listen to minimize/restore events
        primaryStage.iconifiedProperty().addListener((obs, wasMinimized, isNowMinimized) -> {
            if (!isNowMinimized) {
                // When restored, bring all opened stages to the front
                for (Stage s : openedStages.values()) {
                    if (s.isShowing()) {
                        s.setIconified(false); // Restore if minimized
                        s.toFront();           // Bring to front
                    }
                }
            }
        });
    }

    @FXML
    public void gotoViewPasswordPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentattendanceandmanagementsystem/ViewPassword.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoResetPasswordPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/studentattendanceandmanagementsystem/resetPassword.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Reset Password");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to register a stage
    public static void registerStage(String stageName, Stage stage) {
        openedStages.put(stageName, stage);
    }

    // Method to unregister a stage
    public static void unregisterStage(String stageName) {
        openedStages.remove(stageName);
    }
}
