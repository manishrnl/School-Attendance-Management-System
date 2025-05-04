package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ManageStaffsTable {
    private IntegerProperty userID;
    private IntegerProperty slNo;
    private StringProperty firstName;
    private StringProperty lastName;

    private StringProperty department;
    private StringProperty position;
    private LongProperty mobile;
    private ObjectProperty<LocalDate> hireDate;

    public ManageStaffsTable(int slNo,int userID,  String firstName, String lastName, String department, String position, long mobile, LocalDate hireDate) {
        this.userID = new SimpleIntegerProperty(userID);
        this.slNo = new SimpleIntegerProperty(slNo);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);

        this.department = new SimpleStringProperty(department);
        this.position = new SimpleStringProperty(position);
        this.mobile = new SimpleLongProperty(mobile);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
    }

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public int getSlNo() {
        return slNo.get();
    }

    public IntegerProperty slNoProperty() {
        return slNo;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }

    public long getMobile() {
        return mobile.get();
    }

    public LongProperty mobileProperty() {
        return mobile;
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }



}