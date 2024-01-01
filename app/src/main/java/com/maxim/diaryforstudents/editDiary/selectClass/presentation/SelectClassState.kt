package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import android.view.View

interface SelectClassState {
    fun show(adapter: ClassesAdapter, progressBar: View)

    object Loading : SelectClassState {
        override fun show(adapter: ClassesAdapter, progressBar: View) {
            progressBar.visibility = View.VISIBLE
        }
    }

    data class Base(private val list: List<ClassUi>) : SelectClassState {
        override fun show(adapter: ClassesAdapter, progressBar: View) {
            adapter.update(list)
            progressBar.visibility = View.GONE
        }
    }
}