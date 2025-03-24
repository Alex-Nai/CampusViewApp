package com.example.campusview.repository

import com.example.campusview.api.ApiService
import com.example.campusview.model.BookingRecordDto
import com.example.campusview.model.CreateBookingRequest
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getMyBookings(page: Int, pageSize: Int = 20): List<BookingRecordDto> {
        val response = apiService.getMyBookings(page, pageSize)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("获取预约记录失败: ${response.message()}")
    }

    suspend fun cancelBooking(bookingId: Long) {
        val response = apiService.cancelBooking(bookingId)
        if (!response.isSuccessful) {
            throw Exception("取消预约失败: ${response.message()}")
        }
    }

    suspend fun createBooking(
        resourceId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): BookingRecordDto {
        val request = CreateBookingRequest(resourceId, startTime, endTime)
        val response = apiService.createBooking(request)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("创建预约失败: ${response.message()}")
    }
} 