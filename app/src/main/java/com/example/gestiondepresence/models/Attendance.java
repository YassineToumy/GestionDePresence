package com.example.gestiondepresence.models;
public class Attendance {
    private int id;
    private int studentId;
    private String date;
    private String status; // "present", "absent", "retard"
    private String sessionName;

    // Constructors
    public Attendance() {
    }

    public Attendance(int id, int studentId, String date, String status, String sessionName) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.sessionName = sessionName;
    }

    public Attendance(int studentId, String date, String status, String sessionName) {
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.sessionName = sessionName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
