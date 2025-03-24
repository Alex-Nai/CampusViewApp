package com.example.campusview.service

import com.example.campusview.dto.ResourceBookingDto
import com.example.campusview.dto.CreateResourceBookingRequest
import com.example.campusview.dto.UpdateResourceBookingRequest
import com.example.campusview.entity.ResourceBooking
import com.example.campusview.repository.ResourceBookingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ResourceBookingService(
    private val resourceBookingRepository: ResourceBookingRepository
) {
    fun getAllResources(): List<ResourceBookingDto> =
        resourceBookingRepository.findAll().map { it.toDto() }

    fun getResourcesByType(type: String): List<ResourceBookingDto> =
        resourceBookingRepository.findByResourceType(type).map { it.toDto() }

    fun getAvailableResources(): List<ResourceBookingDto> =
        resourceBookingRepository.findByIsAvailable(true).map { it.toDto() }

    fun getAvailableResourcesForTimeSlot(startTime: LocalDateTime?, endTime: LocalDateTime?): List<ResourceBookingDto> =
        resourceBookingRepository.findAvailableForTimeSlot(startTime, endTime).map { it.toDto() }

    @Transactional
    fun createResource(request: CreateResourceBookingRequest): ResourceBookingDto {
        val resource = ResourceBooking(
            resourceType = request.resourceType,
            resourceName = request.resourceName,
            location = request.location,
            capacity = request.capacity,
            startTime = request.startTime,
            endTime = request.endTime,
            description = request.description
        )
        return resourceBookingRepository.save(resource).toDto()
    }

    @Transactional
    fun updateResource(id: Long, request: UpdateResourceBookingRequest): ResourceBookingDto {
        val resource = resourceBookingRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Resource not found") }

        val updatedResource = resource.copy(
            isAvailable = request.isAvailable ?: resource.isAvailable,
            startTime = request.startTime ?: resource.startTime,
            endTime = request.endTime ?: resource.endTime,
            description = request.description ?: resource.description
        )

        return resourceBookingRepository.save(updatedResource).toDto()
    }

    fun deleteResource(id: Long) {
        resourceBookingRepository.deleteById(id)
    }

    private fun ResourceBooking.toDto() = ResourceBookingDto(
        id = id,
        resourceType = resourceType,
        resourceName = resourceName,
        location = location,
        capacity = capacity,
        startTime = startTime,
        endTime = endTime,
        isAvailable = isAvailable,
        description = description
    )
} 