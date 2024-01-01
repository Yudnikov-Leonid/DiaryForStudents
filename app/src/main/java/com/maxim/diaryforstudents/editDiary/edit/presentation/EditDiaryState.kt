package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

interface EditDiaryState {
    fun show(adapter: StudentsAdapter, recyclerView: View, progressBar: View, newLessonButton: FloatingActionButton)

    fun setGrade(grade: Int?, userId: String, date: Int) {}

    object Loading : EditDiaryState {
        override fun show(
            adapter: StudentsAdapter,
            recyclerView: View,
            progressBar: View,
            newLessonButton: FloatingActionButton
        ) {
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            newLessonButton.isEnabled = false
        }
    }

    data class Base(private val list: List<LessonUi>) : EditDiaryState {
        override fun show(
            adapter: StudentsAdapter,
            recyclerView: View,
            progressBar: View,
            newLessonButton: FloatingActionButton
        ) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            newLessonButton.isEnabled = true
            adapter.update(list)
        }

        override fun setGrade(grade: Int?, userId: String, date: Int) {
            list.forEach { it.setGrade(grade, userId, date) }
        }
    }
}