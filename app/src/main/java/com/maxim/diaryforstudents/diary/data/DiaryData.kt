package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.diary.data.room.RoomLesson
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.data.PerformanceData

interface DiaryData {
    fun isDate(date: Int): Boolean
    fun homeworks(): List<Pair<String, String>>
    fun previousHomeworks(): List<Pair<String, String>>
    fun lessons(): List<DiaryData> = emptyList()
    fun period(): Pair<Int, Int> = Pair(0, 0)

    fun addMenuState(state: MenuLessonState): DiaryData = Empty

    fun toDomain(): DiaryDomain
    fun toRoom(): RoomLesson = throw IllegalStateException("can't map DiaryData to RoomLesson")

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
        override fun toDomain() = DiaryDomain.Day(date, lessons.map { it.toDomain() })
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
        private val notes: List<String>,
        private val menuState: MenuLessonState? = null
    ) : DiaryData {
        override fun isDate(date: Int) = date == this.date

        override fun homeworks() = listOf(Pair(name, homework))
        override fun previousHomeworks() = listOf(Pair(name, previousHomework))

        override fun period(): Pair<Int, Int> {
            return Pair(startTime.replace(":", "").toInt(), endTime.replace(":", "").toInt())
        }

        override fun addMenuState(state: MenuLessonState) = Lesson(
            name, number, teacherName, topic, homework, previousHomework,
            startTime, endTime, date, marks, absence, notes, state
        )

        override fun toDomain() = DiaryDomain.Lesson(
            name, number, teacherName, topic, homework, previousHomework,
            startTime, endTime, date, marks.map { it.toDomain() }, absence, notes, menuState
        )

        override fun toRoom(): RoomLesson {
            return RoomLesson(
                name, number, teacherName, topic, homework, previousHomework,
                startTime, endTime, date
            )
        }
    }

    object Empty : DiaryData {
        override fun isDate(date: Int) = false

        override fun homeworks() = emptyList<Pair<String, String>>()
        override fun previousHomeworks() = emptyList<Pair<String, String>>()
        override fun toDomain() = DiaryDomain.Empty
    }
}