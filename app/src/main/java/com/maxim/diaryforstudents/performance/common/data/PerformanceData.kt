package com.maxim.diaryforstudents.performance.common.data

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import java.io.Serializable

interface PerformanceData : Serializable {

    //todo how to sort without public fields?
    fun average(): Float = 0f
    fun progress(): List<Int> = emptyList()
    fun marksCount(): Int = 0
    fun twoStatus(): Int = 0

    fun toDomain(): PerformanceDomain

    data class Lesson(
        private val name: String,
        private val marks: List<PerformanceData>,
        private val marksSum: Int,
        private val isFinal: Boolean,
        private val average: Float,
        private val twoStatus: Int,
        private val weekProgress: Int,
        private val twoWeeksProgress: Int,
        private val monthProgress: Int,
        private val quarterProgress: Int
    ) : PerformanceData {
        override fun average() = average

        override fun progress() =
            listOf(weekProgress, twoWeeksProgress, monthProgress, quarterProgress)

        override fun marksCount() = marks.sumOf { it.marksCount() }

        override fun twoStatus() = twoStatus

        override fun toDomain() = PerformanceDomain.Lesson(
            name,
            marks.map { it.toDomain() },
            marksSum,
            isFinal,
            average,
            twoStatus,
            weekProgress,
            twoWeeksProgress,
            monthProgress,
            quarterProgress
        )
    }

    object Empty : PerformanceData {
        override fun toDomain() = PerformanceDomain.Empty
    }

    data class Mark(
        private val mark: Int,
        private val type: MarkType,
        private val date: String,
        private val lessonName: String,
        private val isFinal: Boolean,
        private val isChecked: Boolean
    ) : PerformanceData {
        override fun toDomain() = PerformanceDomain.Mark(mark, type, date, lessonName, isFinal, isChecked)

        override fun marksCount() = 1
    }

    data class SeveralMarks(
        private val marks: List<Int>,
        private val types: List<MarkType>,
        private val date: String,
        private val lessonName: String,
        private val isChecked: Boolean
    ) : PerformanceData {

        override fun marksCount() = marks.size
        override fun toDomain() = PerformanceDomain.SeveralMarks(marks, types, date, lessonName, isChecked)
    }
}