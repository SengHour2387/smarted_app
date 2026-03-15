package com.hourdex.smartedu.features.teacherSubjectClass

import com.hourdex.smartedu.features.classes.ClassesRes
import com.hourdex.smartedu.features.subjects.SubjectsRes
import com.hourdex.smartedu.features.teachers.TeacherRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@Serializable
data class TeacherAssignReq( val teacher_id: Long, val class_id: Long, val subject_id: Long)

@Serializable
data class TeacherAssignRes(
    val id: Long? = null,
    val teacher: TeacherSummary? = null,
    @SerialName("class") val classInfo: ClassSummary? = null,
    val subject: SubjectSummary? = null
)

@Serializable
data class TeacherSummary(val id: Long, val full_name: String)

@Serializable
data class ClassSummary(val id: Long, val name: String)

@Serializable
data class SubjectSummary(val id: Long, val name: String)


interface TeacherAssignService {
    @POST("admin/teacher-assignments")
    suspend fun assignTeacher( @Body teacherAssignReq: TeacherAssignReq): TeacherAssignRes
    @GET("admin/teacher-assignments")
    suspend fun getTeacherAssignments(): List<TeacherAssignRes>
}