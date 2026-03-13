package com.hourdex.smartedu.features.teacherSubjectClass

import kotlinx.serialization.Serializable

@Serializable
data class TeacherAssignReq( val teacher_id: Long, val class_id: Long, val subject_id: Long )

@Serializable
data class TeacherAssignRes( val teacher_id: Long, val class_id: Long, val subject_id: Long )