package com.maxim.diaryforstudents.performance.analytics.data

import com.maxim.diaryforstudents.performance.analytics.domain.AnalyticsDomain

interface AnalyticsData {
    fun toDomain(): AnalyticsDomain

    data class Base(
        private val data: List<Float>,
        private val labels: List<String>
    ) : AnalyticsData {
        override fun toDomain() = AnalyticsDomain.Base(data, labels)
    }
}