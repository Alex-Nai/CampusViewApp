package com.example.campusview.data.dao

import androidx.room.*
import com.example.campusview.data.entity.BookingRecord
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BookingRecordDao {
    @Query("SELECT * FROM booking_records WHERE user_id = :userId")
    fun getUserBookings(userId: Long): Flow<List<BookingRecord>>

    @Query("SELECT * FROM booking_records WHERE resource_id = :resourceId")
    fun getResourceBookings(resourceId: Long): Flow<List<BookingRecord>>

    @Query("""
        SELECT * FROM booking_records 
        WHERE resource_id = :resourceId 
        AND status != 'rejected'
        AND (:startTime IS NULL OR end_time > :startTime)
        AND (:endTime IS NULL OR start_time < :endTime)
    """)
    suspend fun getConflictingBookings(resourceId: Long, startTime: Date?, endTime: Date?): List<BookingRecord>

    @Insert
    suspend fun insert(bookingRecord: BookingRecord): Long

    @Update
    suspend fun update(bookingRecord: BookingRecord)

    @Delete
    suspend fun delete(bookingRecord: BookingRecord)

    @Query("UPDATE booking_records SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)
} 