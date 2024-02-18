package com.maxim.diaryforstudents.analytics.domain

import com.maxim.diaryforstudents.analytics.presentation.AnalyticsUi

interface AnalyticsDomain {
    fun toUi(): AnalyticsUi
    fun message(): String

    class LineCommon(
        private val data: List<Float>,
        private val labels: List<String>,
        private val quarter: Int,
        private val interval: Int
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.LineCommon(data, labels, quarter, interval)
        override fun message() = ""
    }

    class LineMarks(
        private val fiveData: List<Float>,
        private val fourData: List<Float>,
        private val threeData: List<Float>,
        private val twoData: List<Float>,
        private val labels: List<String>
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.LineMarks(fiveData, fourData, threeData, twoData, labels)

        override fun message() = ""
    }

    class PieMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.PieMarks(fiveCount, fourCount, threeCount, twoCount)

        override fun message() = ""
    }

    class PieFinalMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.PieFinalMarks(fiveCount, fourCount, threeCount, twoCount)

        override fun message() = ""
    }

    class Error(
        private val message: String
    ) : AnalyticsDomain {
        override fun toUi() = AnalyticsUi.Error
        override fun message() = message
    }
}