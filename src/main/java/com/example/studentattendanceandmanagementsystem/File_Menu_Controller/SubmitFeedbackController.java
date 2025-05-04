package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SubmitFeedbackController {

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    public void handleSubmit() {
        String feedback = feedbackTextArea.getText();
        if (!feedback.isEmpty()) {
            // You can save this to a file or database instead
            System.out.println("Feedback submitted: " + feedback);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Feedback");
            alert.setHeaderText("Thank You!");
            alert.setContentText("Your feedback has been submitted.");
            alert.showAndWait();

            feedbackTextArea.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Empty Feedback");
            alert.setContentText("Please enter your feedback before submitting.");
            alert.showAndWait();
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
