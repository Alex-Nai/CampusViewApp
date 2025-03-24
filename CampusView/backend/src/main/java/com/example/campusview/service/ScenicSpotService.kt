package com.example.campusview.service

import com.example.campusview.dto.*
import com.example.campusview.entity.ScenicSpot
import com.example.campusview.repository.ScenicSpotRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ScenicSpotService(
    private val scenicSpotRepository: ScenicSpotRepository
) {
    fun getAllSpots(): List<ScenicSpotDto> =
        scenicSpotRepository.findAll().map { it.toDto() }

    fun searchSpots(keyword: String): List<ScenicSpotDto> =
        scenicSpotRepository.findByNameContainingIgnoreCase(keyword).map { it.toDto() }

    @Transactional
    fun createSpot(request: CreateScenicSpotRequest): ScenicSpotDto {
        val spot = ScenicSpot(
            name = request.name,
            description = request.description,
            location = request.location,
            coordinates = request.coordinates
        )
        return scenicSpotRepository.save(spot).toDto()
    }

    @Transactional
    fun updateSpot(id: Long, request: UpdateScenicSpotRequest): ScenicSpotDto {
        val spot = scenicSpotRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Scenic spot not found") }

        val updatedSpot = spot.copy(
            name = request.name ?: spot.name,
            description = request.description ?: spot.description,
            location = request.location ?: spot.location,
            coordinates = request.coordinates ?: spot.coordinates
        )

        return scenicSpotRepository.save(updatedSpot).toDto()
    }

    fun deleteSpot(id: Long) {
        scenicSpotRepository.deleteById(id)
    }

    @Transactional
    fun recognizeImage(request: ImageRecognitionRequest): ImageRecognitionResponse {
        // TODO: 实现图像识别逻辑
        // 这里应该调用图像识别服务来处理图片并返回匹配结果
        // 目前返回模拟数据
        val spots = scenicSpotRepository.findAll()
        if (spots.isEmpty()) {
            throw IllegalStateException("No scenic spots available")
        }
        val spot = spots.random()
        return ImageRecognitionResponse(
            spotId = spot.id,
            name = spot.name,
            description = spot.description,
            confidence = 0.95
        )
    }

    private fun ScenicSpot.toDto() = ScenicSpotDto(
        id = id,
        name = name,
        description = description,
        location = location,
        imagePath = imagePath,
        coordinates = coordinates
    )
} 