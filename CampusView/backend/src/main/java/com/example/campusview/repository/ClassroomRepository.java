package com.example.campusview.repository;

import com.example.campusview.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Page<Classroom> findByBuildingType(String buildingType, Pageable pageable);
    Page<Classroom> findByIsAvailable(Boolean isAvailable, Pageable pageable);
    Page<Classroom> findByCapacityGreaterThanEqual(Integer minCapacity, Pageable pageable);
} 