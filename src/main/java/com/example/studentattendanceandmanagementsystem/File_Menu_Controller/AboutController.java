package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AboutController {
    @FXML
    private void handleGitHubLink() {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://github.com/manishrnl/tests"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleWebsiteLink(ActionEvent actionEvent) {
    }

    public void handleLicenseLink(ActionEvent actionEvent) {

    }
}
