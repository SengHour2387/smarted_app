package com.hourdex.smartedu.features.subjects

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotId
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hourdex.smartedu.core.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val subjectsService: SubjectsService,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _subjects = MutableStateFlow<List<SubjectsRes>>(emptyList())
    val subjects: StateFlow<List<SubjectsRes>> = _subjects.asStateFlow()

    fun getSubjects() {
        viewModelScope.launch {
           try {
               val subjects = subjectsService.getSubjects()
               _subjects.value = subjects
               Log.d("Subjects", "Subjects: $subjects")
           } catch (e: Exception) {
               Log.e("Subjects", "Error fetching subjects", e)
           }
        }
    }

    fun updateSubject(id: SnapshotId,newName: String) {
        viewModelScope.launch {
            try {
                subjectsService.updateSubject(SubjectsReq(newName),id)
            } catch (e: Exception) {
                Log.e("Subjects", "Error updating subject", e)
            }
            getSubjects()
        }
    }

    fun addSubject(newName: String) {
        viewModelScope.launch {
            try {
                subjectsService.createSubject(SubjectsReq(newName))
            } catch (e: Exception) {
                Log.e("Subjects", "Error adding subject", e)
            }
            getSubjects()
        }
    }

    fun deleteSubject(longValue: Long) {
        viewModelScope.launch {
            try {
                subjectsService.deleteSubject(longValue)
            } catch (e: Exception) {
                Log.e("Subjects", "Error deleting subject", e)
            }
            getSubjects()
        }

    }
}