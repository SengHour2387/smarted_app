package com.hourdex.smartedu.features.subjects

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class SubjectsReq(val name:String)

@Serializable
data class  SubjectsRes(val id: Long, val name: String, val code: String)

interface SubjectsService {
    //get all
    @GET("admin/subjects")
    suspend fun getSubjects(): List<SubjectsRes>

    //create
    @POST("admin/subjects")
    suspend fun createSubject(@Body req: SubjectsReq): SubjectsRes

    //get one
    @GET("admin/subjects/{id}")
    suspend fun getSubjectById(id: Long): SubjectsRes

    //update
    @PATCH("admin/subjects/{id}")
    suspend fun updateSubject(@Body req: SubjectsReq, @Path("id") id: Long): SubjectsRes

    //delete
    @DELETE("admin/subjects/{id}")
    suspend fun deleteSubject(@Path("id") id: Long)

}