package com.maxim.diaryforstudents.performance.common.data

import java.io.Serializable

data class PerformanceResponse(
    val success: Boolean,
    val message: String,
    val data: List<CloudLesson>
)

data class CloudLesson(
    val SUBJECT_NAME: String,
    val SUBJECT_SYS_GUID: String,
    val MARKS: List<CloudMark>
): Serializable

data class CloudMark(
    val DATE: String,
    val VALUE: Int
): Serializable