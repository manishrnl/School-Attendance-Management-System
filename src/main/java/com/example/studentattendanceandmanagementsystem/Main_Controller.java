package com.example.studentattendanceandmanagementsystem;

import com.example.studentattendanceandmanagementsystem.File_Menu_Controller.Connect_With_Database;
import com.example.studentattendanceandmanagementsystem.SessionManager.SessionManager;
import com.example.studentattendanceandmanagementsystem.Themes.ThemeManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class Main_Controller implements Initializable {
    public Text errorText;
    public Text welcomeText;
    @FXML
    BorderPane rootPane = null;
    public Text totalPresent, totalStudents;
    public Text totalAbsent;
    public Text lateCame;
    public MenuBar menuBar;
    private Stage aboutStage, settingsStage,
            appearanceStage, loginStage, markAttandanceStage, viewAttandanceStage, themeSettingsStage, helpTopicsStage, submitFeedbackStage, manageStudentStage, userPreferanceStage, monthlyReportStage,
            overallReportStage, userGuideStage, AddStudentStage, primaryStage;
    Optional<ButtonType> response;
    private final Map<String, Stage> openedStages = new HashMap<>();
    int totalCount = 0, present = 0, absent = 0, late = 0;
    Stage currentStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Apply theme to the rootPane's scene (when it becomes available)
        if (rootPane != null && rootPane.getScene() != null) {
            ThemeManager.applyTheme(rootPane.getScene());
        }

        String UserLoggedIn = SessionManager.getInstance().getUserName();
        getTotalPresentAbsentLateCame();
        totalStudents.setText(String.valueOf(totalCount));
        totalPresent.setText(String.valueOf(present));
        totalAbsent.setText(String.valueOf(absent));
        lateCame.setText(String.valueOf(late));
        welcomeText.setText("Welcome " + UserLoggedIn);
        welcomeText.setStyle("-fx-fill: #33add7;");
    }

    private void getTotalPresentAbsentLateCame() {
        String currentDate = LocalDate.now().toString();
        String query = "SELECT COUNT(*) FROM Students";
        String lateQuery = "SELECT COUNT(*) FROM Attendance WHERE status = 'Late' AND AttandanceDate = '" + currentDate + "'";
        String absentQuery = "SELECT COUNT(*) FROM Attendance WHERE status = 'Absent' AND AttandanceDate = '" + currentDate + "'";
        String presentQuery = "SELECT COUNT(*) FROM Attendance WHERE status = 'Present' AND AttandanceDate = '" + currentDate + "'";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmtTotal = conn.prepareStatement(query);
             PreparedStatement pstmLate = conn.prepareStatement(lateQuery);
             PreparedStatement pstmAbsent = conn.prepareStatement(absentQuery);
             PreparedStatement pstmPresent = conn.prepareStatement(presentQuery);
             ResultSet rsTotal = pstmtTotal.executeQuery();
             ResultSet rsLate = pstmLate.executeQuery();
             ResultSet rsAbsent = pstmAbsent.executeQuery();
             ResultSet rsPresent = pstmPresent.executeQuery()) {
            if (rsTotal.next()) {
                totalCount = rsTotal.getInt(1);
            }
            if (rsLate.next()) {
                late = rsLate.getInt(1);
            }
            if (rsAbsent.next()) {
                absent = rsAbsent.getInt(1);
            }
            if (rsPresent.next()) {
                present = rsPresent.getInt(1);
            }
        } catch (SQLException e) {
            errorText.setText("Error in database : " + e.getMessage());
            errorText.setStyle("-fx-fill: red");
        }
    }

    public void refreshData(ActionEvent actionEvent) {
        getTotalPresentAbsentLateCame();
        totalStudents.setText(String.valueOf(totalCount));
        totalPresent.setText(String.valueOf(present));
        totalAbsent.setText(String.valueOf(absent));
        lateCame.setText(String.valueOf(late));
    }

    public void handleMenu(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        String itemText = item.getText();
        switch (itemText) {
            case "Login":
                currentStage = (Stage) menuBar.getScene().getWindow();
                currentStage.close();
                // loadIntoCenterPane("Login.fxml", "Login to our Dashboard"); // same window
                showOrLoadStage("Login.fxml", "Login Page", "loginStage"); // new window
                break;
            case "About":
                showOrLoadStage("File_Menu_FXML/About.fxml", "About Page", "aboutStage");
                break;
            case "Mark Attendance":
                showOrLoadStage("File_Menu_FXML/mark_attandance.fxml", "Mark Attandance Page", "markAttandanceStage");
                break;

            case "View Attendance":
                showOrLoadStage("File_Menu_FXML/view_attendance.fxml", "View Attandance Page", "viewAttandanceStage");
                break;
            case "Theme Settings":
                showOrLoadStage("File_Menu_FXML/ThemeSettings.fxml", "Setting Theme  Page",
                        "themeSettingsStage");
                break;
            case "Help Topics":
                showOrLoadStage("File_Menu_FXML/helpTopics.fxml", "Help Page", "helpTopicsStage");
                break;
            case "Submit Feedback":
                showOrLoadStage("File_Menu_FXML/submitFeedback.fxml", "Feedback Page", "submitFeedbackStage");
                break;
            case "Manage Students":
                showOrLoadStage("File_Menu_FXML/manage_Students.fxml", "Manage Students Page", "manageStudentStage");
                break;
            case "Manage Teachers":
                showOrLoadStage("File_Menu_FXML/manageTeachers.fxml", "Manage Teachers Page", "manageStudentStage");
                break;
            case "Add Teachers":
                showOrLoadStage("File_Menu_FXML/addTeachers.fxml", "Add Teachers Page", "manageStudentStage");
                break;

            case "Manage Staffs":
                showOrLoadStage("File_Menu_FXML/manageStaffs.fxml", "Manage Staffs Page", "manageStudentStage");
                break;
            case "Add Staffs":
                showOrLoadStage("File_Menu_FXML/addStaff.fxml", "Add Staffs Page", "manageStudentStage");
                break;
            case "Monthly Report":
                showOrLoadStage("File_Menu_FXML/monthly_Report.fxml", "Monthly Attendance Report of a Student ", "monthlyReportStage");
                break;
            case "Overall Report":
                showOrLoadStage("File_Menu_FXML/overall_Report.fxml", "All Time Attendance Report of a Student ", "overallReportStage");
                break;

            case "User Guide":
                showOrLoadStage("File_Menu_FXML/user_Guide.fxml", "User Guide Page", "userGuideStage");
                break;
            case "Logout":
                showAlert(Alert.AlertType.CONFIRMATION, "Quit", "Quitting the Application",
                        "Are you sure you want to quit?");
                if (response.isPresent() && response.get() == ButtonType.OK) {
                    currentStage = (Stage) menuBar.getScene().getWindow();
                    currentStage.close();
                    showOrLoadStage("Login.fxml", "Login Page", "loginStage");
                }
                break;
            case "Add Student":
                showOrLoadStage("File_Menu_FXML/AddStudents.fxml", "Student Signup Page", "AddStudentStage");
                break;

        }
    }

    private void showOrLoadStage(String fxmlName, String title, String stageFieldName) {
        try {
            Stage existingStage = (Stage) this.getClass().getDeclaredField(stageFieldName).get(this);

            if (existingStage != null && existingStage.isShowing()) {
                existingStage.setIconified(false);
                existingStage.toFront();
            } else {
                Stage newStage = loadPage(fxmlName, title, stageFieldName);
                this.getClass().getDeclaredField(stageFieldName).set(this, newStage);

                newStage.setOnCloseRequest(event -> {
                    openedStages.remove(newStage);
                    try {
                        this.getClass().getDeclaredField(stageFieldName).set(this, null);
                    } catch (Exception e) {
                        errorText.setText("Error in closing stage: " + e.getMessage());
                        errorText.setStyle("-fx-fill: red");
                    }
                });
            }
        } catch (Exception e) {
            errorText.setText("Error in database: " + e.getMessage());
            errorText.setStyle("-fx-fill: red");
        }
    }

    private Stage activeModalStage = null;


    // Call this once from Main to inject the main window
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        
        // Register this stage with ThemeManager
        ThemeManager.registerStage("mainDashboard", stage);
        
        // Apply theme to the stage
        if (stage.getScene() != null) {
            ThemeManager.applyTheme(stage.getScene());
        }
        
        // Make sure to unregister when the stage closes
        stage.setOnCloseRequest(event -> {
            ThemeManager.unregisterStage("mainDashboard");
        });
    }

    // Shake effect if already open
    private void shakeStage(Stage stage) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        tt.setFromX(0);
        tt.setByX(10);
        tt.setAutoReverse(true);
        tt.setCycleCount(6);
        tt.play();
    }

    private Stage loadPage(String page, String title, String stageFieldName) throws IOException {
        if (activeModalStage != null && activeModalStage.isShowing()) {
            activeModalStage.toFront();
            shakeStage(activeModalStage);
            java.awt.Toolkit.getDefaultToolkit().beep();
            return null;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/example/studentattendanceandmanagementsystem/" + page));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        ThemeManager.applyTheme(scene); // ✅ Apply theme to scene

        Stage modalStage = new Stage();

        // Enable full screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        modalStage.setX(screenBounds.getMinX());
        modalStage.setY(screenBounds.getMinY());
        modalStage.setWidth(screenBounds.getWidth());
        modalStage.setHeight(screenBounds.getHeight());

        modalStage.setTitle(title);
        modalStage.setScene(scene); // ✅ Use the themed scene

        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(primaryStage);

        activeModalStage = modalStage;
        openedStages.put(stageFieldName, modalStage);

        // Register this new stage with the ThemeManager
        ThemeManager.registerStage(stageFieldName, modalStage);
    
        modalStage.setOnCloseRequest(event -> {
            openedStages.remove(stageFieldName);
            // Unregister the stage from ThemeManager when closed
            ThemeManager.unregisterStage(stageFieldName);
            activeModalStage = null;
            try {
                this.getClass().getDeclaredField(stageFieldName).set(this, null);
            } catch (Exception e) {
                errorText.setText("Error in closing stage: " + e.getMessage());
                errorText.setStyle("-fx-fill: red");
            }
        });

        modalStage.show();
        return modalStage;
    }


    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.initOwner(primaryStage); // Set to primary stage
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        response = alert.showAndWait();
    }
}