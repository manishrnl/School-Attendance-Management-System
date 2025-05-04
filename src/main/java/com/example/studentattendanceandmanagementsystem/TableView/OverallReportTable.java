package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.*;

public class OverallReportTable {
    private IntegerProperty studentID, totalDays, daysPresent;
    private StringProperty name, Classes;
    private DoubleProperty Attandance;

    public OverallReportTable(int studentID, int totalDays, int daysPresent, String name,
                              String Classes, double Attandance) {
        this.studentID = new SimpleIntegerProperty(studentID);
        this.totalDays = new SimpleIntegerProperty(totalDays);
        this.daysPresent = new SimpleIntegerProperty(daysPresent);
        this.name = new SimpleStringProperty(name);
        this.Classes = new SimpleStringProperty(Classes);
        this.Attandance = new SimpleDoubleProperty(Attandance);
    }

    public int getStudentID() {
        return studentID.get();
    }

    public IntegerProperty studentIDProperty() {
        return studentID;
    }

    public int getTotalDays() {
        return totalDays.get();
    }

    public IntegerProperty totalDaysProperty() {
        return totalDays;
    }

    public int getDaysPresent() {
        return daysPresent.get();
    }

    public IntegerProperty daysPresentProperty() {
        return daysPresent;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getClasses() {
        return Classes.get();
    }

    public StringProperty classProperty() {
        return Classes;
    }

    public double getAttandance() {
        return Attandance.get();
    }

    public double getAttendancePercentage() {
        return Attandance.get();
    }
}
