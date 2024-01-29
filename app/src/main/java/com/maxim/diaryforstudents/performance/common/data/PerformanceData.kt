package com.maxim.diaryforstudents.performance.common.data

import java.io.Serializable

interface PerformanceData : Serializable {

    fun search(search: String): Boolean = true

    //todo public to sort
    fun average(): Float = 0f
    fun progress(): List<Int> = emptyList()

    interface Mapper<T> {
        fun map(
            name: String,
            marks: List<Mark>,
            marksSum: Int,
            isFinal: Boolean,
            average: Float,
            weekProgress: Int,
            twoWeeksProgress: Int,
            monthProgress: Int,
            quarterProgress: Int,
        ): T

        fun map(): T
        fun map(mark: Int, date: String, isFinal: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val marksSum: Int,
        private val isFinal: Boolean,
        private val average: Float,
        private val weekProgress: Int,
        private val twoWeeksProgress: Int,
        private val monthProgress: Int,
        private val quarterProgress: Int
    ) : PerformanceData {
        override fun search(search: String) = name.contains(search, true)

        override fun average() = average

        override fun progress() = listOf(weekProgress, twoWeeksProgress, monthProgress, quarterProgress)

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
        private val isFinal: Boolean
    ) : PerformanceData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(mark, date, isFinal)
    }
}