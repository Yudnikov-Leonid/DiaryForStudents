package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import android.widget.TextView

interface MenuState {
    fun show(textView: TextView)

    class Initial(private val newNewsCount: Int): MenuState {
        override fun show(textView: TextView) {
            textView.visibility = if (newNewsCount == 0) View.GONE else View.VISIBLE
            textView.text = newNewsCount.toString()
        }
    }
}