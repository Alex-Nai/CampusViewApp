package com.example.campusview.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class Resource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: String,
    val location: String?,
    val capacity: Int?,
    val description: String?,
    val status: String = "available"
) 