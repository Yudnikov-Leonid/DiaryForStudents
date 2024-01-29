package com.maxim.diaryforstudents.performance.common.data

data class PerformanceFinalResponse(
    val success: Boolean,
    val message: String,
    val data: List<PerformanceFinalLesson>
)

data class PerformanceFinalLesson(
    val NAME: String,
    val PERIODS: List<PerformanceFinalPeriod>
)

data class PerformanceFinalPeriod(
    val GRADE_TYPE_GUID: String,
    val MARK: PerformanceFinalMark?,
    val AVERAGE: Float
)

data class PerformanceFinalMark(
    val VALUE: Int
)