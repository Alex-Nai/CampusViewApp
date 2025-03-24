package com.example.campusview.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "resource_bookings")
@Data
@NoArgsConstructor
public class ResourceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String resourceType;

    @Column(nullable = false)
    private String resourceName;

    private String location;

    private Integer capacity;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Boolean isAvailable = true;

    private String description;

    public ResourceBooking(String resourceType, String resourceName) {
        this.resourceType = resourceType;
        this.resourceName = resourceName;
    }
} 