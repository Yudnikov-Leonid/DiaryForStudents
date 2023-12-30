package com.maxim.diaryforstudents.editDiary.edit.data

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

    class Base(private val grade: Int?): GradeData {
        override fun toUi() = GradeUi.Base(grade)
    }

    class Date(private val date: Int): GradeData {
        override fun toUi() = GradeUi.Date(date)
    }
}

interface StudentData {
    fun toUi(): StudentUi

    class Base(private val name: String): StudentData {
        override fun toUi() = StudentUi.Base(name)
    }

    class Title(private val name: String): StudentData {
        override fun toUi() = StudentUi.Title(name)
    }
}