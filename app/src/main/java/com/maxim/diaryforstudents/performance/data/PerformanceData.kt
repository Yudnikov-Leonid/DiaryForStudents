package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.performance.presentation.PerformanceUi
import java.io.Serializable

interface PerformanceData: Serializable {
    fun toUi(): PerformanceUi
    fun search(search: String): Boolean = true
    fun message(): String = ""

    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceData {
        override fun toUi() = PerformanceUi.Lesson(name, grades.map { it.toUi() }, isFinal, average)
        override fun search(search: String) = name.contains(search, true)
    }

    object Empty : PerformanceData {
        override fun toUi() = PerformanceUi.Empty
    }

    data class Grade(
        private val grade: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceData {
        override fun toUi() = PerformanceUi.Grade(grade, date, isFinal)
    }

    data class Error(private val message: String): PerformanceData {
        override fun toUi() = PerformanceUi.Error(message)
        override fun message() = message
    }
}