package com.hourdex.smartedu.features.students

import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


//add student state
sealed class AddStudentSate {
    object Idle : AddStudentSate()
    object Loading : AddStudentSate()
    data class Success(val student: StudentRes) : AddStudentSate()
    data class Error(val message: String) : AddStudentSate()
}

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val studentService: StudentService
) : ViewModel()   {

    private val _students = MutableStateFlow<List<StudentRes>>(emptyList())
    val students: StateFlow<List<StudentRes>> = _students
    private val _addStudentState = MutableStateFlow<AddStudentSate>(AddStudentSate.Idle)
    val addStudentState: StateFlow<AddStudentSate> = _addStudentState


    init {
        getStudents()
    }

    fun getStudents() {
        viewModelScope.launch {
            try {
                _students.value = studentService.getStudents()
                Log.d("StudentsViewModel", "Fetched ${_students.value.size} students")
            } catch (e: Exception) {
                Log.e("StudentsViewModel", "Error fetching students", e)
            }
        }
    }

    fun deleteStudent(id: Long) {
        viewModelScope.launch {
            try {
                studentService.deleteStudentById(id)
                getStudents()
            } catch (e: retrofit2.HttpException) {
                Log.e("StudentsViewModel", "Error deleting cuz: ${e.message()}", e)
            }

            catch (e: Exception) {
                Log.e("StudentsViewModel", "Error deleting student", e)
            }
        }
    }

    fun getStudentById( id: Long ) : StudentRes? {
            _students.value.find { it.id == id }?.let {
                return it
            }
        return null
    }

    fun createStudent(student: StudentReq) {
        viewModelScope.launch {
            try {
                _addStudentState.value = AddStudentSate.Loading
                val createdStudent = studentService.addStudent(student)
                _addStudentState.value = AddStudentSate.Success(createdStudent)
            } catch (e: Exception) {
                Log.e("StudentsViewModel", "Error creating student", e)
                _addStudentState.value = AddStudentSate.Error("Error")
            }
            getStudents()
            delay(3000)
            _addStudentState.value = AddStudentSate.Idle
        }
    }

    fun updateStudent(student: StudentUpdateReq,id: Long) {
        viewModelScope.launch {
            try {
                studentService.updateStudent(student, id)
                getStudents()
            } catch (e: Exception) {
                Log.e("StudentsViewModel", "Error updating student", e)
            }
        }
    }

}