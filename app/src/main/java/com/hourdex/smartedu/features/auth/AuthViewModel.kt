package com.hourdex.smartedu.features.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hourdex.smartedu.core.TokenManager
import com.hourdex.smartedu.core.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class LoginResult {
    object Idle : LoginResult()
    object Loading : LoginResult()
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginState: StateFlow<LoginResult> = _loginState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        // Auto login automatically when ViewModel is created
        autoLogin()
    }

    private fun autoLogin() {  // Make it private
        viewModelScope.launch {
            Log.d("Login", "Auto login started")
            _loginState.value = LoginResult.Loading

            try {
                val token = tokenManager.tokenFlow.first()

                if (token.isNullOrEmpty()) {
                    Log.d("Login", "No token found")
                    _loginState.value = LoginResult.Idle
                    return@launch
                }

                val response = authService.tokenLogin("Bearer $token")

                if (response.token.isNotEmpty()) {
                    tokenManager.saveToken(response.token)
                    _user.value = response.user
                    Log.d("Login", "Auto login successful")
                    _loginState.value = LoginResult.Success
                } else {
                    Log.d("Login", "Auto login failed - empty token")
                    _loginState.value = LoginResult.Idle
                }

            } catch (e: HttpException) {
                Log.e("Login", "Auto login failed: ${e.code()}")
                if (e.code() == 401) {
                    tokenManager.clearToken()
                }
                _loginState.value = LoginResult.Idle
            } catch (e: Exception) {
                Log.e("Login", "Auto login error", e)
                _loginState.value = LoginResult.Idle
            }
        }
    }
    // Public login function
    fun login(email: String, role: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginResult.Loading

            try {
                val response = authService.login(LoginReq(email, role, password))
                tokenManager.saveToken(response.token)
                _user.value = response.user
                _loginState.value = LoginResult.Success
                Log.d("Login", "Login successful")
            } catch (e:Exception) {
                Log.e("Login", "Login error", e)
                _loginState.value = LoginResult.Error(e.message ?: "Unknown error")
                //show a sneak bar
                val errorMessage = when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            401 -> "Invalid email or password"
                            404 -> "User not found"
                            else -> "Unknown error"
                        }
                    }
                    else -> "Unknown error"
                }
                _loginState.value = LoginResult.Error(errorMessage)
            }
        }
    }
}