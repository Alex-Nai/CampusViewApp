package com.example.campusview.controller;

import com.example.campusview.model.ResourceDto;
import com.example.campusview.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceDto>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResourceDto>> getResourcesByType(@PathVariable String type) {
        return ResponseEntity.ok(resourceService.getResourcesByType(type));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ResourceDto>> getAvailableResources() {
        return ResponseEntity.ok(resourceService.getAvailableResources());
    }

    @GetMapping("/available/{type}")
    public ResponseEntity<List<ResourceDto>> getAvailableResourcesByType(@PathVariable String type) {
        return ResponseEntity.ok(resourceService.getAvailableResourcesByType(type));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceDto> createResource(@RequestBody ResourceDto resourceDto) {
        return ResponseEntity.ok(resourceService.createResource(resourceDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceDto> updateResource(@PathVariable Long id, @RequestBody ResourceDto resourceDto) {
        return ResponseEntity.ok(resourceService.updateResource(id, resourceDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok().build();
    }
} 