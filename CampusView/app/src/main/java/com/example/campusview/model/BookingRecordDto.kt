package com.example.campusview.model

import java.time.LocalDateTime

data class BookingRecordDto(
    val id: Long,
    val resourceId: Long,
    val resourceName: String,
    val userId: Long,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val status: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) 