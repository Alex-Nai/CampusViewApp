package com.example.campusview.model;

import java.util.Objects;

public class ResourceDto {
    private final Long id;
    private final String name;
    private final String type;
    private final String location;
    private final Integer capacity;
    private final String description;
    private final String status;

    public ResourceDto(Long id, String name, String type, String location,
                      Integer capacity, String description, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDto that = (ResourceDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }
} 