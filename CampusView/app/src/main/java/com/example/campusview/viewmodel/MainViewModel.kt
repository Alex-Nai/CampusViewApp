package com.example.campusview.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusview.model.*
import com.example.campusview.repository.CampusRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = CampusRepository()

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _classrooms = MutableLiveData<Result<List<ClassroomDto>>>()
    val classrooms: LiveData<Result<List<ClassroomDto>>> = _classrooms

    private val _spots = MutableLiveData<Result<List<ScenicSpotDto>>>()
    val spots: LiveData<Result<List<ScenicSpotDto>>> = _spots

    private val _bookings = MutableLiveData<Result<List<BookingRecordDto>>>()
    val bookings: LiveData<Result<List<BookingRecordDto>>> = _bookings

    private val _recognitionResult = MutableLiveData<Result<ImageRecognitionResponse>>()
    val recognitionResult: LiveData<Result<ImageRecognitionResponse>> = _recognitionResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = repository.login(username, password)
        }
    }

    fun getAllClassrooms() {
        viewModelScope.launch {
            _classrooms.value = repository.getAllClassrooms()
        }
    }

    fun getClassroomsByBuilding(buildingType: String) {
        viewModelScope.launch {
            _classrooms.value = repository.getClassroomsByBuilding(buildingType)
        }
    }

    fun getAvailableClassrooms() {
        viewModelScope.launch {
            _classrooms.value = repository.getAvailableClassrooms()
        }
    }

    fun getAllSpots() {
        viewModelScope.launch {
            _spots.value = repository.getAllSpots()
        }
    }

    fun searchSpots(keyword: String) {
        viewModelScope.launch {
            _spots.value = repository.searchSpots(keyword)
        }
    }

    fun recognizeImage(imageBase64: String) {
        viewModelScope.launch {
            _recognitionResult.value = repository.recognizeImage(imageBase64)
        }
    }

    fun getUserBookings(userId: Long) {
        viewModelScope.launch {
            _bookings.value = repository.getUserBookings(userId)
        }
    }

    fun createBooking(request: CreateBookingRequest) {
        viewModelScope.launch {
            val result = repository.createBooking(request)
            // 刷新预约列表
            result.onSuccess { 
                getUserBookings(it.userId)
            }
        }
    }
} 