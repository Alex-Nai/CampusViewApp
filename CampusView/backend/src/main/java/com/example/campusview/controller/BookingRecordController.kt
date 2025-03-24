package com.example.campusview.controller

import com.example.campusview.dto.BookingRecordDto
import com.example.campusview.dto.CreateBookingRequest
import com.example.campusview.dto.UpdateBookingStatusRequest
import com.example.campusview.service.BookingRecordService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingRecordController(
    private val bookingRecordService: BookingRecordService
) {
    @GetMapping("/user/{userId}")
    fun getUserBookings(@PathVariable userId: Long): ResponseEntity<List<BookingRecordDto>> =
        ResponseEntity.ok(bookingRecordService.getUserBookings(userId))

    @GetMapping("/resource/{resourceId}")
    fun getResourceBookings(@PathVariable resourceId: Long): ResponseEntity<List<BookingRecordDto>> =
        ResponseEntity.ok(bookingRecordService.getResourceBookings(resourceId))

    @PostMapping
    fun createBooking(@RequestBody request: CreateBookingRequest): ResponseEntity<BookingRecordDto> =
        ResponseEntity.ok(bookingRecordService.createBooking(request))

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateBookingStatus(
        @PathVariable id: Long,
        @RequestBody request: UpdateBookingStatusRequest
    ): ResponseEntity<BookingRecordDto> =
        ResponseEntity.ok(bookingRecordService.updateBookingStatus(id, request))

    @DeleteMapping("/{id}")
    fun deleteBooking(@PathVariable id: Long): ResponseEntity<Unit> {
        bookingRecordService.deleteBooking(id)
        return ResponseEntity.ok().build()
    }
} 