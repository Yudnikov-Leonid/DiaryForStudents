package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.widget.TextView
import java.io.Serializable

interface StudentUi: Serializable {
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
