package com.example.campusview;

public class Resource {
    private String name;
    private String type;
    private String status;
    private String description;

    public Resource(String name, String type, String status, String description) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 