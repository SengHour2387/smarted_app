package com.hourdex.smartedu.features.teacherSubjectClass

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class AssignStep {
    object SelectTeacher : AssignStep()
    data class SelectSubject(val teacherId: Long) : AssignStep()
    data class SelectClass(val teacherId: Long, val subjectId: Long) : AssignStep()
    object Complete : AssignStep()
}

data class AssignTeacherUiState(
    val step: AssignStep = AssignStep.SelectTeacher,
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TeacherAssignViewModel @Inject constructor(
    private val teacherAssignService: TeacherAssignService
) : ViewModel() {

    private val _uiState = MutableStateFlow(AssignTeacherUiState())
    val uiState: StateFlow<AssignTeacherUiState> = _uiState.asStateFlow()

    private val _teacherAssignments = MutableStateFlow<List<TeacherAssignRes>>(emptyList())
    val teacherAssignments: StateFlow<List<TeacherAssignRes>> = _teacherAssignments.asStateFlow()

    init {
        getTeacherAssignments()
    }

    fun getTeacherAssignments() {
        viewModelScope.launch {
            try {
                _teacherAssignments.value = teacherAssignService.getTeacherAssignments()
            } catch (e: Exception) {
                Log.e("TeacherAssignVM", "Fetch error", e)
            }
        }
    }

    fun selectTeacher(id: Long) {
        _uiState.update { it.copy(step = AssignStep.SelectSubject(id)) }
    }

    fun selectSubject(id: Long) {
        val current = _uiState.value.step as? AssignStep.SelectSubject ?: return
        _uiState.update { it.copy(step = AssignStep.SelectClass(current.teacherId, id)) }
    }

    fun selectClass(id: Long) {
        val current = _uiState.value.step as? AssignStep.SelectClass ?: return
        assignTeacher(current.teacherId, current.subjectId, id)
    }

    fun reset() {
        _uiState.update { AssignTeacherUiState() }
    }

    private fun assignTeacher(teacherId: Long, subjectId: Long, classId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                teacherAssignService.assignTeacher(
                    TeacherAssignReq(
                        teacher_id = teacherId,
                        class_id = classId,
                        subject_id = subjectId,
                    )
                )
                getTeacherAssignments()
                _uiState.update { it.copy(loading = false, step = AssignStep.Complete) }
            } catch (e: HttpException) {
                Log.e("TeacherAssignVM", "HTTP error: ${e.code()} ${e.message()}", e)
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = when (e.code()) {
                            409 -> "Already assigned to this class and subject"
                            400 -> "Invalid assignment data"
                            else -> "Assignment failed (${e.code()})"
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("TeacherAssignVM", "Assignment error", e)
                _uiState.update { it.copy(loading = false, error = "Assignment failed") }
            }
        }
    }
}