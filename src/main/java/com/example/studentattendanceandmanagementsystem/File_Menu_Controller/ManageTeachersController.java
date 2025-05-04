package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.TableView.ManageTeachersTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class ManageTeachersController {

    public TextField fullNameField;
    public TextField mobileField;
    public TextField userIDField;
    public DatePicker hireDatePicker;
    public DatePicker lastTeachedField;
    public TextField experienceField;
    public ComboBox departmentComboField;
    public DatePicker searchByHireDate;
    public Text errorText;
    public AnchorPane anchorPane;
    public ComboBox subjectTeachingFieldCombo;
    public ComboBox specialisationFieldCombo;
    public ComboBox searchByDepartmentCombo;

    @FXML
    private Button addButton, updateButton, deleteButton, clearButton, editButton, refreshButton;
    public TextField searchByTeacherMobile, searchByExperienceYear, searchByuserID,
            searchByTeacherName;
    boolean dataUpdated = false;

    @FXML
    private TableView<ManageTeachersTable> manageTeachersTable;
    @FXML
    private TableColumn<ManageTeachersTable, Integer> userIDdColumn, experienceColumn;
    @FXML
    private TableColumn<ManageTeachersTable, Long> MobileColumn;
    @FXML
    private TableColumn<ManageTeachersTable, String> NameColumn, departmentColumn, subjectTeachingColumn, specialisationColumn;
    @FXML
    private TableColumn<ManageTeachersTable, LocalDate> hireDateColumn, lastTeachedColumn;

    int totalCount = 0;

    public String years_of_experience = "years_of_experience", user_id = "user_id",
            teacher_Mobile = "teacher_Mobile", full_name = "full_name", department = "department", hire_date = "hire_date";

    ObservableList<ManageTeachersTable> data = FXCollections.observableArrayList();
    Optional<ButtonType> response;

    @FXML
    void initialize() {
        setMessage("", "");
        searchByuserID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(false, true, true, true, true, true);
                searchFunction(user_id, newValue);
            }
        });
        searchByTeacherName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, false, true, true, true, true);
                searchFunction(full_name, newValue);
            }
        });
        searchByExperienceYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, false, true, true, true);
                searchFunction(years_of_experience, searchByExperienceYear.getText());
            }
        });
        searchByTeacherMobile.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, true, false, true, true);
                searchFunction(teacher_Mobile, newValue);
            }
        });

        searchByDepartmentCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearFields(true, true, true, true, false, true); // Clear others, not this
                // ComboBox
                searchFunction(department, (String) newValue); // ✅ Use newValue directly
            }
        });


        searchByHireDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                clearFields(true, true, true, true, true, false); // Clear necessary fields
                searchFunction(hire_date, String.valueOf(newValue)); // Use
                // newValue directly
                // instead of calling getValue()
            }
        });
        userIDdColumn.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
        MobileColumn.setCellValueFactory(CellData -> CellData.getValue().mobileProperty().asObject());
        experienceColumn.setCellValueFactory(CellData -> CellData.getValue().experienceProperty().asObject());
        NameColumn.setCellValueFactory(CellData -> CellData.getValue().fullNameProperty());
        departmentColumn.setCellValueFactory(CellData -> CellData.getValue().departmentProperty());
        specialisationColumn.setCellValueFactory(CellData -> CellData.getValue().specialisationProperty());
        subjectTeachingColumn.setCellValueFactory(CellData -> CellData.getValue().subjectTeachingProperty());
        hireDateColumn.setCellValueFactory(CellData -> CellData.getValue().hireDateProperty());
        lastTeachedColumn.setCellValueFactory(CellData -> CellData.getValue().lastTeachedProperty());
        loadTeachersTableData();

        manageTeachersTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                ManageTeachersTable selectedRow =
                        manageTeachersTable.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    fullNameField.setText(String.valueOf(selectedRow.getFullName()));
                    userIDField.setText(String.valueOf((Integer) selectedRow.getUserID()));
                    subjectTeachingFieldCombo.setValue(selectedRow.getSubjectTeaching());
                    experienceField.setText(String.valueOf((Integer) selectedRow.getExperience()));
                    hireDatePicker.setValue(selectedRow.getHireDate());
                    departmentComboField.setValue(selectedRow.getDepartment());
                    lastTeachedField.setValue(selectedRow.getLastTeached());
                    specialisationFieldCombo.setValue(selectedRow.getSpecialisation());
                    mobileField.setText(String.valueOf(selectedRow.getMobile()));
                }
            }
        });
    }

    private void loadTeachersTableData() {
        setMessage("", "");
        String query = "SELECT * FROM Teachers";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            data.clear(); // optional: clear old data if refreshing

            while (rs.next()) {
                int userid = rs.getInt("user_id");
                String fullName = rs.getString("full_name");
                String department = rs.getString("department");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();
                String specialisation = rs.getString("subject_specialization");
                int experienceYear = rs.getInt("years_of_experience");
                LocalDate lastTeached = rs.getDate("last_teached_in_school").toLocalDate();
                long mobile = rs.getLong("teacher_Mobile");
                String subjectTeaching = rs.getString("subject_teaching");
                ManageTeachersTable table = new ManageTeachersTable(userid, fullName, department, hireDate, lastTeached, experienceYear, subjectTeaching, specialisation, mobile
                );
                data.add(table);
            }
            manageTeachersTable.setItems(data);
        } catch (SQLException e) {
            setMessage("RED", "Database error : " + e.getMessage());
        }
    }

    void setMessage(String Color, String Message) {
        errorText.setText(Message);
        errorText.setStyle("-fx-fill:" + Color);
    }

    private void searchFunction(String getFields, String tableValue) {
        setMessage("", "");
        String query;
        totalCount = 0;
        if (getFields.equals("department")) {
            query = "SELECT * FROM Teachers WHERE " + getFields + " = ?";
        } else {
            query = "SELECT * FROM Teachers WHERE " + getFields + " LIKE ?";
        }
        manageTeachersTable.getItems().clear();
        try (
                Connection conn = Connect_With_Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (getFields.equals("department")) {
                pstmt.setString(1, tableValue);
            } else {
                pstmt.setString(1, "%" + tableValue + "%");  // 🔥 enables partial matching!
            }
            ResultSet rs = pstmt.executeQuery();

            boolean dataFound = false;
            while (rs.next()) {
                dataFound = true;
                totalCount++;
                int userid = rs.getInt("user_id");
                String fullName = rs.getString("full_name");
                String department = rs.getString("department");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();
                String specialisation = rs.getString("subject_specialization");
                int experienceYear = rs.getInt("years_of_experience");
                LocalDate lastTeached = rs.getDate("last_teached_in_school").toLocalDate();
                long mobile = rs.getLong("teacher_Mobile");
                String subjectTeaching = rs.getString("subject_teaching");

                ManageTeachersTable table = new ManageTeachersTable(userid, fullName, department, hireDate, lastTeached, experienceYear, subjectTeaching, specialisation, mobile
                );

                data.add(table);
            }

            manageTeachersTable.setItems(data);
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

    private void clearFields(boolean clearUserid, boolean clearFullName, boolean experienceYear, boolean teacherMobile, boolean department, boolean hireDate) {
        if (clearUserid) searchByuserID.clear();
        if (clearFullName) searchByTeacherName.clear();
        if (experienceYear) searchByExperienceYear.clear();
        if (teacherMobile) searchByTeacherMobile.clear();
        if (department) searchByDepartmentCombo.setValue(null); // Clear ComboBox selection
        if (hireDate) searchByHireDate.setValue(null);          // Clear DatePicker selection

    }

    private boolean checkMobileField(String mobil, String fields) {
        setMessage("", "");
        if (mobil.matches("\\d{1,}")) {
            // System.out.println("Type="+mobil);
            return true;
        } else {
            setMessage("RED", "Please enter digits only in " + fields + " Fields to start " +
                    "updating Records");
            return false;
        }
    }

    @FXML
    public void handleUpdate(ActionEvent actionEvent) {
        setMessage("", "");
        String firstName = null, fullName = null, lastName = null;
        int userid = Integer.parseInt(userIDField.getText());
        fullName = fullNameField.getText();
        int spaceIndex = fullName.indexOf(" ");

        if (spaceIndex != -1) {
            firstName = fullName.substring(0, spaceIndex).trim();
            lastName = fullName.substring(spaceIndex + 1).trim();
            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);

        }
        if (userid != 0 && checkMobileField(mobileField.getText(), "Mobile") &&
                checkMobileField(experienceField.getText(), "Experience")) {
            response = showAlert(Alert.AlertType.CONFIRMATION, "Update Records", "Are you sure " +
                            "you want to update selected Students having user id=" + userid + " .",
                    "Clicking on OK button will update the selected students.");

            if (response.isPresent() && response.get() == ButtonType.OK) {
                String query = "UPDATE Teachers SET first_name=?, last_name=?, " +
                        "subject_teaching=?, hire_date=?, years_of_experience=?, subject_specialization=?, " +
                        "last_teached_in_school=?, teacher_Mobile=?, department=? " +
                        "WHERE user_id=?";
                manageTeachersTable.getItems().clear();
                try (Connection conn = Connect_With_Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, (String) subjectTeachingFieldCombo.getValue());
                    pstmt.setDate(4, Date.valueOf(hireDatePicker.getValue()));
                    pstmt.setInt(5, Integer.parseInt(experienceField.getText()));
                    pstmt.setString(6, (String) specialisationFieldCombo.getValue());
                    pstmt.setDate(7, Date.valueOf(lastTeachedField.getValue()));
                    pstmt.setLong(8, Long.parseLong(mobileField.getText()));
                    pstmt.setString(9, (String) departmentComboField.getValue());
                    pstmt.setInt(10, userid);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        loadTeachersTableData();
                        setMessage("GREEN", "Users updated with user ID = " + userid);
                    }
                } catch (SQLException e) {
                    setMessage("RED", "Error in database while updating: " + e.getMessage());
                    System.out.println("ManageTeachersController.handleUpdate: " + e.getMessage());
                }
            } else setMessage("RED", "You terminated the process by clicking cancel Button");
        }
    }


    public void handleClear(ActionEvent actionEvent) {
        setMessage("", "");
        fullNameField.setText("");
        userIDField.setText("");
        departmentComboField.setValue(null);
        subjectTeachingFieldCombo.setValue(null);
        if (subjectTeachingFieldCombo.isEditable()) {
            subjectTeachingFieldCombo.getEditor().clear();
        }
        experienceField.setText("");
        hireDatePicker.setValue(null);
        lastTeachedField.setValue(null);
        specialisationFieldCombo.setValue(null);
        mobileField.setText("");
    }

    @FXML
    void handleDelete(ActionEvent event) throws SQLException {
        setMessage("", "");
        String id = userIDField.getText(); // get text first

        if (id != null && !id.isEmpty()) {  // correct check
            response = showAlert(Alert.AlertType.CONFIRMATION, "Delete Records", "Are you " +
                            "sure you want to delete selected Teachers having user id=" + id +
                            " .",
                    "Clicking on OK button will Delete the selected Teachers.");
            if (response.isPresent() && response.get() == ButtonType.OK) {
                int userid = Integer.parseInt(id);
                String query = "DELETE FROM Teachers WHERE user_id='" + userid + "'";
                try (Connection conn = Connect_With_Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    int rowAffected = pstmt.executeUpdate();
                    if (rowAffected > 0) {
                        loadTeachersTableData();
                        handleClear(event);
                        setMessage("GREEN", "Records deleted Successfully");
                    }
                }
            } else setMessage("RED", "Operation Cancelled");
        } else
            setMessage("RED", "Click on desired Teacher from table you want to delete to initiate Deleting Process.");
    }


    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        response = alert.showAndWait();
        return response;
    }
}