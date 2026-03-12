package com.hourdex.smartedu.features.edYears

import kotlinx.serialization.Serializable
import retrofit2.http.GET

@Serializable
data class EdYearsReq( val id: Long );

@Serializable
data class  EdYearsRes(val id: Long, val year_name: String,val start_date: String,val end_date: String, val is_current: Boolean )

interface EdYearsService {
    @GET("admin/academic-years")
    suspend fun getEdYears(): List<EdYearsRes>
}