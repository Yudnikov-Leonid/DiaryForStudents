package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.presentation.DiaryUi

interface DiaryData {
    fun isDate(date: Int): Boolean
    fun toUi(): DiaryUi
    data class Day(
        private val date: Int,
        private val lessons: List<DiaryData>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() = DiaryUi.Day(date, lessons.map { it.toUi() })
    }

    data class Lesson(
        private val name: String,
        private val topic: String,
        private val homework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() = DiaryUi.Lesson(name, topic, homework, startTime, endTime, date)
    }

    object Empty : DiaryData {
        override fun isDate(date: Int) = false

        override fun toUi() = DiaryUi.Empty
    }
}