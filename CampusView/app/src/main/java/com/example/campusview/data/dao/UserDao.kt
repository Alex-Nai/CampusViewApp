package com.example.campusview.data.dao

import androidx.room.*
import com.example.campusview.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): User?

    @Insert
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    suspend fun exists(username: String): Boolean
} 