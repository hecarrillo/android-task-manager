package com.example.task_manager;

public class Task {
    private int id;
    private String name;
    private String description;
    private String date;
    private String time;
    private String reminder;
    private int categoryId;  // ID de la categoría como clave foránea
    private String categoryName;  // Nombre de la categoría para mostrar
    private String contact;
    private boolean done;

    // Constructor con todos los campos, incluyendo categoryId y categoryName
    public Task(int id, String name, String description, String date, String time, String reminder, int categoryId, String categoryName, String contact, boolean done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.time = time;
        this.reminder = reminder;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.contact = contact;
        this.done = done;
    }

    // Getters y Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
