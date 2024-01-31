package com.maxim.diaryforstudents.performance.common.data

import java.io.Serializable

data class PerformanceFinalResponse(
    val success: Boolean,
    val message: String,
    val data: List<PerformanceFinalLesson>
)

data class PerformanceFinalLesson(
    val NAME: String,
    val PERIODS: List<PerformanceFinalPeriod>
): Serializable

data class PerformanceFinalPeriod(
    val GRADE_TYPE_GUID: String,
    val MARK: PerformanceFinalMark?,
    val AVERAGE: Float
): Serializable

data class PerformanceFinalMark(
    val VALUE: Int
): Serializable