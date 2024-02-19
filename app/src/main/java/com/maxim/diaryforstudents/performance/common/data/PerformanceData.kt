package com.maxim.diaryforstudents.performance.common.data

import java.io.Serializable

interface PerformanceData : Serializable {

    //todo public to sort
    fun average(): Float = 0f
    fun progress(): List<Int> = emptyList()
    fun marksCount(): Int = 0

    interface Mapper<T> {
        fun map(
            name: String,
            marks: List<PerformanceData>,
            marksSum: Int,
            isFinal: Boolean,
            average: Float,
            weekProgress: Int,
            twoWeeksProgress: Int,
            monthProgress: Int,
            quarterProgress: Int,
        ): T

        fun map(): T
        fun map(mark: Int, date: String, lessonName: String, isFinal: Boolean): T
        fun map(marks: List<Int>, date: String, lessonName: String): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Lesson(
        private val name: String,
        private val marks: List<PerformanceData>,
        private val marksSum: Int,
        private val isFinal: Boolean,
        private val average: Float,
        private val weekProgress: Int,
        private val twoWeeksProgress: Int,
        private val monthProgress: Int,
        private val quarterProgress: Int
    ) : PerformanceData {
        override fun average() = average

        override fun progress() = listOf(weekProgress, twoWeeksProgress, monthProgress, quarterProgress)

        override fun marksCount() = marks.size

        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(
                name, marks, marksSum, isFinal, average, weekProgress, twoWeeksProgress,
                monthProgress, quarterProgress
            )
    }

    object Empty : PerformanceData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Mark(
        private val mark: Int,
        private val date: String,
        private val lessonName: String,
        private val isFinal: Boolean
    ) : PerformanceData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(mark, date, lessonName, isFinal)
    }

    data class SeveralMarks(
        private val marks: List<Int>,
        private val date: String,
        private val lessonName: String,
    ) : PerformanceData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(marks, date, lessonName)
    }
}