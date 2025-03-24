package com.example.campusview.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusview.model.ClassroomDto
import com.example.campusview.repository.ClassroomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
    private val repository: ClassroomRepository
) : ViewModel() {
    private val _classrooms = MutableLiveData<List<ClassroomDto>>()
    val classrooms: LiveData<List<ClassroomDto>> = _classrooms

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadAllClassrooms() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _classrooms.value = repository.getAllClassrooms()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadClassroomsByBuilding(buildingType: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _classrooms.value = repository.getClassroomsByBuilding(buildingType)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAvailableClassrooms() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _classrooms.value = repository.getAvailableClassrooms()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 