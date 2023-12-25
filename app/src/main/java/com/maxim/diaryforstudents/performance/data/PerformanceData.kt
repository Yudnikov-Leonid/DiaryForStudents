package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.performance.presentation.PerformanceUi
import java.util.Locale

interface PerformanceData {
    fun toUi(): PerformanceUi
    fun search(search: String): Boolean = true
    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val average: Float
    ) : PerformanceData {
        override fun toUi() = PerformanceUi.Lesson(name, grades.map { it.toUi() }, average)
        override fun search(search: String) = name.lowercase(Locale.ROOT).contains(search.lowercase(Locale.ROOT))
    }

    object Empty: PerformanceData {
        override fun toUi() = PerformanceUi.Empty
    }

    data class Grade(
        private val grade: Int,
        private val date: Int
    ) : PerformanceData {
        override fun toUi() = PerformanceUi.Grade(grade, date)
    }
}