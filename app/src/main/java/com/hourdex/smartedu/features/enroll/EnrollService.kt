package com.hourdex.smartedu.features.enroll

import com.hourdex.smartedu.features.students.StudentRes
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@Serializable
data class EnrollReq(val student_id: Long, val class_id: Long )

@Serializable
data class EnrollReqs(val student_ids: List<Long>, val class_id: Long )

interface EnrollService {
    @POST("admin/enroll-student")
    suspend fun enrollStudent( @Body req: EnrollReq)

    @POST("admin/enroll-student")
    suspend fun enrollStudents( @Body req: EnrollReqs)

    @GET("admin/unenrolled-students")
    suspend fun getUnEnrolledStudents(): List<StudentRes>


}