package com.example.campusview.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "classroom")
@Data
@NoArgsConstructor
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_type", nullable = false)
    private String buildingType;  // '主楼' 或 '中楼'

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "current_status")
    private String currentStatus;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    public Classroom(String buildingType, String roomNumber, Integer capacity) {
        this.buildingType = buildingType;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.lastUpdated = LocalDateTime.now();
    }
} 