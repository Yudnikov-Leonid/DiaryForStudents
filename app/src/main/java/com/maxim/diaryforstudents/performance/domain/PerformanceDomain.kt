package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

interface PerformanceDomain {
    fun toUi(): PerformanceUi
    fun message(): String = ""

    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceDomain {
        override fun toUi() = PerformanceUi.Lesson(name, grades.map { it.toUi() }, isFinal, average)
    }

    object Empty : PerformanceDomain {
        override fun toUi() = PerformanceUi.Empty
    }

    data class Grade(
        private val grade: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceDomain {
        override fun toUi() = PerformanceUi.Mark(grade, date, isFinal)
    }

    data class Error(private val message: String): PerformanceDomain {
        override fun toUi() = PerformanceUi.Error(message)
        override fun message() = message
    }
}