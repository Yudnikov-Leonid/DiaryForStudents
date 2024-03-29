package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.performance.common.data.PerformanceData

interface DiaryData {
    fun isDate(date: Int): Boolean
    fun homeworks(): List<Pair<String, String>>
    fun previousHomeworks(): List<Pair<String, String>>
    fun lessons(): List<DiaryData> = emptyList()
    fun period(): Pair<Int, Int> = Pair(0, 0)

    interface Mapper<T> {
        fun map(date: Int, lessons: List<DiaryData>): T
        fun map(
            name: String,
            number: Int,
            teacherName: String,
            topic: String,
            homework: String,
            previousHomework: String,
            startTime: String,
            endTime: String,
            date: Int,
            marks: List<PerformanceData.Mark>,
            absence: List<String>,
            notes: List<String>
        ): T

        fun map(): T
    }

    fun <T> map(mapper: Mapper<T>): T

    data class Day(
        private val date: Int,
        private val lessons: List<DiaryData>
    ) : DiaryData {
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

        override fun lessons() = lessons

        override fun <T> map(mapper: Mapper<T>): T = mapper.map(date, lessons)
    }

    data class Lesson(
        private val name: String,
        private val number: Int,
        private val teacherName: String,
        private val topic: String,
        private val homework: String,
        private val previousHomework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int,
        private val marks: List<PerformanceData.Mark>,
        private val absence: List<String>,
        private val notes: List<String>
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date

        override fun homeworks() = listOf(Pair(name, homework))
        override fun previousHomeworks() = listOf(Pair(name, previousHomework))

        override fun <T> map(mapper: Mapper<T>): T = mapper.map(
            name,
            number,
            teacherName,
            topic,
            homework,
            previousHomework,
            startTime,
            endTime,
            date,
            marks,
            absence,
            notes
        )

        override fun period(): Pair<Int, Int> {
            return Pair(startTime.replace(":", "").toInt(), endTime.replace(":", "").toInt())
        }
    }

    object Empty : DiaryData {
        override fun isDate(date: Int) = false

        override fun homeworks() = emptyList<Pair<String, String>>()
        override fun previousHomeworks() = emptyList<Pair<String, String>>()
        override fun <T> map(mapper: Mapper<T>): T = mapper.map()
    }
}