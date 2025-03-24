package com.example.campusview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusview.model.BookingRecordDto
import com.example.campusview.network.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor() : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val _bookings = MutableLiveData<List<BookingRecordDto>>()
    val bookings: LiveData<List<BookingRecordDto>> = _bookings

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMore = MutableLiveData<Boolean>()
    val isLoadingMore: LiveData<Boolean> = _isLoadingMore

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 0
    private var isLastPage = false

    fun loadMyBookings(isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 0
            isLastPage = false
        }
        
        if (isLastPage) return

        viewModelScope.launch {
            try {
                if (isRefresh) {
                    _isLoading.value = true
                } else {
                    _isLoadingMore.value = true
                }

                val response = apiService.getMyBookings(page = currentPage)
                
                if (response.isSuccessful) {
                    val newBookings = response.body() ?: emptyList()
                    if (newBookings.isEmpty()) {
                        isLastPage = true
                    } else {
                        currentPage++
                        if (isRefresh) {
                            _bookings.value = newBookings
                        } else {
                            val currentList = _bookings.value.orEmpty()
                            _bookings.value = currentList + newBookings
                        }
                    }
                } else {
                    _error.value = "加载预约记录失败：${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "加载预约记录失败：${e.message}"
            } finally {
                if (isRefresh) {
                    _isLoading.value = false
                } else {
                    _isLoadingMore.value = false
                }
            }
        }
    }

    fun loadMoreBookings() {
        loadMyBookings(isRefresh = false)
    }

    fun refreshBookings() {
        loadMyBookings(isRefresh = true)
    }

    fun cancelBooking(bookingId: Long) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.cancelBooking(bookingId)
                
                if (response.isSuccessful) {
                    // 刷新预约列表
                    refreshBookings()
                } else {
                    _error.value = "取消预约失败：${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "取消预约失败：${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 