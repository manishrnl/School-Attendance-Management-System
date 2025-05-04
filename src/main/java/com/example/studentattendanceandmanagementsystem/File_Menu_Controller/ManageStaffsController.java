package com.example.studentattendanceandmanagementsystem.File_Menu_Controller;

import com.example.studentattendanceandmanagementsystem.TableView.ManageStaffsTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class ManageStaffsController {
    @FXML
    private Text errorText;
    @FXML
    private TableColumn<ManageStaffsTable, String> positionCol, lastNameCol, firstNameCol,
            departmentCol;
    @FXML
    private TableColumn<ManageStaffsTable, LocalDate> hireDateCol;
    @FXML
    private TableColumn<ManageStaffsTable, Long> mobileCol;
    @FXML
    private TableColumn<ManageStaffsTable, Integer> userIdCol, slNoCol;
    @FXML
    private TableView<ManageStaffsTable> staffTable;
    @FXML
    private ComboBox<String> departmentComboField, searchByDepartmentCombo, positionComboBoxField;
    @FXML
    private TextField firstNameField, lastNameField, userIdField, mobileField, searchByMobile,
            searchByFullName, searchByPosition, searchByuserID;
    @FXML
    private DatePicker hireDateField, searchByHireDate;
    ObservableList<ManageStaffsTable> data = FXCollections.observableArrayList();
    public String user_id = "user_id", staff_Mobile = "staff_Mobile", full_name = "full_name", hire_date = "hire_date", department =
            "department", position = "position";
    int countTotal = 0;
    String query;
    Optional<ButtonType> response;


    @FXML
    void initialize() {
        setMessage("", "");
        loadDataIntoTable();
        searchByuserID.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(false, true, true, true, true, true);
                searchFunction(user_id, newValue.toString());
            }
        });
        searchByFullName.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, false, true, true, true, true);
                searchFunction(full_name, newValue);
            }
        }));
        searchByMobile.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, false, true, true, true);
                searchFunction(staff_Mobile, newValue);
            }
        }));
        searchByPosition.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, true, false, true, true);
                searchFunction(position, newValue);
            }
        }));
        searchByDepartmentCombo.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                clearFields(true, true, true, true, false, true);
                searchFunction(department, newValue);
            }
        }));
        searchByHireDate.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                clearFields(true, true, true, true, true, false);
                searchFunction(hire_date, String.valueOf(newValue));
            }
        }));

        slNoCol.setCellValueFactory(cellData -> cellData.getValue().slNoProperty().asObject());
        userIdCol.setCellValueFactory(cellData -> cellData.getValue().userIDProperty().asObject());
        firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameCol.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        departmentCol.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        positionCol.setCellValueFactory(cellData -> cellData.getValue().positionProperty());
        hireDateCol.setCellValueFactory(cellData -> cellData.getValue().hireDateProperty());
        mobileCol.setCellValueFactory(cellData -> cellData.getValue().mobileProperty().asObject());
        staffTable.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                ManageStaffsTable selectedRow = staffTable.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    setMessage("", "");
                    userIdField.setText(String.valueOf(selectedRow.getUserID()));
                    firstNameField.setText(selectedRow.getFirstName());
                    lastNameField.setText(selectedRow.getLastName());
                    departmentComboField.setValue(selectedRow.getDepartment());
                    positionComboBoxField.setValue(selectedRow.getPosition());
                    hireDateField.setValue(selectedRow.getHireDate());
                    mobileField.setText(String.valueOf((Long) selectedRow.getMobile()));
                }
            }
        });

    }

    private void loadDataIntoTable() {
        setMessage("", "");
        countTotal = 0;
        staffTable.getItems().clear();
        query = "SELECT * FROM Staff ";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                countTotal++;
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                int userID = rs.getInt("user_id");
                String Department = rs.getString("department");
                String Position = rs.getString("position");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();
                Long mobile = rs.getLong("staff_Mobile");
                ManageStaffsTable manageStaffsTable = new ManageStaffsTable(countTotal, userID, first, last, Department, Position, mobile, hireDate);
                data.add(manageStaffsTable);
            }
            staffTable.setItems(data);
            setMessage("GREEN", "Data inserted Successfully into the table");
        } catch (SQLException e) {
            setMessage("RED", "Database Error while inserting data into table : " + e.getMessage());
        }
    }

    private void searchFunction(String field, String newValue) {
        setMessage("", "");
        countTotal = 0;
        data.clear();
        // Validate input
        if (newValue == null || newValue.trim().isEmpty()) return;
        query = "SELECT * FROM Staff WHERE " + field + " LIKE ?";
        try (Connection conn = Connect_With_Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + newValue + "%"); // supports partial match
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                countTotal++;
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");
                int userID = rs.getInt("user_id");
                String Department = rs.getString("department");
                String Position = rs.getString("position");
                LocalDate hireDate = rs.getDate("hire_date").toLocalDate();
                Long mobile = rs.getLong("staff_Mobile");
                ManageStaffsTable manageStaffsTable = new ManageStaffsTable(countTotal,
                        userID, first, last, Department, Position, mobile, hireDate);
                data.add(manageStaffsTable);
            }
            staffTable.setItems(data);
            if (countTotal == 0) {
                setMessage("RED", "No results found for input " + field + " =" + newValue);
            }

        } catch (SQLException e) {
            setMessage("RED", "Database error: " + e.getMessage());
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        setMessage("", "");
        try {
            int userID = Integer.parseInt(userIdField.getText());
            if (userID != 0) {
                response = showAlert(Alert.AlertType.CONFIRMATION, "Delete Records", "Are you " +
                                "sure you want to delete selected Staffs having user id=" + userID + " .",
                        "Clicking on OK button will Delete the selected Staffs.");
                if (response.isPresent() && response.get() == ButtonType.OK) {
                    String query = "DELETE FROM Staff WHERE user_id=?";
                    try (Connection conn = Connect_With_Database.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, userID);
                        int affectedRows = pstmt.executeUpdate();
                        if (affectedRows > 0) {
                            handleClear(event);
                            loadDataIntoTable();
                            setMessage("GREEN", "Staff deleted successfully From database. Data Refreshed");
                        } else
                            setMessage("RED", "No staff found with the given ID.");
                    } catch (SQLException e) {
                        setMessage("RED", "Database error: " + e.getMessage());
                    }
                } else
                    setMessage("RED", "Deletion cancelled by user.");
            } else
                setMessage("RED", "Please enter a valid staff ID to delete.");
        } catch (NumberFormatException e) {
            setMessage("RED", "Invalid staff ID format.");
        }
    }


    @FXML
    void handleUpdate(ActionEvent event) {
        setMessage("", "");
        String Department = departmentComboField.getValue();
        String Position = positionComboBoxField.getValue();
        String userIdText = userIdField.getText();

        if (userIdText.isEmpty()) {
            setMessage("RED", "Select Staff from table to initiate Updating Process.");
            return;
        }

        try {
            int userID = Integer.parseInt(userIdText);
            if (userID == 0) {
                setMessage("RED", "Invalid Staff ID");
                return;
            }

            // Validate required fields
            if (hireDateField.getValue() == null) {
                setMessage("RED", "Hire Date is required");
                return;
            }
            response = showAlert(Alert.AlertType.CONFIRMATION, "Update Records", "Are you sure " +
                            "you want to update selected Students having user id=" + userID + " .",
                    "Clicking on OK button will update the selected students.");

            if (response.isPresent() && response.get() == ButtonType.OK) {
                query = "UPDATE Staff SET first_name=?, last_name=?, department=?, " +
                        "position=?, staff_Mobile=?, hire_date=? WHERE user_id=?";

                try (Connection conn = Connect_With_Database.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, firstNameField.getText());
                    pstmt.setString(2, lastNameField.getText());
                    pstmt.setString(3, Department);
                    pstmt.setString(4, Position);
                    pstmt.setLong(5, Long.parseLong(mobileField.getText()));
                    pstmt.setDate(6, Date.valueOf(hireDateField.getValue()));
                    pstmt.setInt(7, userID);

                    int affectedRows = pstmt.executeUpdate();

                    if (affectedRows > 0) {
                        setMessage("GREEN", "Records with ID = " + userID + " Updated Successfully");
                        loadDataIntoTable();
                    } else {
                        setMessage("RED", "No records were updated. Staff ID might not exist.");
                    }
                }
            } else setMessage("RED", "Operation Cancelled");
        } catch (NumberFormatException e) {
            setMessage("RED", "Invalid number format in Mobile or User ID field");
        } catch (SQLException e) {
            setMessage("RED", "Database error: " + e.getMessage());
        } catch (Exception e) {
            setMessage("RED", "Error: " + e.getMessage());
        }
    }

    private void clearFields(boolean clearUserID, boolean studentName, boolean Mobile, boolean position, boolean department, boolean hireDate) {
        if (clearUserID) searchByuserID.clear();
        if (studentName) searchByFullName.clear();
        if (Mobile) searchByMobile.clear();
        if (position) searchByPosition.clear();
        if (department) searchByDepartmentCombo.getSelectionModel().clearSelection();
        if (hireDate) searchByHireDate.setValue(null);
    }


    @FXML
    void handleClear(ActionEvent event) {
        userIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        positionComboBoxField.setValue(null);
        departmentComboField.setValue(null);
        hireDateField.setValue(null);
        mobileField.clear();
    }

    void setMessage(String color, String message) {
        errorText.setText(message);
        errorText.setStyle("-fx-fill:" + color + ";");
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String header, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        response = alert.showAndWait();
        return response;
    }

    public void LoadTable(ActionEvent actionEvent) {
        loadDataIntoTable();
    }
}
