package com.maxim.diaryforstudents.diary.domain

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain

interface DiaryDomain {
    fun isDate(date: Int): Boolean
    fun homeworks(): List<Pair<String, String>>
    fun previousHomeworks(): List<Pair<String, String>>

    interface Mapper<T> {
        fun map(date: Int, lessons: List<DiaryDomain>): T
        fun map(
            name: String,
            teacherName: String,
            topic: String,
            homework: String,
            previousHomework: String,
            startTime: String,
            endTime: String,
            date: Int,
            marks: List<PerformanceDomain.Mark>,
            absence: List<String>,
            notes: List<String>
        ): T
        fun map(): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Day(
        private val date: Int,
        private val lessons: List<DiaryDomain>
    ) : DiaryDomain {
        override fun isDate(date: Int) = date == this.date
        override fun homeworks(): List<Pair<String, String>> {
            return lessons.mapNotNull {
                if (it.homeworks().isNotEmpty()) it.homeworks().first() else null
            }
        }

        override fun previousHomeworks(): List<Pair<String, String>> {
            return lessons.mapNotNull {
                if (it.previousHomeworks().isNotEmpty()) it.previousHomeworks().first() else null
            }
        }

        override fun <T> map(mapper: Mapper<T>) = mapper.map(date, lessons)
    }

    data class Lesson(
        private val name: String,
        private val teacherName: String,
        private val topic: String,
        private val homework: String,
        private val previousHomework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int,
        private val marks: List<PerformanceDomain.Mark>,
        private val absence: List<String>,
        private val notes: List<String>
    ) : DiaryDomain {
        override fun isDate(date: Int) = date == this.date

        override fun homeworks() = listOf(Pair(name, homework))
        override fun previousHomeworks() = listOf(Pair(name, previousHomework))

        override fun <T> map(mapper: Mapper<T>) = mapper.map(
            name, teacherName, topic, homework, previousHomework, startTime, endTime, date, marks, absence, notes
        )
    }

    object Empty : DiaryDomain {
        override fun isDate(date: Int) = false

        override fun homeworks() = emptyList<Pair<String, String>>()
        override fun previousHomeworks() = emptyList<Pair<String, String>>()
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Error(private val message: String) : DiaryDomain {
        override fun isDate(date: Int) = false
        override fun homeworks() = emptyList<Pair<String, String>>()
        override fun previousHomeworks() = emptyList<Pair<String, String>>()
        fun message() = message
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }
}