package com.hourdex.smartedu.features.enroll

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hourdex.smartedu.features.classes.ClassesRes
import com.hourdex.smartedu.features.classes.ClassesService
import com.hourdex.smartedu.features.students.StudentRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


sealed class EnrollState {
    object Idle : EnrollState()
    object Loading : EnrollState()
    object Success : EnrollState()
    data class Error (val message: String) : EnrollState()
}


@HiltViewModel
class EnrollViewModel @Inject constructor(
    private val enrollService: EnrollService,
    private val classesService: ClassesService,
) : ViewModel() {
    private val _enrollState = MutableStateFlow<EnrollState>(EnrollState.Idle)
    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _unenrolledStudents = MutableStateFlow<List<StudentRes>>(emptyList())
    private val _availableClasses = MutableStateFlow<List<ClassesRes>>(emptyList())
    val availableClasses: StateFlow<List<ClassesRes>> = _availableClasses.asStateFlow()
    val unenrolledStudents: StateFlow<List<StudentRes>> = _unenrolledStudents
    val selectedIds: StateFlow<Set<Long>> = _selectedIds

    val enrollState: StateFlow<EnrollState> = _enrollState


    init {
        getUnEnrolledStudents()
    }


    fun refresh() {
        getUnEnrolledStudents()
        getAvailableClasses()
    }

    fun getUnEnrolledStudents() {
        viewModelScope.launch {
            try {
                _unenrolledStudents.value = enrollService.getUnEnrolledStudents()
            } catch (e: Exception) {
                Log.d("EnrollViewModel", "Error fetching unenrolled students", e)
            }
        }
    }

    fun getAvailableClasses() {
        viewModelScope.launch {
            try {
                _availableClasses.value = classesService.getAvailableClasses(selectedIds.value.size)
            } catch (e: Exception) {
                Log.d("EnrollViewModel", "Error fetching available classes", e)
            }
        }
    }

    fun addToSelection(id: Long) {
        _selectedIds.value = _selectedIds.value + id
    }

    fun removeFromSelection(id: Long) {
        _selectedIds.value = _selectedIds.value - id
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun enrollManyStudents(classId: Long) {
        viewModelScope.launch {
            try {
                _enrollState.value = EnrollState.Loading
                enrollService.enrollStudents(
                    EnrollReqs(
                        student_ids = _selectedIds.value.toList(),
                        class_id = classId
                    )
                )
                _enrollState.value = EnrollState.Success
            } catch (e: HttpException) {
                _enrollState.value = EnrollState.Error(e.message())
            } catch (e: Exception) {
                Log.d("EnrollViewModel", "Error enrolling students", e)
            }
            delay(10000)
            _enrollState.value = EnrollState.Idle
            _selectedIds.value = emptySet()
        }
    }

    fun enrollStudent(enrollReq: EnrollReq) {
        viewModelScope.launch {
            try {
                _enrollState.value = EnrollState.Loading
                enrollService.enrollStudent(enrollReq)
                _enrollState.value = EnrollState.Success
            } catch (e: Exception) {
                _enrollState.value = EnrollState.Error(e.message ?: "enroll not succeed")
            }
            delay(300)
            _enrollState.value = EnrollState.Idle
            _selectedIds.value = emptySet()
        }
    }

}