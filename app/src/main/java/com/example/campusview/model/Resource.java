package com.example.campusview.model;

public class Resource {
    private String id;
    private String name;
    private String type;
    private String location;
    private String description;
    private boolean available;

    public Resource(String id, String name, String type, String location, String description, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
} 