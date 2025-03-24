package com.example.campusview.dto

import java.time.LocalDateTime

data class ClassroomDto(
    val id: Long,
    val buildingType: String,
    val roomNumber: String,
    val capacity: Int,
    val isAvailable: Boolean,
    val currentStatus: String?,
    val lastUpdated: LocalDateTime
)

data class CreateClassroomRequest(
    val buildingType: String,
    val roomNumber: String,
    val capacity: Int
)

data class UpdateClassroomRequest(
    val isAvailable: Boolean?,
    val currentStatus: String?
) 