package com.maxim.diaryforstudents.performance.data

import com.maxim.diaryforstudents.performance.domain.PerformanceDomain
import java.io.Serializable

interface PerformanceData: Serializable {

    fun toDomain(): PerformanceDomain
    fun search(search: String): Boolean = true

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceData {
        override fun toDomain() = PerformanceDomain.Lesson(name, marks.map { it.toDomain() }, isFinal, average)
        override fun search(search: String) = name.contains(search, true)
    }

    object Empty : PerformanceData {
        override fun toDomain() = PerformanceDomain.Empty
    }

    data class Mark(
        private val mark: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceData {
        override fun toDomain() = PerformanceDomain.Mark(mark, date, isFinal)
    }
}