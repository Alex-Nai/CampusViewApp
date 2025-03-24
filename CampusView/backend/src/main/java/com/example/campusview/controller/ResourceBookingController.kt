package com.example.campusview.controller

import com.example.campusview.dto.ResourceBookingDto
import com.example.campusview.dto.CreateResourceBookingRequest
import com.example.campusview.dto.UpdateResourceBookingRequest
import com.example.campusview.service.ResourceBookingService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/resources")
class ResourceBookingController(
    private val resourceBookingService: ResourceBookingService
) {
    @GetMapping
    fun getAllResources(): ResponseEntity<List<ResourceBookingDto>> =
        ResponseEntity.ok(resourceBookingService.getAllResources())

    @GetMapping("/type/{type}")
    fun getResourcesByType(@PathVariable type: String): ResponseEntity<List<ResourceBookingDto>> =
        ResponseEntity.ok(resourceBookingService.getResourcesByType(type))

    @GetMapping("/available")
    fun getAvailableResources(): ResponseEntity<List<ResourceBookingDto>> =
        ResponseEntity.ok(resourceBookingService.getAvailableResources())

    @GetMapping("/available/time")
    fun getAvailableResourcesForTimeSlot(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startTime: LocalDateTime?,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endTime: LocalDateTime?
    ): ResponseEntity<List<ResourceBookingDto>> =
        ResponseEntity.ok(resourceBookingService.getAvailableResourcesForTimeSlot(startTime, endTime))

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createResource(@RequestBody request: CreateResourceBookingRequest): ResponseEntity<ResourceBookingDto> =
        ResponseEntity.ok(resourceBookingService.createResource(request))

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateResource(
        @PathVariable id: Long,
        @RequestBody request: UpdateResourceBookingRequest
    ): ResponseEntity<ResourceBookingDto> =
        ResponseEntity.ok(resourceBookingService.updateResource(id, request))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteResource(@PathVariable id: Long): ResponseEntity<Unit> {
        resourceBookingService.deleteResource(id)
        return ResponseEntity.ok().build()
    }
} 