package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpTopicsController {

    @FXML
    private TextArea gettingStartedText;
    @FXML
    private TextArea studentRecordsText;
    @FXML
    private TextArea attendanceText;
    @FXML
    private TextArea faqText;

    @FXML
    public void initialize() {
        String gettingStartedContent =
                "Welcome to the Student Attendance and Management System!" +
                        "This platform is designed to help you manage students, teachers, staff, and attendance records efficiently." +
                        "To get started, log in using your administrator credentials." +
                        "After logging in, you'll see the main dashboard where key statistics are displayed." +
                        "Use the sidebar navigation to access different modules like Students, Teachers, Staff, and Attendance." +
                        "Each module allows you to view, add, update, or delete records as per your permissions." +
                        "We recommend first setting up student classes and teacher departments." +
                        "Click on Students to begin adding student profiles including their names, classes, and guardians' information." +
                        "Similarly, set up teacher and staff profiles with their contact information and specializations." +
                        "You can also configure holiday calendars and academic sessions in the Settings module." +
                        "Remember to set up system backups under Settings for data safety." +
                        "If you face any issues, the Help and Support section is always available." +
                        "Your system administrator can customize the user roles and permissions." +
                        "Be sure to check updates regularly to benefit from new features." +
                        "Let’s begin managing your institution smartly and efficiently!";
        String studentRecordsContent =
                "Managing student records is one of the core features of the system." +
                        "Navigate to the Students tab from the dashboard sidebar." +
                        "Here you will find the full list of students currently enrolled." +
                        "Use the Add Student button to register a new student." +
                        "Fill in all required fields including full name, gender, class, contact information, and address." +
                        "You can also assign profile pictures to student records for easy identification." +
                        "Each student's record can be updated later by selecting their name and clicking Edit." +
                        "If a student leaves the institution, you can archive or delete their record safely." +
                        "Search functionality is available to quickly find a student by name, ID, or class." +
                        "Students can be filtered based on different classes or academic years." +
                        "Bulk upload functionality is available for importing large numbers of student records." +
                        "Attendance, grades, and disciplinary notes are linked automatically to student profiles." +
                        "It’s crucial to keep student records updated for smooth reporting and analysis." +
                        "Access control ensures only authorized users can modify student details." +
                        "Remember to regularly back up the student database to avoid any accidental data loss.";
        String attendanceTrackingContent =
                "Attendance tracking is made simple and efficient with this system." +
                        "Navigate to the Attendance section from the main dashboard." +
                        "Select the class and date for which you want to mark attendance." +
                        "Each student will be listed with a default Present status." +
                        "You can manually change status to Absent, Late, or Excused as needed." +
                        "Bulk marking is available if the entire class has a common attendance status." +
                        "Attendance records are automatically saved and timestamped." +
                        "You can view daily, weekly, or monthly attendance reports anytime." +
                        "Detailed filters allow reports based on class, student, or attendance status." +
                        "Attendance trends help identify students with frequent absences." +
                        "Notifications can be triggered to inform parents about absenteeism." +
                        "Historical attendance data is archived for each academic year." +
                        "Teachers and administrators have different permissions for attendance modules." +
                        "You can also export attendance records as PDF or Excel files for offline use." +
                        "Maintaining accurate attendance is critical for academic and legal reporting purposes.";
        String faqContent = "**Q: I forgot my password. What should I do?**\n" +
                "Click on Forgot Password at the login page to reset it via your registered email.\n\n" +
                "**Q: How can I add a new class?**\n" +
                "Go to the Settings module and select Manage Classes to add or edit class details.\n\n" +
                "**Q: The application is running slow. What can I check?**\n" +
                "Ensure your internet connection is stable and clear cache periodically.\n\n" +
                "**Q: How can I generate attendance reports?**\n" +
                "Navigate to the Attendance module and use the Reports tab to customize and export reports.\n\n" +
                "**Q: Can multiple users access the system simultaneously?**\n" +
                "Yes, the system supports multiple user roles with concurrent access.\n\n" +
                "**Q: What should I do if data is not saving?**\n" +
                "Check if all mandatory fields are filled and ensure you have saving permissions.\n\n" +
                "**Q: Is there a way to backup the data manually?**\n" +
                "Yes, you can create manual backups via the Settings > Backup option.\n\n" +
                "**Q: How often is the system updated?**\n" +
                "Major updates are rolled out quarterly, but critical patches may come sooner.\n\n" +
                "**Q: How can I contact support?**\n" +
                "Use the Help and Support section in the menu or email us at support@example.com.\n\n" +
                "**Q: Can students access their profiles?**\n" +
                "Students can view limited information if user permissions allow it.\n\n" +
                "**Q: How secure is my data?**\n" +
                "All records are encrypted and regular security audits are conducted to ensure safety.";
        attendanceText.setText(attendanceTrackingContent);
        faqText.setText(faqContent);
        studentRecordsText.setText(studentRecordsContent);
        gettingStartedText.setText(gettingStartedContent);
    }

    public void handleBack(ActionEvent actionEvent) {
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
