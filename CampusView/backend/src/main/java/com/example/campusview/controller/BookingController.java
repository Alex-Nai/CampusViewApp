package com.example.campusview.controller;

import com.example.campusview.model.BookingRecordDto;
import com.example.campusview.model.PageResponse;
import com.example.campusview.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/my")
    public ResponseEntity<PageResponse<BookingRecordDto>> getMyBookings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId, page, size));
    }

    @PostMapping
    public ResponseEntity<BookingRecordDto> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long resourceId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.createBooking(userId, resourceId, startTime, endTime));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long bookingId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        bookingService.cancelBooking(userId, bookingId);
        return ResponseEntity.ok().build();
    }
} 