package com.maxim.diaryforstudents.editDiary.edit.data

import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CacheDate
import com.maxim.diaryforstudents.editDiary.edit.presentation.GradeUi
import com.maxim.diaryforstudents.editDiary.edit.presentation.LessonUi
import com.maxim.diaryforstudents.editDiary.edit.presentation.StudentUi

interface LessonData {
    fun mapToUi(): LessonUi
    data class Students(private val students: List<StudentData>) : LessonData {
        override fun mapToUi() = LessonUi.Students(students.map { it.toUi() })
    }

    data class Lesson(private val lessons: List<GradeData>) : LessonData {
        override fun mapToUi() = LessonUi.Lesson(lessons.map { it.toUi() })
    }
}

interface GradeData {
    fun toUi(): GradeUi
    fun cacheDate(cache: CacheDate) {}

    class Base(private val date: Int, private val userId: String, private val grade: Int?) :
        GradeData {
        override fun toUi() = GradeUi.Base(date, userId, grade)
    }

    object FinalTitle : GradeData {
        override fun toUi() = GradeUi.FinalTitle
    }

    class Date(
        private val date: Int,
        private val startTime: String,
        private val endTime: String,
        private val theme: String,
        private val homework: String
    ) : GradeData {

        override fun cacheDate(cache: CacheDate) {
            cache.cache(date)
        }

        override fun toUi() = GradeUi.Date(date, startTime, endTime, theme, homework)
    }
}

interface StudentData {
    fun toUi(): StudentUi

    class Base(private val name: String) : StudentData {
        override fun toUi() = StudentUi.Base(name)
    }

    class Title(private val name: String) : StudentData {
        override fun toUi() = StudentUi.Title(name)
    }
}