package com.example.campusview.dto

data class ScenicSpotDto(
    val id: Long,
    val name: String,
    val description: String?,
    val location: String?,
    val imagePath: String?,
    val coordinates: String?
)

data class CreateScenicSpotRequest(
    val name: String,
    val description: String?,
    val location: String?,
    val coordinates: String?
)

data class UpdateScenicSpotRequest(
    val name: String?,
    val description: String?,
    val location: String?,
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