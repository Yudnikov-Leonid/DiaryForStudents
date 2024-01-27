package com.maxim.diaryforstudents.performance.data

data class PerformanceFinalResponse(
    val success: Boolean,
    val message: String,
    val data: List<EduPerformanceFinalLesson>
)

data class EduPerformanceFinalLesson(
    val NAME: String,
    val PERIODS: List<EduPerformanceFinalPeriod>
)

data class EduPerformanceFinalPeriod(
    val GRADE_TYPE_GUID: String,
    val MARK: EduPerformanceFinalMark?,
    val AVERAGE: Float
)

data class EduPerformanceFinalMark(
    val VALUE: Int
)