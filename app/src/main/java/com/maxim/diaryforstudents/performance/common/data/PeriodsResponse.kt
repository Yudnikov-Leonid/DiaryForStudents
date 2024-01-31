package com.maxim.diaryforstudents.performance.common.data

data class PeriodsResponse(
    val success: Boolean,
    val message: String,
    val data: List<PerformancePeriod>
)

data class PerformancePeriod(
    val DATE_BEGIN: String,
    val DATE_END: String,
)