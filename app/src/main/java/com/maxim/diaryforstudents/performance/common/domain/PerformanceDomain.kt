package com.maxim.diaryforstudents.performance.common.domain

interface PerformanceDomain {
    fun message(): String = ""

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
        fun map(message: String): T
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
    ) : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(
                name, marks, marksSum, isFinal, average, weekProgress,
                twoWeeksProgress, monthProgress, quarterProgress
            )
    }

    object Empty : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Mark(
        private val mark: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(mark, date, isFinal)
    }

    data class Error(private val message: String) : PerformanceDomain {
        override fun message() = message
        override fun <T> map(mapper: Mapper<T>) = mapper.map(message)
    }
}