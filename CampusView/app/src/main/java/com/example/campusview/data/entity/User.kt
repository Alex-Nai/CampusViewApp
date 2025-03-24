package com.example.campusview.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val passwordHash: String,
    val email: String?,
    val createdAt: Date = Date()
) 