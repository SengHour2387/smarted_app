package com.hourdex.smartedu.features.teachers

import com.hourdex.smartedu.core.UserRes
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


@Serializable
data class TeacherReq( val email: String, val password: String, val full_name: String, val phone: String,val department: String )

@Serializable
data class TeacherRes(
    val id: Int,
    val user_id: Int,
    val employee_code: String,
    val full_name: String,
    val department: String,
    val joining_date: String,        // keep as String, or change to LocalDate if you parse dates
    val is_class_teacher: Boolean,
    val created_at: String,          // keep as String, or change to LocalDateTime if you parse timestamps
    val users: UserRes               // per your note: reuse UserRes here
)

interface TeacherService {

    @GET("admin/teachers")
    suspend fun getTeachers(): List<TeacherRes>

    @POST("admin/teachers")
    suspend fun addTeacher(@Body teacher: TeacherReq): TeacherRes

    @GET("admin/teachers/{id}")
    suspend fun getTeacherById(@Path("id" ) id:Long): TeacherRes


    @PATCH("admin/teachers/{id}")
    suspend fun updateTeacher(@Body teacher: TeacherReq, @Path("id") id: Long)

    @DELETE("admin/teachers/{id}")
    suspend fun deleteTeacherById(@Path("id" ) id:Long)
}