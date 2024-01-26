package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.performance.eduData.PerformanceData

interface DiaryData {
    fun isDate(date: Int): Boolean
    fun toUi(): DiaryUi
    fun homeworks(): List<Pair<String, String>>

    data class Day(
        private val date: Int,
        private val lessons: List<DiaryData>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() = DiaryUi.Day(date, lessons.map { it.toUi() })
        override fun homeworks(): List<Pair<String, String>> {
            val list = mutableListOf<Pair<String, String>>()
            lessons.forEach {
                if (it.homeworks().isNotEmpty())
                    list.add(it.homeworks().first())
            }
            return list
        }
    }

    data class Lesson(
        private val name: String,
        private val topic: String,
        private val homework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int,
        private val marks: List<PerformanceData.Grade>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() =
            DiaryUi.Lesson(name, topic, homework, startTime, endTime, date, marks.map { it.toUi() })

        override fun homeworks() = listOf(Pair(name, homework))
    }

    object Empty : DiaryData {
        override fun isDate(date: Int) = false

        override fun toUi() = DiaryUi.Empty
        override fun homeworks() = emptyList<Pair<String, String>>()
    }
}