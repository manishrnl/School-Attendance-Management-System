module com.example.studentattendanceandmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires java.desktop;

    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires org.apache.commons.compress;
    requires org.apache.xmlbeans;

    opens com.example.studentattendanceandmanagementsystem to javafx.fxml;
    opens com.example.studentattendanceandmanagementsystem.File_Menu_Controller to javafx.fxml;
    opens com.example.studentattendanceandmanagementsystem.Themes to javafx.fxml;

    exports com.example.studentattendanceandmanagementsystem;
    exports com.example.studentattendanceandmanagementsystem.Themes;
}
