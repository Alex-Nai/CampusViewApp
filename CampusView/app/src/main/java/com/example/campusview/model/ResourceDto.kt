package com.example.campusview.model

data class ResourceDto(
    val id: Long,
    val name: String,
    val type: String,
    val description: String?,
    val capacity: Int,
    val location: String,
    val isAvailable: Boolean,
    val currentStatus: String,
    val lastUpdated: String
) 