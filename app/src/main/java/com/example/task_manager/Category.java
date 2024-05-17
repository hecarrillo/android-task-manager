package com.example.task_manager;

public class Category {
    private int id;
    private String name;

    // Constructor con ID, útil cuando se recupera desde la base de datos
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Constructor sin ID, útil para la creación inicial antes de obtener un ID de la base de datos
    public Category(String name) {
        this.name = name;
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
}
