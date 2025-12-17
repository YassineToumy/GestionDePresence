package com.example.gestiondepresence.models;

public class Seance {
    private int id;
    private int classId;
    private String className;
    private String subjectName;      // Nom de la matière
    private int seanceNumber;        // Numéro de la séance
    private String date;
    private String time;
    private String status;           // "active", "completed"

    // Constructors
    public Seance() {
    }

    public Seance(int classId, String className, String subjectName, int seanceNumber, String date, String time) {
        this.classId = classId;
        this.className = className;
        this.subjectName = subjectName;
        this.seanceNumber = seanceNumber;
        this.date = date;
        this.time = time;
        this.status = "active";
    }

    public Seance(int id, int classId, String className, String subjectName, int seanceNumber, String date, String time, String status) {
        this.id = id;
        this.classId = classId;
        this.className = className;
        this.subjectName = subjectName;
        this.seanceNumber = seanceNumber;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSeanceNumber() {
        return seanceNumber;
    }

    public void setSeanceNumber(int seanceNumber) {
        this.seanceNumber = seanceNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Séance " + seanceNumber + " - " + subjectName + " (" + className + ")";
    }
}