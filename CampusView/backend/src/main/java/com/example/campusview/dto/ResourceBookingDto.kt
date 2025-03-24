package com.example.campusview.dto

import java.time.LocalDateTime

data class ResourceBookingDto(
    val id: Long,
    val resourceType: String,
    val resourceName: String,
    val location: String?,
    val capacity: Int?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val isAvailable: Boolean,
    val description: String?
)

data class CreateResourceBookingRequest(
    val resourceType: String,
    val resourceName: String,
    val location: String?,
    val capacity: Int?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val description: String?
)

data class UpdateResourceBookingRequest(
    val isAvailable: Boolean?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val description: String?
) 