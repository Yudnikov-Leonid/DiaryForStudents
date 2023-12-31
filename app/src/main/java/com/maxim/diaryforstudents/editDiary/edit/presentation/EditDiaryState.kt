package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.view.View

interface EditDiaryState {
    fun show(adapter: StudentsAdapter, recyclerView: View, progressBar: View)

    object Loading : EditDiaryState {
        override fun show(adapter: StudentsAdapter, recyclerView: View, progressBar: View) {
            recyclerView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    data class Base(private val list: List<LessonUi>) : EditDiaryState {
        override fun show(adapter: StudentsAdapter, recyclerView: View, progressBar: View) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.update(list)
        }
    }
}