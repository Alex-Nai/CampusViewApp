package com.example.campusview.controller

import com.example.campusview.dto.ClassroomDto
import com.example.campusview.dto.CreateClassroomRequest
import com.example.campusview.dto.UpdateClassroomRequest
import com.example.campusview.service.ClassroomService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/classrooms")
class ClassroomController(
    private val classroomService: ClassroomService
) {
    @GetMapping
    fun getAllClassrooms(): ResponseEntity<List<ClassroomDto>> =
        ResponseEntity.ok(classroomService.getAllClassrooms())

    @GetMapping("/building/{type}")
    fun getClassroomsByBuilding(@PathVariable type: String): ResponseEntity<List<ClassroomDto>> =
        ResponseEntity.ok(classroomService.getClassroomsByBuilding(type))

    @GetMapping("/available")
    fun getAvailableClassrooms(): ResponseEntity<List<ClassroomDto>> =
        ResponseEntity.ok(classroomService.getAvailableClassrooms())

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createClassroom(@RequestBody request: CreateClassroomRequest): ResponseEntity<ClassroomDto> =
        ResponseEntity.ok(classroomService.createClassroom(request))

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateClassroom(
        @PathVariable id: Long,
        @RequestBody request: UpdateClassroomRequest
    ): ResponseEntity<ClassroomDto> =
        ResponseEntity.ok(classroomService.updateClassroom(id, request))

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteClassroom(@PathVariable id: Long): ResponseEntity<Unit> {
        classroomService.deleteClassroom(id)
        return ResponseEntity.ok().build()
    }
} 