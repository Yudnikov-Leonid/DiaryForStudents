package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar

interface LessonUi {
    fun showNames(adapter: StudentNamesAdapter) {}
    fun showLessonsAndGrades(adapter: EditGradesAdapter) {}
    fun setGrade(grade: Int?, userId: String, date: Int) {}
    data class Students(private val students: List<StudentUi>) : LessonUi {
        override fun showNames(adapter: StudentNamesAdapter) {
            adapter.update(students)
        }
    }

    data class Lesson(private val lessons: List<GradeUi>) : LessonUi {
        override fun showLessonsAndGrades(adapter: EditGradesAdapter) {
            adapter.update(lessons)
        }

        override fun setGrade(grade: Int?, userId: String, date: Int) {
            lessons.forEach { it.setGrade(grade, userId, date) }
        }
    }
}

interface GradeUi {
    fun show(textView: TextView)
    fun setGrade(listener: EditGradesAdapter.Listener, grade: Int?) {}
    fun setGrade(grade: Int?, userId: String, date: Int) {}
    fun editLesson(listener: EditGradesAdapter.Listener) {}

    fun showLesson(startTime: EditText, endTime: EditText, theme: EditText, homework: EditText) {}

    class Base(private val date: Int, private val userId: String, private var grade: Int?) :
        GradeUi {
        override fun show(textView: TextView) {
            grade?.let { textView.text = it.toString() }
        }

        override fun setGrade(listener: EditGradesAdapter.Listener, grade: Int?) {
            listener.setGrade(grade, userId, date)
        }

        override fun setGrade(grade: Int?, userId: String, date: Int) {
            if (date == this.date && userId == this.userId) this.grade = grade
        }
    }

    object FinalTitle: GradeUi {
        override fun show(textView: TextView) = Unit
    }

    class Date(
        private val date: Int,
        private val startTime: String,
        private val endTime: String,
        private val theme: String,
        private val homework: String
    ) :
        GradeUi {
        override fun show(textView: TextView) {
            val formatter = SimpleDateFormat("dd.MM")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            textView.text = formatter.format(calendar.time)
        }

        override fun showLesson(
            startTime: EditText,
            endTime: EditText,
            theme: EditText,
            homework: EditText
        ) {
            startTime.setText(this.startTime)
            endTime.setText(this.endTime)
            theme.setText(this.theme)
            homework.setText(this.homework)
        }

        override fun editLesson(listener: EditGradesAdapter.Listener) {
            listener.editLesson(date, startTime, endTime, theme, homework)
        }
    }
}