package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.*;

import java.time.LocalDate;

public class ManageTeachersTable {

    private final IntegerProperty userID;
    private final StringProperty fullName;
    private final LongProperty mobile;
    private final StringProperty department;
    private final StringProperty specialisation;
    private final ObjectProperty<LocalDate> hireDate;
    private final IntegerProperty experience;
    private final ObjectProperty<LocalDate> lastTeached;
    private final StringProperty subjectTeaching;

    public ManageTeachersTable(int userID, String fullName, String department,
                               LocalDate hireDate, LocalDate lastTeached, int experience,
                               String subjectTeaching, String specialisation, long mobile) {

        this.userID = new SimpleIntegerProperty(userID);
        this.fullName = new SimpleStringProperty(fullName);
        this.mobile = new SimpleLongProperty(mobile);
        this.department = new SimpleStringProperty(department);
        this.specialisation = new SimpleStringProperty(specialisation);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.experience = new SimpleIntegerProperty(experience);
        this.lastTeached = new SimpleObjectProperty<>(lastTeached);
        this.subjectTeaching = new SimpleStringProperty(subjectTeaching);
    }



    // Getters and property methods

    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public long getMobile() {
        return mobile.get();
    }

    public LongProperty mobileProperty() {
        return mobile;
    }

    public String getDepartment() {
        return department.get();
    }

    public StringProperty departmentProperty() {
        return department;
    }

    public String getSpecialisation() {
        return specialisation.get();
    }

    public StringProperty specialisationProperty() {
        return specialisation;
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }

    public ObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }

    public int getExperience() {
        return experience.get();
    }

    public IntegerProperty experienceProperty() {
        return experience;
    }

    public LocalDate getLastTeached() {
        return lastTeached.get();
    }

    public ObjectProperty<LocalDate> lastTeachedProperty() {
        return lastTeached;
    }

    public String getSubjectTeaching() {
        return subjectTeaching.get();
    }

    public StringProperty subjectTeachingProperty() {
        return subjectTeaching;
    }
}
