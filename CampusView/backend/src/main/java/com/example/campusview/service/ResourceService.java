package com.example.campusview.service;

import com.example.campusview.entity.Resource;
import com.example.campusview.model.ResourceDto;
import com.example.campusview.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<ResourceDto> getResourcesByType(String type) {
        return resourceRepository.findByType(type).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<ResourceDto> getAvailableResources() {
        return resourceRepository.findByStatus("available").stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<ResourceDto> getAvailableResourcesByType(String type) {
        return resourceRepository.findByTypeAndStatus(type, "available").stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public ResourceDto createResource(ResourceDto resourceDto) {
        Resource resource = new Resource();
        resource.setName(resourceDto.getName());
        resource.setType(resourceDto.getType());
        resource.setLocation(resourceDto.getLocation());
        resource.setCapacity(resourceDto.getCapacity());
        resource.setDescription(resourceDto.getDescription());
        resource.setStatus("available");
        
        return toDto(resourceRepository.save(resource));
    }

    @Transactional
    public ResourceDto updateResource(Long id, ResourceDto resourceDto) {
        Resource resource = resourceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Resource not found"));

        resource.setName(resourceDto.getName());
        resource.setType(resourceDto.getType());
        resource.setLocation(resourceDto.getLocation());
        resource.setCapacity(resourceDto.getCapacity());
        resource.setDescription(resourceDto.getDescription());
        resource.setStatus(resourceDto.getStatus());

        return toDto(resourceRepository.save(resource));
    }

    @Transactional
    public void deleteResource(Long id) {
        Resource resource = resourceRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
            
        resourceRepository.delete(resource);
    }

    private ResourceDto toDto(Resource resource) {
        return new ResourceDto(
            resource.getId(),
            resource.getName(),
            resource.getType(),
            resource.getLocation(),
            resource.getCapacity(),
            resource.getDescription(),
            resource.getStatus()
        );
    }
} 