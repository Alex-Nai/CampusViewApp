package com.example.campusview.data.dao

import androidx.room.*
import com.example.campusview.data.entity.ScenicSpot
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenicSpotDao {
    @Query("SELECT * FROM scenic_spots")
    fun getAllSpots(): Flow<List<ScenicSpot>>

    @Query("SELECT * FROM scenic_spots WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): ScenicSpot?

    @Query("SELECT * FROM scenic_spots WHERE name LIKE '%' || :keyword || '%'")
    fun searchSpots(keyword: String): Flow<List<ScenicSpot>>

    @Insert
    suspend fun insert(scenicSpot: ScenicSpot): Long

    @Update
    suspend fun update(scenicSpot: ScenicSpot)

    @Delete
    suspend fun delete(scenicSpot: ScenicSpot)

    @Query("SELECT * FROM scenic_spots WHERE coordinates LIKE '%' || :coordinates || '%' LIMIT 1")
    suspend fun findByCoordinates(coordinates: String): ScenicSpot?
} 