package com.maxim.diaryforstudents.performance

interface PerformanceData {
    fun toUi(): PerformanceUi
    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val average: Float
    ) : PerformanceData {
        override fun toUi() = PerformanceUi.Lesson(name, grades.map { it.toUi() }, average)
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