package com.example.campusview.data.dao

import androidx.room.*
import com.example.campusview.data.entity.ResourceBooking
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ResourceBookingDao {
    @Query("SELECT * FROM resource_booking WHERE resource_type = :type")
    fun getResourcesByType(type: String): Flow<List<ResourceBooking>>

    @Query("SELECT * FROM resource_booking WHERE is_available = 1")
    fun getAvailableResources(): Flow<List<ResourceBooking>>

    @Query("""
        SELECT * FROM resource_booking 
        WHERE is_available = 1 
        AND (:startTime IS NULL OR end_time <= :startTime)
        AND (:endTime IS NULL OR start_time >= :endTime)
    """)
    fun getAvailableResourcesForTimeSlot(startTime: Date?, endTime: Date?): Flow<List<ResourceBooking>>

    @Insert
    suspend fun insert(resource: ResourceBooking): Long

    @Update
    suspend fun update(resource: ResourceBooking)

    @Delete
    suspend fun delete(resource: ResourceBooking)

    @Query("UPDATE resource_booking SET is_available = :isAvailable WHERE id = :id")
    suspend fun updateAvailability(id: Long, isAvailable: Boolean)
} 