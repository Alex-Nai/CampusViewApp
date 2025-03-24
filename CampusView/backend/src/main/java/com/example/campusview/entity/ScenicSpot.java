package com.example.campusview.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scenic_spots")
@Data
@NoArgsConstructor
public class ScenicSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private String imagePath;

    private String coordinates;

    @Column(columnDefinition = "TEXT")
    private String features;

    public ScenicSpot(String name, String description) {
        this.name = name;
        this.description = description;
    }
} 