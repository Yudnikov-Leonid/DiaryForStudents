package com.maxim.diaryforstudents.performance.domain

import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

interface PerformanceDomain {
    fun toUi(): PerformanceUi
    fun message(): String = ""

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceDomain {
        override fun toUi() = PerformanceUi.Lesson(name, marks.map { it.toUi() }, isFinal, average)
    }

    object Empty : PerformanceDomain {
        override fun toUi() = PerformanceUi.Empty
    }

    data class Mark(
        private val mark: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceDomain {
        override fun toUi() = PerformanceUi.Mark(mark, date, isFinal)
    }

    data class Error(private val message: String): PerformanceDomain {
        override fun toUi() = PerformanceUi.Error(message)
        override fun message() = message
    }
}