package com.example.campusview.dto

import java.time.LocalDateTime

data class BookingRecordDto(
    val id: Long,
    val resourceId: Long,
    val userId: Long,
    val username: String,
    val bookingTime: LocalDateTime,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val status: String
)

data class CreateBookingRequest(
    val resourceId: Long,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?
)

data class UpdateBookingStatusRequest(
    val status: String
) 