package com.example.campusview.service

import com.example.campusview.dto.BookingRecordDto
import com.example.campusview.dto.CreateBookingRequest
import com.example.campusview.dto.UpdateBookingStatusRequest
import com.example.campusview.entity.BookingRecord
import com.example.campusview.repository.BookingRecordRepository
import com.example.campusview.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingRecordService(
    private val bookingRecordRepository: BookingRecordRepository,
    private val userRepository: UserRepository
) {
    fun getUserBookings(userId: Long): List<BookingRecordDto> =
        bookingRecordRepository.findByUserId(userId).map { it.toDto() }

    fun getResourceBookings(resourceId: Long): List<BookingRecordDto> =
        bookingRecordRepository.findByResourceId(resourceId).map { it.toDto() }

    @Transactional
    fun createBooking(request: CreateBookingRequest): BookingRecordDto {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByUsername(username)
            ?: throw IllegalStateException("User not found")

        // 检查时间冲突
        val conflictingBookings = bookingRecordRepository.findConflictingBookings(
            request.resourceId,
            request.startTime,
            request.endTime
        )
        if (conflictingBookings.isNotEmpty()) {
            throw IllegalStateException("Resource is already booked for the requested time slot")
        }

        val booking = BookingRecord(
            resourceId = request.resourceId,
            user = user,
            startTime = request.startTime,
            endTime = request.endTime
        )
        return bookingRecordRepository.save(booking).toDto()
    }

    @Transactional
    fun updateBookingStatus(id: Long, request: UpdateBookingStatusRequest): BookingRecordDto {
        val booking = bookingRecordRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Booking not found") }

        val updatedBooking = booking.copy(
            status = request.status
        )

        return bookingRecordRepository.save(updatedBooking).toDto()
    }

    fun deleteBooking(id: Long) {
        bookingRecordRepository.deleteById(id)
    }

    private fun BookingRecord.toDto() = BookingRecordDto(
        id = id,
        resourceId = resourceId,
        userId = user.id,
        username = user.username,
        bookingTime = bookingTime,
        startTime = startTime,
        endTime = endTime,
        status = status
    )
} 