package com.hourdex.smartedu.core

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
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
