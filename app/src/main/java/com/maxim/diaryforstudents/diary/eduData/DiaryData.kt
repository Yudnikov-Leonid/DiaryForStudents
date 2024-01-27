package com.maxim.diaryforstudents.diary.eduData

import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.performance.eduData.PerformanceData

interface DiaryData {
    fun isDate(date: Int): Boolean
    fun toUi(): DiaryUi
    fun homeworks(): List<Pair<String, String>>
    fun previousHomeworks(): List<Pair<String, String>>

    data class Day(
        private val date: Int,
        private val lessons: List<DiaryData>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() = DiaryUi.Day(date, lessons.map { it.toUi() })
        override fun homeworks(): List<Pair<String, String>> {
            return lessons.mapNotNull {
                if (it.homeworks().isNotEmpty()) it.homeworks().first() else null
            }
        }

        override fun previousHomeworks(): List<Pair<String, String>> {
            return lessons.mapNotNull {
                if (it.previousHomeworks().isNotEmpty()) it.previousHomeworks().first() else null
            }
        }
    }

    data class Lesson(
        private val name: String,
        private val teacherName: String,
        private val topic: String,
        private val homework: String,
        private val previousHomework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int,
        private val marks: List<PerformanceData.Grade>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date
        override fun toUi() =
            DiaryUi.Lesson(
                name,
                teacherName,
                topic,
                homework,
                previousHomework,
                startTime,
                endTime,
                date,
                marks.map { it.toUi() })

        override fun homeworks() = listOf(Pair(name, homework))
        override fun previousHomeworks() = listOf(Pair(name, previousHomework))
    }

    object Empty : DiaryData {
        override fun isDate(date: Int) = false

        override fun toUi() = DiaryUi.Empty
        override fun homeworks() = emptyList<Pair<String, String>>()
        override fun previousHomeworks() = emptyList<Pair<String, String>>()
    }
}