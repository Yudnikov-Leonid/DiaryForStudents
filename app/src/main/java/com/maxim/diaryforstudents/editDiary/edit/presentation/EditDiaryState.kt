package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.View

interface EditDiaryState {
    fun show(adapter: StudentsAdapter, progressBar: View)
    object Loading: EditDiaryState {
        override fun show(adapter: StudentsAdapter, progressBar: View) {
            progressBar.visibility = View.VISIBLE
        }
    }

    data class Base(private val list: List<LessonUi>): EditDiaryState {
        override fun show(adapter: StudentsAdapter, progressBar: View) {
            progressBar.visibility = View.GONE
            adapter.update(list)
        }
    }
}