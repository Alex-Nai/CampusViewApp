package com.example.campusview.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "booking_records",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId")
    ]
)
data class BookingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val resourceId: Long,
    val userId: Long,
    val bookingTime: Date = Date(),
    val startTime: Date?,
    val endTime: Date?,
    val status: String = "pending" // pending, approved, rejected, completed
) 