package com.hourdex.smartedu.features.classes

import com.hourdex.smartedu.features.edYears.EdYearsRes
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class ClassesReq( val name: String, val grade_level:Long, val academic_year_id:Long, val capacity:Long)

@Serializable
data class ClassesRes(val id: Long, val name: String, val grade_level:Long, val academic_years: EdYearsRes, val capacity:Long)

interface ClassesService {
    @GET("admin/classes")
    suspend fun getClasses(): List<ClassesRes>
    @POST("admin/classes")
    suspend fun createClass(@Body req: ClassesReq): ClassesRes

    @DELETE("admin/classes/{id}")
    suspend fun deleteClass(@Path("id") id: Long)

    @PATCH("admin/classes/{id}")
    suspend fun updateClass(@Path("id") id: Long, @Body req: ClassesReq)

    @GET("admin/classes/available")
    suspend fun getAvailableClasses(@Query("amount") amount: Int ): List<ClassesRes>
}