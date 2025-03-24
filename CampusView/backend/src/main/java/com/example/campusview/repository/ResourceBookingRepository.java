package com.example.campusview.repository;

import com.example.campusview.entity.ResourceBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface ResourceBookingRepository extends JpaRepository<ResourceBooking, Long> {
    Page<ResourceBooking> findByResourceType(String resourceType, Pageable pageable);
    
    Page<ResourceBooking> findByIsAvailable(Boolean isAvailable, Pageable pageable);
    
    @Query("SELECT rb FROM ResourceBooking rb WHERE rb.startTime <= :endTime AND rb.endTime >= :startTime")
    Page<ResourceBooking> findOverlappingBookings(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
} 