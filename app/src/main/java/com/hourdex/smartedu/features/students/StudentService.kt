package com.hourdex.smartedu.features.students

import com.hourdex.smartedu.core.UserRes
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class StudentReq(
    val email: String,
    val password: String,
    val full_name: String,
    val phone: String,
)

@Serializable
data class StudentUpdateReq(
    // users table
    val full_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val avatar_url: String? = null,

    // students table
    val date_of_birth: String? = null,
    val gender: String? = null,
    val address: String? = null,
    val medical_history: String? = null,
    val allergies: String? = null,
    val emergency_contact_name: String? = null,
    val emergency_contact_phone: String? = null,
    val photo_url: String? = null,
)

@Serializable
data class StudentRes(
    val id: Long,
    val user_id: Long,
    val student_code: String,
    val full_name: String,
    val date_of_birth: String? = null,
    val gender: String? = null,
    val address: String? = null,
    val medical_history: String? = null,
    val allergies: String? = null,
    val emergency_contact_name: String? = null,
    val emergency_contact_phone: String? = null,
    val photo_url: String? = null,
    val enrollment_date: String,
    val status: String,
    val users: UserRes
)

interface StudentService {
    @GET("admin/students")
    suspend fun getStudents(): List<StudentRes>

    @POST("admin/students")
    suspend fun addStudent(@Body student: StudentReq): StudentRes

    @GET("admin/students/{id}")
    suspend fun getStudentById(@Path("id" ) id:Long): StudentRes

    @PATCH("admin/students/{id}")
    suspend fun updateStudent(@Body student: StudentUpdateReq, @Path("id") id: Long)
}