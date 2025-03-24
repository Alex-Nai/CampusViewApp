package com.example.campusview.service

import com.example.campusview.dto.ClassroomDto
import com.example.campusview.dto.CreateClassroomRequest
import com.example.campusview.dto.UpdateClassroomRequest
import com.example.campusview.entity.Classroom
import com.example.campusview.repository.ClassroomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClassroomService(
    private val classroomRepository: ClassroomRepository
) {
    fun getAllClassrooms(): List<ClassroomDto> =
        classroomRepository.findAll().map { it.toDto() }

    fun getClassroomsByBuilding(buildingType: String): List<ClassroomDto> =
        classroomRepository.findByBuildingType(buildingType).map { it.toDto() }

    fun getAvailableClassrooms(): List<ClassroomDto> =
        classroomRepository.findByIsAvailable(true).map { it.toDto() }

    @Transactional
    fun createClassroom(request: CreateClassroomRequest): ClassroomDto {
        val classroom = Classroom(
            buildingType = request.buildingType,
            roomNumber = request.roomNumber,
            capacity = request.capacity
        )
        return classroomRepository.save(classroom).toDto()
    }

    @Transactional
    fun updateClassroom(id: Long, request: UpdateClassroomRequest): ClassroomDto {
        val classroom = classroomRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Classroom not found") }

        val updatedClassroom = classroom.copy(
            isAvailable = request.isAvailable ?: classroom.isAvailable,
            currentStatus = request.currentStatus ?: classroom.currentStatus
        )

        return classroomRepository.save(updatedClassroom).toDto()
    }

    fun deleteClassroom(id: Long) {
        classroomRepository.deleteById(id)
    }

    private fun Classroom.toDto() = ClassroomDto(
        id = id,
        buildingType = buildingType,
        roomNumber = roomNumber,
        capacity = capacity,
        isAvailable = isAvailable,
        currentStatus = currentStatus,
        lastUpdated = lastUpdated
    )
} 