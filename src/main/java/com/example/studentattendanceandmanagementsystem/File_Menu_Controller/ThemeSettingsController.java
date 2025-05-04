package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.Themes.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ThemeSettingsController {

    public AnchorPane rootPane;

    @FXML
    private MenuButton themeMenu, fontMenu, fontSizeMenu;
    @FXML private MenuItem darkTheme, lightTheme, aquaTheme, blueTheme, greenTheme, monokaiTheme, solarizedDarkTheme, solarizedLightTheme, draculaTheme, highContrastTheme;
    @FXML private MenuItem arialFont, calibriFont, poppinsFont, centuryFont, verdanaFont, consolasFont, segoeFont, timesNewRomanFont, lucidaConsoleFont, robotoFont, jetbrainsMonoFont;
    @FXML private MenuItem weight8, weight9, weight10, weight11, weight12, weight13, weight14, weight15, weight16, weight17, weight18, weight20, weight22, weight24, weight28, weight32;


    @FXML
    public void initialize() {
        // Set previous selections as current labels
        themeMenu.setText(ThemeManager.getTheme());
        fontMenu.setText(ThemeManager.getFont());
        fontSizeMenu.setText(String.valueOf(ThemeManager.getFontSize()));

        // Set handlers
        for (MenuItem item : fontMenu.getItems()) {
            final String text = item.getText();
            item.setOnAction(e -> {
                fontMenu.setText(text);
                System.out.println("Font set to: " + text);
            });
        }

        for (MenuItem item : themeMenu.getItems()) {
            final String text = item.getText();
            item.setOnAction(e -> {
                themeMenu.setText(text);
                System.out.println("Theme set to: " + text);
            });
        }

        for (MenuItem item : fontSizeMenu.getItems()) {
            final String text = item.getText();
            item.setOnAction(e -> {
                fontSizeMenu.setText(text);
                System.out.println("Size set to: " + text);
            });
        }
        // Set the default theme to match the current setting in ThemeManager
        String currentTheme = ThemeManager.getTheme();
        themeMenu.setText(currentTheme);

        // Set listeners for each MenuItem
        darkTheme.setOnAction(this::handleThemeSelection);
        lightTheme.setOnAction(this::handleThemeSelection);
        aquaTheme.setOnAction(this::handleThemeSelection);
        blueTheme.setOnAction(this::handleThemeSelection);
        greenTheme.setOnAction(this::handleThemeSelection);
        monokaiTheme.setOnAction(this::handleThemeSelection);
    }
    @FXML
    private void handleThemeSelection(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String selectedTheme = selectedItem.getText();

        // Update the theme in ThemeManager (this also saves the setting)
        ThemeManager.setTheme(selectedTheme);
    
        // Apply the theme to the current scene and all open stages
        Scene currentScene = themeMenu.getScene();
        ThemeManager.applyTheme(currentScene);
    
        // Apply the theme to all open stages as well
        ThemeManager.applyThemeToAllStages();
    
        // Update the menu button text to reflect the selected theme
        themeMenu.setText(selectedTheme);
        
        System.out.println("Theme changed and saved: " + selectedTheme);
    }
    @FXML
    public void applyChanges(ActionEvent e1) {
        String selectedTheme = themeMenu.getText();
        String selectedFont = fontMenu.getText();
        String selectedSize = fontSizeMenu.getText();

        try {
            // Set and save the theme settings
            ThemeManager.setTheme(selectedTheme);
            ThemeManager.setFont(selectedFont);
            ThemeManager.setFontSize(Integer.parseInt(selectedSize));
            
            // Apply the theme to current scene to see changes immediately
            Scene currentScene = themeMenu.getScene();
            ThemeManager.applyTheme(currentScene);
            
            // Apply to all open stages
            ThemeManager.applyThemeToAllStages();
            
            System.out.println("Theme settings applied and saved: " + selectedTheme + ", " + selectedFont + ", " + selectedSize);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid font size selected: " + selectedSize);
        }

        ((Stage) ((Node) e1.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void cancelChanges(ActionEvent e) {
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }
}
