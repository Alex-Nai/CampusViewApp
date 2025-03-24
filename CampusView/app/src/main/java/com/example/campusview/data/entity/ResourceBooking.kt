package com.example.campusview.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "resource_booking")
data class ResourceBooking(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val resourceType: String, // '讲座' 或 '实验室'
    val resourceName: String,
    val location: String?,
    val capacity: Int?,
    val startTime: Date?,
    val endTime: Date?,
    val isAvailable: Boolean = true,
    val description: String?
) 