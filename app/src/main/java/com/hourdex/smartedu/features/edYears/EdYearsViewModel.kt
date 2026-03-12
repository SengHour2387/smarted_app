package com.hourdex.smartedu.features.edYears

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EdYearsViewModel @Inject constructor(
    private val edYearsService: EdYearsService
) : ViewModel() {

    private val _edYears = MutableStateFlow<List<EdYearsRes>>(emptyList())
    private val _lastEdYears = MutableStateFlow<EdYearsRes?>(null)
    val lastEdYears: StateFlow<EdYearsRes?> = _lastEdYears.asStateFlow()
    val edYears: StateFlow<List<EdYearsRes>> = _edYears.asStateFlow()


    init {
        getLastEdYears()
        getAllEdYears()
    }

    fun getLastEdYears() {
        _lastEdYears.value = _edYears.value.lastOrNull()
    }

    fun getAllEdYears() {
        viewModelScope.launch {
            try {
                val edYears = edYearsService.getEdYears()
                _edYears.value = edYears
            } catch (e: Exception) {
                Log.d("EdYears", "Error fetching edYears", e)
            }
        }
    }

}