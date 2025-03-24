package com.example.campusview.repository;

import com.example.campusview.entity.BookingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingRecord, Long> {
    Page<BookingRecord> findByUserId(Long userId, Pageable pageable);
    Page<BookingRecord> findByResourceId(Long resourceId, Pageable pageable);
} 