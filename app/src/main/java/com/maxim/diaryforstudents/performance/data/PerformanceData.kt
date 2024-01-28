package com.maxim.diaryforstudents.performance.data

import java.io.Serializable

interface PerformanceData: Serializable {

    fun search(search: String): Boolean = true

    interface Mapper<T> {
        fun map(name: String, marks: List<Mark>, isFinal: Boolean, average: Float): T
        fun map(): T
        fun map(mark: Int, date: String, isFinal: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceData {
        override fun search(search: String) = name.contains(search, true)
        override fun <T> map(mapper: Mapper<T>) = mapper.map(name, marks, isFinal, average)
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