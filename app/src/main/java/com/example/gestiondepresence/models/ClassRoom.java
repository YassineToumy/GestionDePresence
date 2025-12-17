package com.example.gestiondepresence.models;

public class ClassRoom {
    private int id;
    private String className;
    private String description;

    // Constructors
    public ClassRoom() {
    }

    public ClassRoom(int id, String className, String description) {
        this.id = id;
        this.className = className;
        this.description = description;
    }

    public ClassRoom(String className, String description) {
        this.className = className;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return className;
    }
}