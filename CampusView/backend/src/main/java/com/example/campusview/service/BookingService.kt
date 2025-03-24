package com.example.campusview.service

import com.example.campusview.entity.BookingRecord
import com.example.campusview.model.BookingRecordDto
import com.example.campusview.model.PageResponse
import com.example.campusview.repository.BookingRepository
import com.example.campusview.repository.ResourceRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val resourceRepository: ResourceRepository
) {
    fun getBookingsByUserId(userId: Long, page: Int, size: Int): PageResponse<BookingRecordDto> {
        val pageable = PageRequest.of(page - 1, size)
        val bookingPage = bookingRepository.findByUserId(userId, pageable)
        
        return PageResponse(
            content = bookingPage.content.map { it.toDto() },
            totalElements = bookingPage.totalElements,
            totalPages = bookingPage.totalPages,
            pageNumber = bookingPage.number + 1,
            pageSize = bookingPage.size,
            hasNext = bookingPage.hasNext()
        )
    }

    @Transactional
    fun createBooking(userId: Long, resourceId: Long, startTime: LocalDateTime, endTime: LocalDateTime): BookingRecordDto {
        val resource = resourceRepository.findById(resourceId).orElseThrow {
            IllegalArgumentException("Resource not found")
        }

        // 检查资源是否可用
        if (resource.status != "available") {
            throw IllegalArgumentException("Resource is not available")
        }

        // 检查时间冲突
        // TODO: 添加时间冲突检查逻辑

        val booking = BookingRecord(
            resource = resource,
            userId = userId,
            startTime = startTime,
            endTime = endTime
        )

        return bookingRepository.save(booking).toDto()
    }

    @Transactional
    fun cancelBooking(userId: Long, bookingId: Long) {
        val booking = bookingRepository.findById(bookingId).orElseThrow {
            IllegalArgumentException("Booking not found")
        }

        if (booking.userId != userId) {
            throw IllegalArgumentException("Not authorized to cancel this booking")
        }

        if (booking.status != "pending" && booking.status != "approved") {
            throw IllegalArgumentException("Booking cannot be cancelled")
        }

        booking.status = "cancelled"
        bookingRepository.save(booking)
    }

    private fun BookingRecord.toDto(): BookingRecordDto {
        return BookingRecordDto(
            id = id,
            resourceId = resource.id,
            resourceName = resource.name,
            userId = userId,
            startTime = startTime,
            endTime = endTime,
            status = status,
            bookingTime = bookingTime,
            remarks = remarks
        )
    }
} 