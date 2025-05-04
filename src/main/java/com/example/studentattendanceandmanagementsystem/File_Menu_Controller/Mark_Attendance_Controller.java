package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.TableView.MarkAttendanceTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Mark_Attendance_Controller {


    public DatePicker datePickerAttendance;
    public TextArea textAreaGeneralRemarks;
    public Button btnSubmitAttendance;
    public Button btnClearForm;
    public ComboBox attandanceStatusCombo;
    public TextField RemarksField;
    public TextField dateField;
    public TextField fullNameField;
    public TextField studIDField;
    public TextField classField;
    public Text errorText;
    @FXML
    private ComboBox<String> comboBoxClass;
    @FXML
    private TextArea remarksField;

    @FXML
    private TableView<MarkAttendanceTable> attendanceTableView;
    @FXML
    private TableColumn<MarkAttendanceTable, String> idColumn;
    @FXML
    private TableColumn<MarkAttendanceTable, String> nameColumn;
    @FXML
    private TableColumn<MarkAttendanceTable, String> classColumn;
    @FXML
    private TableColumn<MarkAttendanceTable, String> dateColumn;
    @FXML
    private TableColumn<MarkAttendanceTable, String> statusColumn;
    @FXML
    private TableColumn<MarkAttendanceTable, String> remarksColumn;
    ObservableList<MarkAttendanceTable> list = FXCollections.observableArrayList();
    private final Map<String, String> studentNameCache = new HashMap<>();

    @FXML
    void initialize() {
        datePickerAttendance.setValue(LocalDate.now());
        comboBoxClass.valueProperty().addListener((observable, oldValue, newValue) ->
                updateAttendanceForClass(newValue));
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().classNameProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().attendanceStatusProperty());
        remarksColumn.setCellValueFactory(cellData -> cellData.getValue().remarksProperty());
        attendanceTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                MarkAttendanceTable selectedRow = attendanceTableView.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    setMessage("", "");
                    studIDField.setText(selectedRow.getId());
                    classField.setText(selectedRow.getClassName());
                    dateField.setText(selectedRow.getDate());
                    RemarksField.setText(selectedRow.getRemarks());
                    fullNameField.setText(selectedRow.getName());
                }
            }
        });

        loadAttendanceData();
    }

    private void loadAttendanceData() {
        list.clear(); // clear previous data
        String query = "SELECT * FROM Attendance ORDER BY AttandanceDate DESC LIMIT 100";

        try (Connection conn = Connect_With_Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("user_id");
                String studentClass = rs.getString("classes");
                String date = rs.getString("AttandanceDate");
                String status = rs.getString("status");
                String remarks = rs.getString("remarks");

                String name = getStudentNameById(id);
                MarkAttendanceTable record = new MarkAttendanceTable(id, studentClass, date, status, remarks);
                record.setName(name);
                list.add(record);
            }
            attendanceTableView.setItems(list);
            setMessage("GREEN", "Loaded latest 100 attendance records.");

        } catch (SQLException e) {
            setMessage("RED", "Failed to load data: " + e.getMessage());
        }
    }

        private String getStudentNameById(String id) {
        if (studentNameCache.containsKey(id)) {
            return studentNameCache.get(id);
        }

        String query = "SELECT first_name, last_name FROM Students WHERE user_id = ?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                studentNameCache.put(id, fullName);
                return fullName;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void updateAttendanceForClass(String classValue) {
        int totalCount = 0;
        list.clear(); // Important: clear old data

        String query = """
        SELECT a.*
        FROM Attendance a
        JOIN (
            SELECT user_id, MAX(AttandanceDate) AS latest_date
            FROM Attendance
            WHERE classes = ?
            GROUP BY user_id
        ) b ON a.user_id = b.user_id AND a.AttandanceDate = b.latest_date
        WHERE a.classes = ?
        ORDER BY a.AttandanceDate DESC
    """;

        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, classValue); // for subquery
            pstmt.setString(2, classValue); // for main query

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                totalCount++;
                String studentId = rs.getString("user_id");
                String studentClass = rs.getString("classes");
                String date = rs.getString("AttandanceDate");
                String status = rs.getString("status");
                String remarks = rs.getString("remarks");

                String name = getStudentNameById(studentId);
                MarkAttendanceTable record = new MarkAttendanceTable(studentId, studentClass, date, status, remarks);
                record.setName(name);
                list.add(record);
            }

            attendanceTableView.setItems(list);
            setMessage("GREEN", "Class-wise attendance loaded. Total Row count: " + totalCount);

        } catch (SQLException e) {
            setMessage("RED", "Error loading class-wise attendance: " + e.getMessage());
        }
    }

    // Clear form fields
    @FXML
    public void handleClear() {
        classField.setText("");
        dateField.setText("");
        RemarksField.setText("");
        studIDField.setText("");
        fullNameField.setText("");
        attandanceStatusCombo.getSelectionModel().clearSelection();
    }

    public void updateAttandance(ActionEvent actionEvent) {
        String insertQuery = "INSERT INTO Attendance (user_id, classes, AttandanceDate, status, remarks) VALUES (?, ?, ?, ?, ?)";
        String updateQuery = "UPDATE Attendance SET classes = ?, status = ?, remarks = ? WHERE user_id = ? AND AttandanceDate = ?";
        if (attandanceStatusCombo.getValue() == null) {
            setMessage("RED", "Please choose status (Present, Absent ,Late) before Marking Attendance");

        } else {
            try (Connection conn = Connect_With_Database.getConnection()) {
                String studentId = studIDField.getText();
                String className = classField.getText();
                LocalDate today = LocalDate.now();
                String status = (String) attandanceStatusCombo.getValue();
                String remarks = RemarksField.getText();
                int rowsAffected = 0;

                if (!dateField.getText().isEmpty()) {
                    // Check if record exists for this student and today's date
                    String checkQuery = "SELECT COUNT(*) FROM Attendance WHERE user_id = ? AND AttandanceDate = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                        checkStmt.setString(1, studentId);
                        checkStmt.setDate(2, Date.valueOf(today));
                        ResultSet rs = checkStmt.executeQuery();

                        if (rs.next() && rs.getInt(1) > 0) {
                            // Record exists, update it
                            try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                                pstmt.setString(1, className);
                                pstmt.setString(2, status);
                                pstmt.setString(3, remarks);
                                pstmt.setString(4, studentId);
                                pstmt.setDate(5, Date.valueOf(today));

                                rowsAffected = pstmt.executeUpdate();
                                setMessage("GREEN", "Attendance updated successfully.");
                            }
                        } else {
                            // No record, insert new
                            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                                pstmt.setString(1, studentId);
                                pstmt.setString(2, className);
                                pstmt.setDate(3, Date.valueOf(today));
                                pstmt.setString(4, status);
                                pstmt.setString(5, remarks);

                                rowsAffected = pstmt.executeUpdate();
                                setMessage("GREEN", "Attendance inserted successfully.");
                            }
                        }
                    }

                    if (rowsAffected > 0) {
                        MarkAttendanceTable selected = attendanceTableView.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            selected.setName(fullNameField.getText());
                            selected.setClassName(className);
                            selected.setDate(today.toString());
                            selected.setAttendanceStatus(status);
                            selected.setRemarks(remarks);
                            attendanceTableView.refresh(); // Refresh to show changes
                        }
                    } else {
                        setMessage("RED", "No record inserted/updated for student ID: " + studentId);
                    }

                } else {
                    setMessage("RED", "Date field is empty. Cannot proceed.");
                }

            } catch (SQLException e) {
                setMessage("RED", "Database error occurred while updating attendance : " + e.getMessage());
            }
        }
    }

    void setMessage(String Color, String Message) {
        errorText.setText(Message);
        errorText.setStyle("-fx-fill:" + Color + ";");
    }
}
