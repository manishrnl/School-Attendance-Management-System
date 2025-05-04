
package com.example.studentattendanceandmanagementsystem;

import com.example.studentattendanceandmanagementsystem.Themes.ThemeManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;

public class Main_Application extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Application starting with theme: " + ThemeManager.getTheme() +
                    ", font: " + ThemeManager.getFont() +
                    ", size: " + ThemeManager.getFontSize());

            // Check if theme resources are available
            System.out.println("Checking for theme resources...");
            URL darkThemeUrl = getClass().getResource("/themes/dark-Theme.css");
            URL lightThemeUrl = getClass().getResource("/themes/light-Theme.css");
            
            if (darkThemeUrl != null) {
                System.out.println("Found dark theme at: " + darkThemeUrl);
            } else {
                System.err.println("Dark theme CSS not found! Checking alternate paths...");
                // Try alternate paths
                darkThemeUrl = getClass().getClassLoader().getResource("themes/dark-Theme.css");
                if (darkThemeUrl != null) {
                    System.out.println("Found dark theme with class loader at: " + darkThemeUrl);
                } else {
                    System.err.println("Critical: Dark theme CSS not found with class loader either!");
                }
            }
            
            if (lightThemeUrl != null) {
                System.out.println("Found light theme at: " + lightThemeUrl);
            } else {
                System.err.println("Light theme CSS not found!");
            }
    
            // First register the primary stage with the ThemeManager
            ThemeManager.registerStage("primaryStage", primaryStage);

            // Get the correct file path for login
            String loginFxml = "/com/example/studentattendanceandmanagementsystem/Login.fxml";
            URL fxmlLocation = getClass().getResource(loginFxml);

            if (fxmlLocation == null) {
                throw new IllegalStateException("Cannot find FXML file: " + loginFxml);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Create scene with the loaded FXML
            Scene scene = new Scene(root);

            // Apply the theme to the scene
            ThemeManager.applyTheme(scene);
            
            // Set up a listener to monitor for any new dialogs or windows
            Window.getWindows().addListener((ListChangeListener<Window>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (Window window : change.getAddedSubList()) {
                            if (window instanceof Stage) {
                                Stage newStage = (Stage) window;
                                if (newStage.getScene() != null) {
                                    ThemeManager.applyTheme(newStage.getScene());
                                }
                                
                                // Listen for scene changes
                                newStage.sceneProperty().addListener((obs, oldScene, newScene) -> {
                                    if (newScene != null) {
                                        ThemeManager.applyTheme(newScene);
                                    }
                                });
                            }
                        }
                    }
                }
            });

            // Set up the primary stage
            primaryStage.setTitle("Student Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            // Make sure to unregister the stage when closed
            primaryStage.setOnCloseRequest(event -> {
                ThemeManager.unregisterStage("primaryStage");
                Platform.exit();
            });

            // Show the primary stage
            primaryStage.show();
            System.out.println("Login screen shown with theme applied");

        } catch (Exception e) {
            System.err.println("Error loading login or applying theme: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        // Before launching, ensure we have the app directory
        File appDir = new File(System.getProperty("user.home") + File.separator + "StudentAttendanceSystem");
        if (!appDir.exists()) {
            if (appDir.mkdirs()) {
                System.out.println("Created application data directory: " + appDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create application data directory");
            }
        }

        launch(args);
    }
}