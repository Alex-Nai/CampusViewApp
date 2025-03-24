package com.example.campusview.model

import java.time.LocalDateTime

// 用户认证相关
data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val role: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)

data class RegisterResponse(
    val userId: Long,
    val username: String,
    val email: String
)

// 教室相关
data class ClassroomDto(
    val id: Long,
    val buildingType: String,
    val roomNumber: String,
    val capacity: Int,
    val isAvailable: Boolean,
    val currentStatus: String?,
    val lastUpdated: LocalDateTime
)

// 资源预约相关
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

data class CreateBookingRequest(
    val resourceId: Long,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?
)

// 预约记录相关
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

// 景点相关
data class ScenicSpotDto(
    val id: Long,
    val name: String,
    val description: String?,
    val location: String?,
    val imagePath: String?,
    val coordinates: String?
)

data class ImageRecognitionRequest(
    val imageBase64: String
)

data class ImageRecognitionResponse(
    val spotId: Long,
    val name: String,
    val description: String?,
    val confidence: Double
) 