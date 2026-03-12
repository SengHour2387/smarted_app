package com.hourdex.smartedu.features.classes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ClassesVieModel @Inject constructor(
    private val classesService: ClassesService,
) : ViewModel() {

    private val _classes = MutableStateFlow<List<ClassesRes>>(emptyList())
    private val _reqMessage = MutableStateFlow<String?>(null)
    val classes: StateFlow<List<ClassesRes>> = _classes.asStateFlow()
    val reqMessage: StateFlow<String?> = _reqMessage.asStateFlow()


    init {
        getAllClasses()
    }

    fun refresh() {
        getAllClasses()
    }

    fun deleteClass(id: Long) {
        viewModelScope.launch {
            try {
                classesService.deleteClass(id)
                getAllClasses()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val message = try {
                    JSONObject(errorBody ?: "").getString("error")
                } catch (_: Exception) {
                    e.message()
                }
                _reqMessage.value = message
            } catch (e: Exception) {
                Log.d("Classes", "Error deleting class", e)
            }
            delay(3000)
            _reqMessage.value = null
        }
    }

    fun updateClass(classesReq: ClassesReq, id: Long) {
        viewModelScope.launch {
            try {
                classesService.updateClass(id, classesReq)
                getAllClasses()
            } catch (e: Exception) {
                Log.d("Classes", "Error updating class", e)
            }
        }
    }

    fun addClass( classesReq: ClassesReq ) {
        viewModelScope.launch {
            try {
                classesService.createClass(classesReq)
                getAllClasses()
            } catch (e: Exception) {
                Log.d("Classes", "Error adding class", e)
            }
        }
    }

    fun getAllClasses() {
        viewModelScope.launch {
            try {
                _classes.value = classesService.getClasses()
            } catch (e: Exception) {
                Log.d("Classes", "Error fetching classes", e)

            }
        }
    }

}