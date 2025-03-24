package com.example.campusview.repository;

import com.example.campusview.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Long> {
    List<ScenicSpot> findByNameContainingIgnoreCase(String name);
    List<ScenicSpot> findByLocationContainingIgnoreCase(String location);
} 