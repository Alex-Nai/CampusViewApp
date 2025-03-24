package com.example.campusview.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class BookingRecordDto {
    private final Long id;
    private final Long resourceId;
    private final String resourceName;
    private final Long userId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String status;
    private final LocalDateTime bookingTime;
    private final String remarks;

    public BookingRecordDto(Long id, Long resourceId, String resourceName, Long userId,
                          LocalDateTime startTime, LocalDateTime endTime, String status,
                          LocalDateTime bookingTime, String remarks) {
        this.id = id;
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.bookingTime = bookingTime != null ? bookingTime : LocalDateTime.now();
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public String getRemarks() {
        return remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingRecordDto that = (BookingRecordDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(resourceId, that.resourceId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resourceId, userId, startTime, endTime, status);
    }
} 