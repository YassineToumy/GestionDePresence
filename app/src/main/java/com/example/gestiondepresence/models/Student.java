package com.example.gestiondepresence.models;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String group;
    private int classId;

    // Constructors
    public Student() {
    }

    public Student(int id, String name, String surname, String group, int classId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.classId = classId;
    }

    public Student(String name, String surname, String group, int classId) {
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.classId = classId;
    }

    public Student(String name, String surname, String group) {
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.classId = -1;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return name + " " + surname + " - " + group;
    }
}