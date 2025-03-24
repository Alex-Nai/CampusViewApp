package com.example.campusview.repository

import com.example.campusview.api.ApiService
import com.example.campusview.model.*
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CampusRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("登录失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String, email: String): Result<RegisterResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(RegisterRequest(username, password, email))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("注册失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllClassrooms(): Result<List<ClassroomDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllClassrooms()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取教室列表失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getClassroomsByBuilding(buildingType: String): Result<List<ClassroomDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getClassroomsByBuilding(buildingType)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取教室列表失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAvailableClassrooms(): Result<List<ClassroomDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAvailableClassrooms()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取可用教室失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllSpots(): Result<List<ScenicSpotDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllSpots()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取景点列表失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchSpots(keyword: String): Result<List<ScenicSpotDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.searchSpots(keyword)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("搜索景点失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun recognizeImage(imageBase64: String): Result<ImageRecognitionResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.recognizeImage(ImageRecognitionRequest(imageBase64))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("图像识别失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserBookings(userId: Long): Result<List<BookingRecordDto>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserBookings(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("获取预约记录失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createBooking(request: CreateBookingRequest): Result<BookingRecordDto> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createBooking(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("创建预约失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 