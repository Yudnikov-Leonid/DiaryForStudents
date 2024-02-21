package com.maxim.diaryforstudents.selectUser.presentation

import android.widget.TextView
import com.maxim.diaryforstudents.R

interface SelectUserUi {
    fun showName(textView: TextView)
    fun showSchool(textView: TextView)

    object Title: SelectUserUi {
        override fun showName(textView: TextView) {
            textView.setText(R.string.select_user)
        }

        override fun showSchool(textView: TextView) = Unit
    }

    data class Base(private val name: String, private val school: String): SelectUserUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showSchool(textView: TextView) {
            textView.text = school
        }
    }
}