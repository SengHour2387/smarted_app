package com.hourdex.smartedu.features.teachers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject



sealed class AddTeacherState {
    object Idle : AddTeacherState()
    object Loading : AddTeacherState()
    data class Success(val teacher: TeacherRes) : AddTeacherState()
    data class Error(val message: String) : AddTeacherState()
}

class TeacherViewModel @Inject constructor(
    private val teacherService: TeacherService
) : ViewModel() {

    private val _teachers = MutableStateFlow<List<TeacherRes>>(emptyList())
    val teachers: StateFlow<List<TeacherRes>> = _teachers

    private val _addTeacherState = MutableStateFlow<AddTeacherState>(AddTeacherState.Idle)
    val addTeacherState: StateFlow<AddTeacherState> = _addTeacherState

    init {
        getTeachers()
    }

    fun refresh() {
        getTeachers()
    }

    fun getTeachers() {
        viewModelScope.launch {
            try {
                _teachers.value = teacherService.getTeachers()
            } catch (e: HttpException) {
                Log.d("TeacherViewModel", "Error fetching teachers", e)
            } catch (e: Exception) {
                Log.d("TeacherViewModel", "Error fetching teachers", e)
            }

        }
    }


    fun createTeacher(teacher: TeacherReq) {
        viewModelScope.launch {
            try {
                _addTeacherState.value = AddTeacherState.Loading
                val createdTeacher = teacherService.addTeacher(teacher)
                _addTeacherState.value = AddTeacherState.Success(createdTeacher)
            } catch (e: HttpException) {
                Log.d("TeacherViewModel", "Error creating teacher", e)
                _addTeacherState.value = AddTeacherState.Error("Error")
            } catch (e: Exception) {
                Log.d("TeacherViewModel", "Error creating teacher", e)
            }
        }
    }

    fun deleteTeacher(id: Long) {
        viewModelScope.launch {
            try {
                teacherService.deleteTeacherById(id)
                getTeachers()
            } catch (e: HttpException) {
                Log.d("TeacherViewModel", "Error deleting teacher", e)
            } catch (e: Exception) {
                Log.d("TeacherViewModel", "Error deleting teacher", e)
            }
        }
    }

}