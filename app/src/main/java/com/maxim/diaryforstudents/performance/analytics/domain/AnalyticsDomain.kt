package com.maxim.diaryforstudents.performance.analytics.domain

import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsUi

interface AnalyticsDomain {
    fun toUi(): AnalyticsUi
    fun message(): String

    class Line(
        private val data: List<Float>,
        private val labels: List<String>
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Line(data, labels)
        override fun message() = ""
    }

    class Pie(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ): AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Pie(fiveCount, fourCount, threeCount, twoCount)

        override fun message() = ""
    }

    class Error(
        private val message: String
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Error
        override fun message() = message
    }
}