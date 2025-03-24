package com.example.campusview.model;

public class Classroom {
    private String id;
    private String name;
    private String type;
    private String location;
    private int capacity;
    private boolean available;

    public Classroom(String id, String name, String type, String location, int capacity, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
} 