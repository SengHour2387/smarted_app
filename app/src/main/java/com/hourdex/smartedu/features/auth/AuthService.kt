package com.hourdex.smartedu.features.auth

import com.hourdex.smartedu.core.User
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


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