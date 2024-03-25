package com.maxim.diaryforstudents.selectUser.presentation

import android.widget.TextView

interface SelectUserUi {
    fun showName(textView: TextView)
    fun showSchool(textView: TextView)

    data class Base(private val name: String, private val school: String): SelectUserUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showSchool(textView: TextView) {
            textView.text = school
        }
    }
}