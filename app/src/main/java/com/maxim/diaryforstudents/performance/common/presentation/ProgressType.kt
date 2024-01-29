package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.R
import java.io.Serializable

interface ProgressType: Serializable {
    fun isVisible(): Boolean
    fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int): Int
    fun betterStringId(): Int
    fun lowerStringId(): Int

    object Hide : ProgressType {
        override fun isVisible() = false
        override fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int) = 0
        override fun betterStringId() = -1
        override fun lowerStringId() = -1
    }

    object AWeekAgo : ProgressType {
        override fun isVisible() = true
        override fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int) = week
        override fun betterStringId() = R.string.better_week_progress
        override fun lowerStringId() = R.string.worse_week_progress
    }

    object TwoWeeksAgo : ProgressType {
        override fun isVisible() = true
        override fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int) = twoWeeks
        override fun betterStringId() = R.string.better_two_weeks_progress
        override fun lowerStringId() = R.string.worse_two_weeks_progress
    }

    object AMonthAgo : ProgressType {
        override fun isVisible() = true
        override fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int) = month
        override fun betterStringId() = R.string.better_month_progress
        override fun lowerStringId() = R.string.worse_month_progress
    }

    object PreviousQuarter : ProgressType {
        override fun isVisible() = true
        override fun selectProgress(week: Int, twoWeeks: Int, month: Int, quarter: Int) = quarter
        override fun betterStringId() = R.string.better_quarter_progress
        override fun lowerStringId() = R.string.worse_quarter_progress
    }
}