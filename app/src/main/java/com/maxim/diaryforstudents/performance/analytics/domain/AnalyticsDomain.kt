package com.maxim.diaryforstudents.performance.analytics.domain

import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsUi

interface AnalyticsDomain {
    fun toUi(): AnalyticsUi
    fun message(): String

    class Base(
        private val data: List<Float>,
        private val labels: List<String>
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Base(data, labels)
        override fun message() = ""
    }

    class Error(
        private val message: String
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Error
        override fun message() = message
    }
}