package com.example.studentattendanceandmanagementsystem.TableView;

import javafx.beans.property.*;

public class MonthlyReportTable {
    private final IntegerProperty studentId;
    private final StringProperty studentName;
    private final StringProperty className;
    private final StringProperty month;
    private final IntegerProperty totalDays;
    private final IntegerProperty presentDays;
    private final DoubleProperty attendancePercentage;

    public MonthlyReportTable(int studentId, String studentName, String className, 
                             String month, int totalDays, int presentDays, double attendancePercentage) {
        this.studentId = new SimpleIntegerProperty(studentId);
        this.studentName = new SimpleStringProperty(studentName);
        this.className = new SimpleStringProperty(className);
        this.month = new SimpleStringProperty(month);
        this.totalDays = new SimpleIntegerProperty(totalDays);
        this.presentDays = new SimpleIntegerProperty(presentDays);
        this.attendancePercentage = new SimpleDoubleProperty(attendancePercentage);
    }

    // Getters and Property methods
    public int getStudentId() {
        return studentId.get();
    }

    public IntegerProperty studentIdProperty() {
        return studentId;
    }

    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public String getClassName() {
        return className.get();
    }

    public StringProperty classProperty() {
        return className;
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public int getTotalDays() {
        return totalDays.get();
    }

    public IntegerProperty totalDaysProperty() {
        return totalDays;
    }

    public int getPresentDays() {
        return presentDays.get();
    }

    public IntegerProperty presentDaysProperty() {
        return presentDays;
    }

    public double getAttendancePercentage() {
        return attendancePercentage.get();
    }

    public DoubleProperty attendancePercentageProperty() {
        return attendancePercentage;
    }
}