package com.example.studentattendanceandmanagementsystem.StageManaging;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileChooserFunction {


    /**
     * Prompts user with a save dialog and returns the chosen file path.
     *
     * @param stage             The parent stage for the FileChooser dialog.
     * @param suggestedFileName The default file name to suggest.
     * @return The selected File, or null if the user cancels.
     */
    public static File chooseSaveLocation(Stage stage, String suggestedFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.setInitialFileName(suggestedFileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx"));
        return fileChooser.showSaveDialog(stage);
    }
}
