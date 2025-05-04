package com.example.studentattendanceandmanagementsystem.SessionManager;

public class SessionManager {
    private static SessionManager instance;
    private int userID;
    private String userName;

    private SessionManager() {}
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUser( int userID,String userName) {
        this.userName = userName;
        this.userID = userID;
    }

}