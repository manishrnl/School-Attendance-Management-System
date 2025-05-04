package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ManageStudentsTable {
    private final IntegerProperty studentID;
    private final IntegerProperty userId;
    private final IntegerProperty enrolledYear; // Corrected to IntegerProperty
    private final StringProperty fatherMobile;
    private final StringProperty motherMobile;
    private final StringProperty parentAddress;
    private final StringProperty address;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> dob; // Keeping this as LocalDate
    private final StringProperty gender;
    private final StringProperty classLevel;
    private final StringProperty course;

    public ManageStudentsTable(int studentID, int userId, String classLevel,
                               String firstName, String lastName, LocalDate dob,
                               String gender, String course, int enrolledYear,
                               String fatherMobile, String motherMobile, String parentAddress, String address) {
        this.studentID = new SimpleIntegerProperty(studentID);
        this.userId = new SimpleIntegerProperty(userId);
        this.enrolledYear = new SimpleIntegerProperty(enrolledYear); // Changed to
        // IntegerProperty
        this.fatherMobile = new SimpleStringProperty(fatherMobile);
        this.motherMobile = new SimpleStringProperty(motherMobile);
        this.parentAddress = new SimpleStringProperty(parentAddress);
        this.address = new SimpleStringProperty(address);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.dob = new SimpleObjectProperty<>(dob);
        this.gender = new SimpleStringProperty(gender);
        this.classLevel = new SimpleStringProperty(classLevel);
        this.course = new SimpleStringProperty(course);
    }

    // Getter and Setter methods
    public int getStudentID() {
        return studentID.get();
    }

    public IntegerProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID.set(studentID);
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public LocalDate getDob() {
        return dob.get(); // Return as LocalDate
    }

    public void setDob(LocalDate dob) {
        this.dob.set(dob);
    }

    public ObjectProperty<LocalDate> dobProperty() {
        return dob;
    }

    public int getEnrolledYear() {
        return enrolledYear.get();  // Return as Integer
    }

    public IntegerProperty enrolledYearProperty() {
        return enrolledYear;
    }

    public void setEnrolledYear(int enrolledYear) {
        this.enrolledYear.set(enrolledYear);
    }

    public String getFatherMobile() {
        return fatherMobile.get();
    }

    public StringProperty fatherMobileProperty() {
        return fatherMobile;
    }

    public void setFatherMobile(String fatherMobile) {
        this.fatherMobile.set(fatherMobile);
    }

    public String getMotherMobile() {
        return motherMobile.get();
    }

    public StringProperty motherMobileProperty() {
        return motherMobile;
    }

    public void setMotherMobile(String motherMobile) {
        this.motherMobile.set(motherMobile);
    }

    public String getParentAddress() {
        return parentAddress.get();
    }

    public StringProperty parentAddressProperty() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress.set(parentAddress);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getClassLevel() {
        return classLevel.get();
    }

    public StringProperty classLevelProperty() {
        return classLevel;
    }

    public void setClassLevel(String classLevel) {
        this.classLevel.set(classLevel);
    }

    public String getCourse() {
        return course.get();
    }

    public StringProperty courseProperty() {
        return course;
    }

    public void setCourse(String course) {
        this.course.set(course);
    }
}
