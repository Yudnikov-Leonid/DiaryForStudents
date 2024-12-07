package com.maxim.diaryforstudents.performance.common.domain

import com.maxim.diaryforstudents.performance.common.presentation.MarkType

interface PerformanceDomain {
    fun message(): String = ""

    interface Mapper<T> {
        fun map(
            name: String,
            marks: List<PerformanceDomain>,
            marksSum: Int,
            isFinal: Boolean,
            average: Float,
            twoStatus: Int,
            weekProgress: Int,
            twoWeeksProgress: Int,
            monthProgress: Int,
            quarterProgress: Int,
        ): T

        fun map(): T
        fun map(message: String): T
        fun map(mark: Int, type: MarkType, date: String, lessonName: String, isFinal: Boolean, isChecked: Boolean): T
        fun map(marks: List<Int>, types: List<MarkType>, date: String, lessonName: String, isChecked: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Lesson(
        private val name: String,
        private val marks: List<PerformanceDomain>,
        private val marksSum: Int,
        private val isFinal: Boolean,
        private val average: Float,
        private val twoStatus: Int,
        private val weekProgress: Int,
        private val twoWeeksProgress: Int,
        private val monthProgress: Int,
        private val quarterProgress: Int
    ) : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(
                name, marks, marksSum, isFinal, average, twoStatus, weekProgress,
                twoWeeksProgress, monthProgress, quarterProgress
            )
    }

    object Empty : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Mark(
        private val mark: Int,
        private val type: MarkType,
        private val date: String,
        private val lessonName: String,
        private val isFinal: Boolean,
        private val isChecked: Boolean
    ) : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(mark, type, date, lessonName, isFinal, isChecked)
    }

    data class SeveralMarks(
        private val marks: List<Int>,
        private val types: List<MarkType>,
        private val date: String,
        private val lessonName: String,
        private val isChecked: Boolean
    ) : PerformanceDomain {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(marks, types, date, lessonName, isChecked)
    }

    data class Error(private val message: String) : PerformanceDomain {
        override fun message() = message
        override fun <T> map(mapper: Mapper<T>) = mapper.map(message)
    }
}