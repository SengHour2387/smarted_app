package com.hourdex.smartedu.features.auth

import com.hourdex.smartedu.core.User
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


@Serializable
data class User(
    val id: Long,
    val full_name: String,
    val email: String,
    val phone: String? = null,
    val role: String,
    val avatar_url: String? = null,
    val is_active: Boolean
)

@Serializable
data class UserRes(
    val email: String,
    val phone: String? = null,
    val full_name: String,
)

interface UserService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

@Serializable
data class LoginReq(val email: String, val role: String, val password: String)

@Serializable
data class LoginRes(val message: String, val token: String, val user: User)

@Serializable
data class MeRes(val user: User)

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body req: LoginReq): LoginRes

    @POST("auth/token-login")
    suspend fun tokenLogin(@Header("Authorization")token: String): LoginRes

    @POST("auth/me")
    suspend fun tokenGetMe(@Header("Authorization")token: String): MeRes
}