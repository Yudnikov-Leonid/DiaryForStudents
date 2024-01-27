package com.maxim.diaryforstudents.performance.eduData

import com.maxim.diaryforstudents.performance.domain.PerformanceDomain
import java.io.Serializable

interface PerformanceData: Serializable {

    fun toDomain(): PerformanceDomain
    fun search(search: String): Boolean = true

    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceData {
        override fun toDomain() = PerformanceDomain.Lesson(name, grades.map { it.toDomain() }, isFinal, average)
        override fun search(search: String) = name.contains(search, true)
    }

    object Empty : PerformanceData {
        override fun toDomain() = PerformanceDomain.Empty
    }

    data class Grade(
        private val grade: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceData {
        override fun toDomain() = PerformanceDomain.Grade(grade, date, isFinal)
    }
}