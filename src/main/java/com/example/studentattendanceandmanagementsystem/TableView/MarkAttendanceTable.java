package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MarkAttendanceTable {

    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty className;
    private StringProperty date = new SimpleStringProperty();
    private final StringProperty attendanceStatus;
    private final StringProperty remarks;

    public MarkAttendanceTable(String id, String className, String date, String status, String remarks) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(""); // default, set later
        this.className = new SimpleStringProperty(className);
        this.date = new SimpleStringProperty(date);
        this.attendanceStatus = new SimpleStringProperty(status);
        this.remarks = new SimpleStringProperty(remarks);
    }

    // Set name manually later
    public void setName(String name) {
        this.name.set(name);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getClassName() {
        return className.get();
    }
    public String setClassName(String className) {
        return className;
    }
    public StringProperty classNameProperty() {
        return className;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }


    public String getAttendanceStatus() {
        return attendanceStatus.get();
    }

    public StringProperty attendanceStatusProperty() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String status) {
        this.attendanceStatus.set(status);
    }

    public String getRemarks() {
        return remarks.get();
    }


    public SimpleStringProperty remarksProperty() {
        return (SimpleStringProperty) remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks.set(remarks);
    }

}
