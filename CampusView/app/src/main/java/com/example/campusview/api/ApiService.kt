package com.example.campusview.api

import com.example.campusview.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // 用户认证相关
    @POST("api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    // 教室相关
    @GET("api/classrooms")
    suspend fun getAllClassrooms(): Response<List<ClassroomDto>>

    @GET("api/classrooms/building/{buildingType}")
    suspend fun getClassroomsByBuilding(@Path("buildingType") buildingType: String): Response<List<ClassroomDto>>

    @GET("api/classrooms/available")
    suspend fun getAvailableClassrooms(): Response<List<ClassroomDto>>

    // 资源相关
    @GET("api/resources")
    suspend fun getAllResources(): Response<List<ResourceDto>>

    @GET("api/resources/type/{type}")
    suspend fun getResourcesByType(@Path("type") type: String): Response<List<ResourceDto>>

    @GET("api/resources/available")
    suspend fun getAvailableResources(): Response<List<ResourceDto>>

    @GET("api/resources/available/timeslot")
    suspend fun getAvailableResourcesForTimeSlot(
        @Query("startTime") startTime: String,
        @Query("endTime") endTime: String
    ): Response<List<ResourceDto>>

    // 预约记录相关
    @GET("api/bookings/my")
    suspend fun getMyBookings(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 20
    ): Response<List<BookingRecordDto>>

    @POST("api/bookings")
    suspend fun createBooking(@Body bookingRequest: CreateBookingRequest): Response<BookingRecordDto>

    @DELETE("api/bookings/{id}")
    suspend fun cancelBooking(@Path("id") id: Long): Response<Unit>

    // 景点相关
    @GET("api/spots")
    suspend fun getAllSpots(): Response<List<ScenicSpotDto>>

    @GET("api/spots/search")
    suspend fun searchSpots(@Query("keyword") keyword: String): Response<List<ScenicSpotDto>>

    @POST("api/spots/recognize")
    suspend fun recognizeImage(@Body request: ImageRecognitionRequest): Response<ImageRecognitionResponse>

    // 用户预约记录
    @GET("api/bookings/user/{userId}")
    suspend fun getUserBookings(@Path("userId") userId: Long): Response<List<BookingRecordDto>>
} 