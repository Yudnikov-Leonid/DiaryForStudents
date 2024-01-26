package com.maxim.diaryforstudents.performance.eduData

data class EduPerformanceResponse(
    val success: Boolean,
    val message: String,
    val data: List<EduCloudLesson>
)

data class EduCloudLesson(
    val SUBJECT_NAME: String,
    val MARKS: List<EduCloudMark>
)

data class EduCloudMark(
    val DATE: String,
    val VALUE: Int
)