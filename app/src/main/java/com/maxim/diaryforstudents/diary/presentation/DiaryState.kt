package com.maxim.diaryforstudents.diary.presentation

interface DiaryState {
    fun show(lessonsAdapter: DiaryLessonsAdapter, daysAdapter: DiaryDaysAdapter)
    data class Base(
        private val day: DiaryUi.Day,
        private val days: List<DayUi>
    ): DiaryState {
        override fun show(lessonsAdapter: DiaryLessonsAdapter, daysAdapter: DiaryDaysAdapter) {
            day.showLessons(lessonsAdapter)
            daysAdapter.update(days)
        }
    }

    data class Error(private val message: String): DiaryState {
        override fun show(lessonsAdapter: DiaryLessonsAdapter, daysAdapter: DiaryDaysAdapter) {
            TODO("Not yet implemented")
        }
    }
}