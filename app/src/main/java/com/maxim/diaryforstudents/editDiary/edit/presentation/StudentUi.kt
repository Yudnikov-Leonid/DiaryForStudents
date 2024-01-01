package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.widget.TextView

interface StudentUi {
    fun showName(textView: TextView)

    data class Base(private val name: String) : StudentUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }
    }

    data class Title(private val name: String) : StudentUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }
    }
}
