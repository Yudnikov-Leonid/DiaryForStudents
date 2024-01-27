package com.maxim.diaryforstudents.performance.data

data class PerformanceResponse(
    val success: Boolean,
    val message: String,
    val data: List<CloudLesson>
)

data class CloudLesson(
    val SUBJECT_NAME: String,
    val MARKS: List<CloudMark>
)

data class CloudMark(
    val DATE: String,
    val VALUE: Int
)