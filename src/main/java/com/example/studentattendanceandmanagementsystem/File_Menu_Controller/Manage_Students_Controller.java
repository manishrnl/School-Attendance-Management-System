package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.TableView.ManageStudentsTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class Manage_Students_Controller {
    public Button addButton;
    public Button updateButton;
    public Button clearButton;
    public Button deleteButton;
    public TextField searchByUserID;
    public TextField searchByStudentName;
    public ComboBox searchByClassCombo;
    public TextField searchByEnrolledYear;
    public TextField searchByFatherMobile;
    public TextField searchByMotherMobile;

    public Text errorText;

    // public DatePicker dobPicker;
    int totalCount;

    @FXML
    private TableView<ManageStudentsTable> studentsTable;
    @FXML
    private TableColumn<ManageStudentsTable, String> classColumn, firstNameColumn, lastNameColumn, genderColumn, courseColumn, addressColumn, parentAddressColumn, fatherMobileColumn, motherMobileColumn;

    @FXML
    private TableColumn<ManageStudentsTable, Integer> studentIDColumn, userIdColumn, enrolledYearColumn;

    @FXML
    private TableColumn<ManageStudentsTable, LocalDate> AttandanceDateColumn;
    @FXML
    private TextField studentIDField, userIDField, firstNameField, lastNameField, fatherMobileField, motherMobileField, enrolledYearField, courseField, yearField;
    @FXML
    private TextArea addressArea, parentAddressArea;
    @FXML
    private DatePicker AttandanceDatePicker;
    @FXML
    private ComboBox<String> genderComboBox, classComboBox, courseComboBox;
    ObservableList<ManageStudentsTable> data = FXCollections.observableArrayList();
    public String classes = "classes", user_id = "user_id", father_mobile = "father_mobile",
            mother_mobile = "mother_mobile", year_of_enrollment = "year_of_enrollment";
    int countTotal = 0;
    Optional<ButtonType> response;

    @FXML
    void initialize() {
        setMessage("", "");

        loadDataIntoTable();
        searchByUserID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(false, true, true, true, true, true);
                searchFunction(user_id, newValue);

            }
        });
        searchByStudentName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, false, true, true, true, true);
                searchByNameFunction(newValue);

            }
        });
        searchByEnrolledYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, false, true, true, true);
                searchFunction(year_of_enrollment, newValue);
            }
        });
        searchByFatherMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, true, false, true, true);
                searchFunction(father_mobile, newValue);
            }
        });
        searchByMotherMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, true, true, false, true);
                searchFunction(mother_mobile, newValue);
            }
        });
        searchByClassCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearFields(true, true, true, true, true, false);
                searchFunction(classes, (String) newValue);
            }
        });


        studentIDColumn.setCellValueFactory(cellData -> cellData.getValue().studentIDProperty().asObject());
        userIdColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());

        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        AttandanceDateColumn.setCellValueFactory(cellData -> cellData.getValue().dobProperty());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().classLevelProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        enrolledYearColumn.setCellValueFactory(cellData -> cellData.getValue().enrolledYearProperty().asObject());
        fatherMobileColumn.setCellValueFactory(cellData -> cellData.getValue().fatherMobileProperty());
        motherMobileColumn.setCellValueFactory(cellData -> cellData.getValue().motherMobileProperty());
        parentAddressColumn.setCellValueFactory(cellData -> cellData.getValue().parentAddressProperty());

