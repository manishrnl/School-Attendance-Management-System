package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class User_Guide_Controller {
    public TextArea guideContentArea;
    public ComboBox topicComboBox;

    public void handleTopicSelection(ActionEvent actionEvent) {
    }

    public void handleClose(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();  // Close login window
    }
}
