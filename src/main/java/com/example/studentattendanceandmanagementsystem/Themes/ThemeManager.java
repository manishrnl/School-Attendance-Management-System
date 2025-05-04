package com.example.studentattendanceandmanagementsystem.Themes;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ThemeManager {

    private static final String THEME_PROPERTIES_FILE = "src/main/resources/theme.properties";
    private static final Properties properties = new Properties();

    private static String theme = "Light"; // Default
    private static String font = "Poppins";  // Default
    private static int fontSize = 12;      // Default
    private static final Map<String, Stage> openedStages = new HashMap<>();
    private static final List<Scene> managedScenes = new ArrayList<>();

    // Map of theme names to their CSS resources
    private static final Map<String, String> themeStylesheets = new HashMap<>();
    
    static {
        try {
            // Initialize theme stylesheet map
            themeStylesheets.put("Dark", "/themes/dark-Theme.css");
            themeStylesheets.put("Light", "/themes/light-Theme.css");
            themeStylesheets.put("Aqua", "/themes/aqua.css");
            themeStylesheets.put("Blue", "/themes/blue.css");
            themeStylesheets.put("Green", "/themes/green.css");
            themeStylesheets.put("Monokai", "/themes/monokai.css");
            themeStylesheets.put("Dracula", "/themes/dracula.css");
            themeStylesheets.put("High Contrast", "/themes/high-Contrast.css");
            themeStylesheets.put("Solarized Dark", "/themes/solarized-dark.css");
            themeStylesheets.put("Solarized Light", "/themes/solarized-light.css");
            
            // Validate that at least one theme is available
            boolean themeFound = false;
            for (String themeName : themeStylesheets.keySet()) {
                String cssPath = themeStylesheets.get(themeName);
                java.net.URL cssResource = ThemeManager.class.getResource(cssPath);
                if (cssResource != null) {
                    themeFound = true;
                    System.out.println("Theme CSS found: " + themeName + " at " + cssResource);
                    break;
                }
            }
            
            if (!themeFound) {
                System.err.println("WARNING: No theme CSS files found! Theme functionality may not work.");
            }
            
            // Load saved settings on class initialization
            loadThemeSettings();
            
            // Log the loaded settings
            System.out.println("ThemeManager initialized with: " +
                              "Theme=" + theme + ", Font=" + font + ", Size=" + fontSize);
                              
        } catch (Exception e) {
            System.err.println("Error initializing ThemeManager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setTheme(String newTheme) {
        theme = newTheme;
        saveThemeSettings();
        applyThemeToAllScenes();
    }
    
    public static void setFont(String newFont) {
        font = newFont;
        saveThemeSettings();
        applyThemeToAllScenes();
    }
    
    public static void setFontSize(int newSize) {
        fontSize = newSize;
        saveThemeSettings();
        applyThemeToAllScenes();
    }

    public static String getTheme() {
        return theme;
    }

    public static String getFont() {
        return font;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static void applyTheme(Scene scene) {
        if (scene == null) {
            System.err.println("Cannot apply theme to null scene");
            return;
        }
        
        // Add to managed scenes list if not already there
        if (!managedScenes.contains(scene)) {
            managedScenes.add(scene);
            // Listen for scene removal
            scene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow == null) {
                    managedScenes.remove(scene);
                    System.out.println("Scene removed from managed scenes, total: " + managedScenes.size());
                }
            });
            System.out.println("Added scene to managed scenes, total: " + managedScenes.size());
        }
        
        // Apply theme in the JavaFX thread to be safe
        Platform.runLater(() -> {
            try {
                // Clear existing stylesheets
                scene.getStylesheets().clear();
                
                // Apply the theme stylesheet
                String cssPath = "/themes/dark-Theme.css"; // Default fallback
                
                switch (theme) {
                    case "Dark":
                        cssPath = "/themes/dark-Theme.css";
                        break;
                    case "Light":
                        cssPath = "/themes/light-Theme.css";
                        break;
                    case "Aqua":
                        cssPath = "/themes/aqua.css";
                        break;
                    case "Blue":
                        cssPath = "/themes/blue.css";
                        break;
                    case "Green":
                        cssPath = "/themes/green.css";
                        break;
                    case "Monokai":
                        cssPath = "/themes/monokai.css";
                        break;
                    case "Solarized Dark":
                        cssPath = "/themes/solarized-dark.css";
                        break;
                    case "Solarized Light":
                        cssPath = "/themes/solarized-light.css";
                        break;
                    case "High Contrast":
                        cssPath = "/themes/high-Contrast.css";
                        break;
                    case "Dracula":
                        cssPath = "/themes/dracula.css";
                        break;

                }
                
                // Load the theme CSS carefully
                try {
                    String themeStylesheetUrl = getThemeStylesheetUrl();
                    
                    if (themeStylesheetUrl != null) {
                        scene.getStylesheets().add(themeStylesheetUrl);
                        System.out.println("Applied theme CSS from: " + themeStylesheetUrl);
                    } else {
                        System.err.println("Could not find theme stylesheet. Using inline fallback styles.");
                        
                        // If CSS files can't be loaded, apply some basic theme styles directly
                        String baseStyle = "";
                        if ("Dark".equals(theme)) {
                            baseStyle = "-fx-base: #3c3f41; -fx-background: #2b2b2b; -fx-control-inner-background: #1e1e1e;";
                        } else {
                            baseStyle = "-fx-base: #ececec; -fx-background: #ffffff; -fx-control-inner-background: #f4f4f4;";
                        }
                        
                        scene.getRoot().setStyle(scene.getRoot().getStyle() + baseStyle);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading theme CSS file: " + e.getMessage());
                    e.printStackTrace();
                }
                
                // Add dynamic font CSS
                String fontCss = createFontStylesheet();
                scene.getStylesheets().add(fontCss);
                
                // Directly apply font to nodes recursively
                applyFontToAllNodes(scene.getRoot());
                
                System.out.println("Applied theme: " + theme + " with font: " + font + ", size: " + fontSize + " to scene");
            } catch (Exception e) {
                System.err.println("Error applying theme: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Recursively applies font settings to all nodes in a scene graph
     */
    private static void applyFontToAllNodes(Parent parent) {
        if (parent == null) return;
        
        // Apply to each child node
        for (Node node : parent.getChildrenUnmodifiable()) {
            // Apply font settings directly based on node type
            if (node instanceof Labeled) {
                Labeled labeled = (Labeled) node;
                labeled.setStyle(String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", font, fontSize));
            } else if (node instanceof TextInputControl) {
                TextInputControl textInput = (TextInputControl) node;
                textInput.setStyle(String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", font, fontSize));
            } else if (node instanceof Text) {
                Text text = (Text) node;
                text.setStyle(String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", font, fontSize));
                text.setFont(Font.font(font, fontSize));
            }
            
            // For menu buttons and other compound controls
            node.setStyle(String.format("-fx-font-family: '%s'; -fx-font-size: %dpx;", font, fontSize));
            
            // Continue recursively
            if (node instanceof Parent) {
                applyFontToAllNodes((Parent) node);
            }
        }
    }
    
    /**
     * Creates a dynamic stylesheet for font settings
     * @return URL to the in-memory stylesheet
     */
    private static String createFontStylesheet() {
        StringBuilder cssBuilder = new StringBuilder();
        
        // Set global font family and size for all elements with !important to override
        cssBuilder.append("* {\n")
                .append(String.format("  -fx-font-family: '%s' !important;\n", font))
                .append(String.format("  -fx-font-size: %dpx !important;\n", fontSize))
                .append("}\n\n");
        
        // Apply to specific control types to ensure consistent sizing
        String[] nodeTypes = {
            ".root", ".label", ".button", ".text-field", ".text-area", ".combo-box", 
            ".choice-box", ".menu-button", ".check-box", ".radio-button",
            ".hyperlink", ".text", ".general-text", ".general-label", ".tab",
            "Text", "Label", "Button", "TextField", "TextArea", "ComboBox",
            "ChoiceBox", "MenuButton", "CheckBox", "RadioButton", "Hyperlink",
            "TabPane", "Tab", ".menu-item", "MenuItem", ".context-menu",
            ".table-view", ".table-cell", ".list-view", ".list-cell",
            ".tree-view", ".tree-cell"
        };
        
        for (String nodeType : nodeTypes) {
            cssBuilder.append(nodeType).append(" {\n")
                    .append(String.format("  -fx-font-family: '%s' !important;\n", font))
                    .append(String.format("  -fx-font-size: %dpx !important;\n", fontSize))
                    .append("}\n");
        }
        
        // Special cases for popup menus, table headers, etc.
        cssBuilder.append(".menu-item > .label, .menu-item, .menu, .menu-bar, .menu-button {\n")
                .append(String.format("  -fx-font-family: '%s' !important;\n", font))
                .append(String.format("  -fx-font-size: %dpx !important;\n", fontSize))
                .append("}\n");
        
        // Create special sizes for headers if needed
        cssBuilder.append(".header, .title {\n")
                .append(String.format("  -fx-font-family: '%s' !important;\n", font))
                .append(String.format("  -fx-font-size: %dpx !important;\n", fontSize + 4))
                .append("}\n");
                
        // Convert to data URL format
        String cssContent = cssBuilder.toString();
        return "data:text/css;charset=utf-8," + java.net.URLEncoder.encode(cssContent, java.nio.charset.StandardCharsets.UTF_8);
    }
    
    /**
     * Debug method to list available resources
     */
    public static void listAvailableResources(String path) {
        try {
            System.out.println("Checking for resources at: " + path);
            java.net.URL resourceUrl = ThemeManager.class.getResource(path);
            
            if (resourceUrl != null) {
                System.out.println("Resource found: " + resourceUrl.toExternalForm());
                
                if (resourceUrl.getProtocol().equals("jar")) {
                    System.out.println("Resource is in a JAR file");
                } else if (resourceUrl.getProtocol().equals("file")) {
                    File resourceDir = new File(resourceUrl.toURI());
                    if (resourceDir.isDirectory()) {
                        System.out.println("Directory contents:");
                        File[] files = resourceDir.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                System.out.println(" - " + file.getName());
                            }
                        } else {
                            System.out.println("No files found or not a directory");
                        }
                    }
                }
            } else {
                System.out.println("Resource not found at path: " + path);
                
                // Try with class loader
                resourceUrl = ThemeManager.class.getClassLoader().getResource(path);
                if (resourceUrl != null) {
                    System.out.println("Resource found with class loader: " + resourceUrl);
                } else {
                    System.out.println("Resource not found with class loader either");
                }
            }
        } catch (Exception e) {
            System.out.println("Error checking resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Apply theme to all registered stages
     */
    public static void applyThemeToAllStages() {
        // Apply theme to all open stages, if any
        for (Stage stage : openedStages.values()) {
            if (stage != null && stage.getScene() != null) {
                Scene scene = stage.getScene();
                applyTheme(scene);
            }
        }
        System.out.println("Applied theme to " + openedStages.size() + " registered stages");
    }
    
    /**
     * Get the stylesheet URL for the current theme
     * @return URL as string for the theme CSS, or null if not found
     */
    public static String getThemeStylesheetUrl() {
        String cssPath = themeStylesheets.getOrDefault(theme, "/themes/light-Theme.css");
        try {
            // First try the standard resource loading
            java.net.URL cssResource = ThemeManager.class.getResource(cssPath);
            if (cssResource != null) {
                return cssResource.toExternalForm();
            }
            
            // If that fails, try class loader
            cssResource = ThemeManager.class.getClassLoader().getResource(cssPath.substring(1)); // Remove leading slash
            if (cssResource != null) {
                return cssResource.toExternalForm();
            }
            
            // If still not found, try a direct path for the case of file system resources
            File cssFile = new File(System.getProperty("user.dir") + "/src/main/resources" + cssPath);
            if (cssFile.exists()) {
                return cssFile.toURI().toURL().toExternalForm();
            }
            
            System.err.println("Failed to locate theme CSS: " + cssPath);
            return null;
        } catch (Exception e) {
            System.err.println("Error getting theme stylesheet: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Apply theme to all managed scenes (more comprehensive approach)
     */
    public static void applyThemeToAllScenes() {
        // First clean up any null scenes or scenes with null windows
        managedScenes.removeIf(scene -> scene == null || scene.getWindow() == null);
        
        // Apply theme to all tracked scenes
        for (Scene scene : new ArrayList<>(managedScenes)) {
            applyTheme(scene);
        }
        
        // Also check all JavaFX windows for any scenes we might have missed
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                if (stage.getScene() != null) {
                    Scene scene = stage.getScene();
                    if (!managedScenes.contains(scene)) {
                        applyTheme(scene);
                    }
                }
            }
        }
        
        System.out.println("Applied theme to all scenes and windows, managed scenes: " + managedScenes.size());
    }
    
    /**
     * Updates all scenes when font settings change
     */
    public static void updateFontSettings() {
        // No need to call setFontSize as it will be done by the caller
        applyThemeToAllScenes(); // Apply to all scenes, not just registered stages
        System.out.println("Updated font settings to size: " + fontSize);
    }
    
    /**
     * Saves the current theme settings to the properties file
     */
    private static void saveThemeSettings() {
        try {
            properties.setProperty("theme", theme);
            properties.setProperty("font", font);
            properties.setProperty("fontSize", String.valueOf(fontSize));
            
            File propertiesFile = new File(THEME_PROPERTIES_FILE);
            try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
                properties.store(out, "#Theme Settings\n" +
                        "#Default theme settings\n" +
                        "#This file is created to keep track of themes and font size and font name sa as to load it\n" +
                        "# while applications start.System will remember users theme .");
                System.out.println("Theme settings saved to: " + propertiesFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failed to save theme settings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads theme settings from the properties file
     */
    private static void loadThemeSettings() {
        File propertiesFile = new File(THEME_PROPERTIES_FILE);
        
        if (propertiesFile.exists()) {
            try (FileInputStream in = new FileInputStream(propertiesFile)) {
                properties.load(in);
                
                // Apply loaded settings
                theme = properties.getProperty("theme", "Light");
                font = properties.getProperty("font", "Arial");
                try {
                    fontSize = Integer.parseInt(properties.getProperty("fontSize", "13"));
                } catch (NumberFormatException e) {
                    fontSize = 13; // Default if parsing fails
                }
                
                System.out.println("Theme settings loaded: Theme=" + theme + ", Font=" + font + ", Size=" + fontSize);
            } catch (IOException e) {
                System.err.println("Failed to load theme settings: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Use defaults and save them
            System.out.println("Theme properties file not found at: " + propertiesFile.getAbsolutePath() + ", creating with defaults");
            saveThemeSettings();
        }
    }
    
    /**
     * Register a stage so it can have themes applied to it
     */
    public static void registerStage(String id, Stage stage) {
        openedStages.put(id, stage);
        
        // Also register its scene for theme updates
        if (stage.getScene() != null) {
            Scene scene = stage.getScene();
            if (!managedScenes.contains(scene)) {
                managedScenes.add(scene);
                System.out.println("Added scene from stage " + id + " to managed scenes");
            }
            
            // Listen for scene changes on this stage
            stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (oldScene != null && managedScenes.contains(oldScene)) {
                    managedScenes.remove(oldScene);
                }
                if (newScene != null && !managedScenes.contains(newScene)) {
                    managedScenes.add(newScene);
                    applyTheme(newScene);
                }
            });
        }
    }
    
    /**
     * Remove a stage from the registry
     */
    public static void unregisterStage(String id) {
        Stage stage = openedStages.remove(id);
        if (stage != null && stage.getScene() != null) {
            managedScenes.remove(stage.getScene());
        }
        System.out.println("Unregistered stage: " + id);
    }
    
    /**
     * Register a scene directly for theme management (for dialogs and other non-Stage windows)
     */
    public static void registerScene(Scene scene) {
        if (scene != null && !managedScenes.contains(scene)) {
            managedScenes.add(scene);
            applyTheme(scene);
            System.out.println("Registered additional scene for theme management");
        }
    }
}
