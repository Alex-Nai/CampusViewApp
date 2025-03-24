package com.example.campusview.service;

import com.example.campusview.entity.BookingRecord;
import com.example.campusview.entity.Resource;
import com.example.campusview.model.BookingRecordDto;
import com.example.campusview.model.PageResponse;
import com.example.campusview.repository.BookingRepository;
import com.example.campusview.repository.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ResourceRepository resourceRepository;

    public BookingService(BookingRepository bookingRepository, ResourceRepository resourceRepository) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
    }

    public PageResponse<BookingRecordDto> getBookingsByUserId(Long userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        Page<BookingRecord> bookingPage = bookingRepository.findByUserId(userId, pageable);
        
        return new PageResponse<>(
            bookingPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList()),
            bookingPage.getTotalElements(),
            bookingPage.getTotalPages(),
            bookingPage.getNumber() + 1,
            bookingPage.getSize(),
            bookingPage.hasNext()
        );
    }

    @Transactional
    public BookingRecordDto createBooking(Long userId, Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        Resource resource = resourceRepository.findById(resourceId)
            .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        // 检查资源是否可用
        if (!"available".equals(resource.getStatus())) {
            throw new IllegalArgumentException("Resource is not available");
        }

        // 检查时间冲突
        // TODO: 添加时间冲突检查逻辑

        BookingRecord booking = new BookingRecord(resource, userId, startTime, endTime);
        return toDto(bookingRepository.save(booking));
    }

    @Transactional
    public void cancelBooking(Long userId, Long bookingId) {
        BookingRecord booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to cancel this booking");
        }

        if (!"pending".equals(booking.getStatus()) && !"approved".equals(booking.getStatus())) {
            throw new IllegalArgumentException("Booking cannot be cancelled");
        }

        booking.setStatus("cancelled");
        bookingRepository.save(booking);
    }

    private BookingRecordDto toDto(BookingRecord booking) {
        return new BookingRecordDto(
            booking.getId(),
            booking.getResource().getId(),
            booking.getResource().getName(),
            booking.getUserId(),
            booking.getStartTime(),
            booking.getEndTime(),
            booking.getStatus(),
            booking.getBookingTime(),
            booking.getRemarks()
        );
    }
} 