// Ensure sorting for numeric columns (like student ID or enrolled year)
        studentIDColumn.setSortType(TableColumn.SortType.ASCENDING); // For default sort order
        userIdColumn.setSortType(TableColumn.SortType.ASCENDING); // Similarly for other numeric columns


        studentsTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                ManageStudentsTable selectedRow = studentsTable.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    studentIDField.setText(String.valueOf(selectedRow.getStudentID()));
                    userIDField.setText(String.valueOf((Integer) selectedRow.getUserId()));
                    classComboBox.setValue(selectedRow.getClassLevel());
                    firstNameField.setText(selectedRow.getFirstName());
                    lastNameField.setText(selectedRow.getLastName());
                    genderComboBox.setValue(selectedRow.getGender());
                    AttandanceDatePicker.setValue(selectedRow.getDob());
                    courseComboBox.setValue(selectedRow.getCourse());
                    addressArea.setText(selectedRow.getAddress());
                    enrolledYearField.setText(String.valueOf(selectedRow.getEnrolledYear()));
                    fatherMobileField.setText(String.valueOf(selectedRow.getFatherMobile()));
                    motherMobileField.setText(String.valueOf(selectedRow.getMotherMobile()));
                    parentAddressArea.setText(selectedRow.getParentAddress());
                }
            }
        });
        studentIDColumn.setSortable(true);
        userIdColumn.setSortable(true);
        firstNameColumn.setSortable(true);
        lastNameColumn.setSortable(true);
        genderColumn.setSortable(true);
        AttandanceDateColumn.setSortable(true);
        classColumn.setSortable(true);
        courseColumn.setSortable(true);
        addressColumn.setSortable(true);
        enrolledYearColumn.setSortable(true);
        fatherMobileColumn.setSortable(true);
        motherMobileColumn.setSortable(true);
        parentAddressColumn.setSortable(true);

    }

    private void clearFields(boolean clearStudentID, boolean studentName, boolean enrollYear, boolean fatherMobile, boolean motherMobile, boolean clearClass) {
        setMessage("", "");
        if (clearStudentID) searchByUserID.clear();
        if (studentName) searchByStudentName.clear();
        if (enrollYear) searchByEnrolledYear.clear();
        if (fatherMobile) searchByFatherMobile.clear();
        if (motherMobile) searchByMotherMobile.clear(); // Clear DatePicker
        if (clearClass) searchByClassCombo.getSelectionModel().clearSelection();
    }


    private void searchByNameFunction(String name) {
        setMessage("", "");
        countTotal = 0;
        String query = "SELECT * FROM Students WHERE LOWER(full_name) LIKE LOWER(?) ";

        studentsTable.getItems().clear();
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            boolean dataFound = false;

            while (rs.next()) {
                countTotal++;
                dataFound = true;
                errorText.setText("");
                int studentID = rs.getInt("student_id");
                int userID = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String classLevel = rs.getString("classes");
                LocalDate AttandanceDate = LocalDate.parse(rs.getString("date_of_birth"));
                String gender = rs.getString("gender");
                String address = rs.getString("address");
                int enrolledYear = rs.getInt("year_of_enrollment");
                String fatherMobile = rs.getString("father_mobile");
                String motherMobile = rs.getString("mother_mobile");
                String parentAddress = rs.getString("parent_address");
                String course = rs.getString("course");

                ManageStudentsTable manageStudentsTable = new ManageStudentsTable(
                        studentID, userID, classLevel, firstName, lastName, AttandanceDate, gender, course, enrolledYear, fatherMobile, motherMobile, parentAddress, address);
                data.add(manageStudentsTable);
                setMessage("GREEN", "Found total of " + countTotal + " Data for Typed Name :" + name);

            }
            studentsTable.setItems(data);
            if (!dataFound) {
                setMessage("RED", "No students found for name : " + name);

            }
        } catch (SQLException e) {
            setMessage("RED", "Database error : " + e.getMessage());

        }
    }

    void setMessage(String Color, String Message) {
        errorText.setText(Message);
        errorText.setStyle("-fx-fill:" + Color + ";");
    }

    private void searchFunction(String getFields, String tableValue) {
        String query;
        totalCount = 0;
        if (getFields.equals("classes")) {
            query = "SELECT * FROM Students WHERE " + getFields + " = ?";
        } else {
            query = "SELECT * FROM Students WHERE " + getFields + " LIKE ?";
        }
        studentsTable.getItems().clear();
        try (
                Connection conn = Connect_With_Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (getFields.equals("classes")) {
                pstmt.setString(1, tableValue);

            } else {
                pstmt.setString(1, "%" + tableValue + "%");  // 🔥 enables partial matching!

            }
            ResultSet rs = pstmt.executeQuery();

            boolean dataFound = false;

            while (rs.next()) {
                dataFound = true;
                totalCount++;
                int studentID = rs.getInt("student_id");
                int userID = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String classLevel = rs.getString("classes");
                LocalDate AttandanceDate = LocalDate.parse(rs.getString("date_of_birth"));
                String gender = rs.getString("gender");
                String address = rs.getString("address");
                int enrolledYear = rs.getInt("year_of_enrollment");
                String fatherMobile = rs.getString("father_mobile");
                String motherMobile = rs.getString("mother_mobile");
                String parentAddress = rs.getString("parent_address");
                String course = rs.getString("course");

                ManageStudentsTable manageStudentsTable = new ManageStudentsTable(studentID, userID, classLevel, firstName, lastName, AttandanceDate, gender, course, enrolledYear, fatherMobile, motherMobile, parentAddress, address);
                data.add(manageStudentsTable);
                setMessage("GREEN", "Found Data for given " + getFields + " Having Value :" + tableValue);

            }
            studentsTable.setItems(data);
            if (!dataFound) {
                setMessage("RED", "No students found for " + getFields + " = " + tableValue);

            } else {
                setMessage("GREEN", "Found total of " + totalCount + " Data for " + tableValue);
            }
        } catch (
                SQLException e) {
            setMessage("RED", "Error in database while searching by  : " + getFields + " " + e.getMessage());

        }
    }

    private void loadDataIntoTable() {
        setMessage("", "");
        studentsTable.getItems().clear();
        String query = "SELECT * FROM Students";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int studentID = rs.getInt("student_id");
                int userID = rs.getInt("user_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String classLevel = rs.getString("classes");
                LocalDate AttandanceDate = LocalDate.parse(rs.getString("date_of_birth"));
                String gender = rs.getString("gender");
                String address = rs.getString("address");
                int enrolledYear = rs.getInt("year_of_enrollment");
                String fatherMobile = rs.getString("father_mobile");
                String motherMobile = rs.getString("mother_mobile");
                String parentAddress = rs.getString("parent_address");
                String course = rs.getString("course");
                ManageStudentsTable manageStudentsTable = new ManageStudentsTable(studentID, userID, classLevel, firstName, lastName, AttandanceDate, gender, course, enrolledYear, fatherMobile, motherMobile, parentAddress, address);
                data.add(manageStudentsTable);

            }
            studentsTable.setItems(data);
        } catch (SQLException e) {
            setMessage("RED", "Database error: " + e.getMessage());

        }
    }

    @FXML
    void handleDeleteStudent(ActionEvent event) {

        setMessage("", "");
        if (userIDField.getText() == null || userIDField.getText().isEmpty()) {
            setMessage("RED", "❌ Please provide a valid student ID.");
            return;
        }

        int userID = Integer.parseInt(userIDField.getText());
        showAlert(Alert.AlertType.CONFIRMATION, "Delete Student", "Are you sure you want to delete this student?", "Clicking on OK will delete the student with ID = " + userID + " from the database.");

        if (response.isPresent() && response.get() == ButtonType.OK) {
            String deleteQuery = "DELETE FROM Students WHERE user_id=?";
            String deleteAttendance = "DELETE FROM Attendance WHERE user_id=?";

            try (Connection conn = Connect_With_Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                 PreparedStatement pstmt1 = conn.prepareStatement(deleteAttendance)) {

                pstmt1.setInt(1, userID);
                pstmt.setInt(1, userID);

                pstmt1.executeUpdate(); // delete attendance (optional)
                int affectedRows = pstmt.executeUpdate(); // delete student

                if (affectedRows > 0) {
                   clearFields(true, true, true, true, true, true);
                    loadDataIntoTable();
                    handleClearForm(event);
                    setMessage("GREEN", "✅ Student with ID " + userID + " deleted successfully.");
                } else {
                    setMessage("RED", "❌ No student found with ID: " + userID);
                }
            } catch (SQLException e) {
                setMessage("RED", "❌ Following Database error has encountered while deleting Students: " + e.getMessage());
            }
        } else {
            setMessage("RED", "❌ Deletion cancelled.");
        }
    }

    Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        response = alert.showAndWait();
        return response;
    }

    @FXML
    void handleUpdateStudent(ActionEvent event) {
        setMessage("", "");

        if (userIDField.getText() != null && !userIDField.getText().isEmpty()) {
            int studentid = Integer.parseInt(studentIDField.getText());
            int userID = Integer.parseInt(userIDField.getText());

            showAlert(Alert.AlertType.CONFIRMATION, "Update Student", "Are you sure you want to update this student?", "Clicking on OK will update the student with ID = " + userID + " in the database.");

            if (response.isPresent() && response.get() == ButtonType.OK) {

                // 💡 Only now extract all other fields
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                LocalDate AttandanceDate = AttandanceDatePicker.getValue();
                String course = courseComboBox.getValue();
                int enrolledYear = Integer.parseInt(enrolledYearField.getText());
                long fatherMobile = Long.parseLong(fatherMobileField.getText());
                long motherMobile = Long.parseLong(motherMobileField.getText());
                String address = addressArea.getText();
                String parentAddress = parentAddressArea.getText();
                String Class = classComboBox.getValue();
                String gender = genderComboBox.getValue();

                String updateStudent = "UPDATE Students SET first_name = ?, last_name = ?, classes = ?, date_of_birth = ?, gender = ?, address = ?, year_of_enrollment = ?, father_mobile = ?, mother_mobile = ?, parent_address = ?, course = ? WHERE user_id = ?";
                String deleteAttendance = "DELETE FROM Attendance WHERE user_id = ?";
                String insertAttendance = "INSERT INTO Attendance (user_id, AttandanceDate, classes,status) VALUES (?, ?, ?, ?)";

                try (Connection conn = Connect_With_Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(updateStudent);
                     PreparedStatement pstmtDelete = conn.prepareStatement(deleteAttendance);
                     PreparedStatement pstmtInsert = conn.prepareStatement(insertAttendance)) {

                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, Class);
                    pstmt.setDate(4, Date.valueOf(AttandanceDate));
                    pstmt.setString(5, gender);
                    pstmt.setString(6, address);
                    pstmt.setInt(7, enrolledYear);
                    pstmt.setLong(8, fatherMobile);
                    pstmt.setLong(9, motherMobile);
                    pstmt.setString(10, parentAddress);
                    pstmt.setString(11, course);
                    pstmt.setInt(12, userID);

                    int res1 = pstmt.executeUpdate();

                    // Delete old attendance record(s)
                    pstmtDelete.setInt(1, userID);
                    pstmtDelete.executeUpdate();

                    // Insert updated attendance record
                    pstmtInsert.setInt(1, userID);
                    pstmtInsert.setDate(2, Date.valueOf(AttandanceDate));
                    pstmtInsert.setString(3, Class);
                    pstmtInsert.setString(4, "Updated from JAVA");

                    int res2 = pstmtInsert.executeUpdate();

                    if (res1 > 0 && res2 > 0) {
                        loadDataIntoTable();
                        handleClearForm(event);
                        setMessage("GREEN", "✅  Student having Student ID = " + userID + " updated successfully.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    setMessage("RED", "❌ Error updating student or attendance: " + e.getMessage());
                }
            } else {
                setMessage("RED", "Updation Cancelled");
            }
        } else {
            setMessage("RED", "Please Select a student from table to initiate Updating process");
        }
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        studentIDField.clear();
        userIDField.clear();
        classComboBox.getSelectionModel().clearSelection();
        firstNameField.clear();
        lastNameField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        AttandanceDatePicker.setValue(null);
        courseComboBox.getSelectionModel().clearSelection();
        addressArea.clear();
        enrolledYearField.clear();
        fatherMobileField.clear();
        motherMobileField.clear();
        parentAddressArea.clear();
    }
}