package com.maxim.diaryforstudents.performance.analytics.data

import com.maxim.diaryforstudents.performance.analytics.domain.AnalyticsDomain

interface AnalyticsData {
    fun toDomain(): AnalyticsDomain

    data class Line(
        private val data: List<Float>,
        private val labels: List<String>
    ) : AnalyticsData {
        override fun toDomain() = AnalyticsDomain.Line(data, labels)
    }

    data class Pie(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ): AnalyticsData {
        override fun toDomain() = AnalyticsDomain.Pie(fiveCount, fourCount, threeCount, twoCount)
    }
}