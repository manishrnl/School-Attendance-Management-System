package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewAttandanceTable extends MarkAttendanceTable {
    private int id;

  //  private final StringProperty name;
    private StringProperty date = new SimpleStringProperty();
    private final StringProperty attendanceStatus;
    private final StringProperty remarks;

    public ViewAttandanceTable( String date, String status, String remarks) {
        super("", "", date, status, remarks); // or use real values if needed
       // this.name = new SimpleStringProperty(name); // default, set later
        this.date = new SimpleStringProperty(date);
        this.attendanceStatus = new SimpleStringProperty(status);
        this.remarks = new SimpleStringProperty(remarks);
    }
/*
    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }
*/
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

