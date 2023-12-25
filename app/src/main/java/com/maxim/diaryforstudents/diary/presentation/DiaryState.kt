package com.maxim.diaryforstudents.diary.presentation

import android.widget.TextView

interface DiaryState {
    fun show(lessonsAdapter: DiaryLessonsAdapter, daysAdapter: DiaryDaysAdapter, monthTitle: TextView)
    data class Base(
        private val day: DiaryUi.Day,
        private val days: List<DayUi>
    ): DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            monthTitle: TextView
        ) {
            day.showLessons(lessonsAdapter)
            daysAdapter.update(days)
            day.showName(monthTitle)
        }
    }

    data class Error(private val message: String): DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            monthTitle: TextView
        ) {
            TODO("Not yet implemented")
        }
    }
}