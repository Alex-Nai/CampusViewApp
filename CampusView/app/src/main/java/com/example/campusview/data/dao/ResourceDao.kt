package com.example.campusview.data.dao

import androidx.room.*
import com.example.campusview.data.entity.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {
    @Query("SELECT * FROM resources")
    fun getAllResources(): Flow<List<Resource>>

    @Query("SELECT * FROM resources WHERE type = :type")
    fun getResourcesByType(type: String): Flow<List<Resource>>

    @Query("SELECT * FROM resources WHERE status = 'available'")
    fun getAvailableResources(): Flow<List<Resource>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: Resource): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resources: List<Resource>)

    @Update
    suspend fun update(resource: Resource)

    @Delete
    suspend fun delete(resource: Resource)

    @Query("DELETE FROM resources")
    suspend fun deleteAll()
} 