package com.example.campusview.repository

import com.example.campusview.api.ApiService
import com.example.campusview.model.ClassroomDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllClassrooms(): List<ClassroomDto> {
        val response = apiService.getAllClassrooms()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("获取教室列表失败: ${response.message()}")
    }

    suspend fun getClassroomsByBuilding(buildingType: String): List<ClassroomDto> {
        val response = apiService.getClassroomsByBuilding(buildingType)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("获取建筑教室列表失败: ${response.message()}")
    }

    suspend fun getAvailableClassrooms(): List<ClassroomDto> {
        val response = apiService.getAvailableClassrooms()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("获取可用教室列表失败: ${response.message()}")
    }
} 