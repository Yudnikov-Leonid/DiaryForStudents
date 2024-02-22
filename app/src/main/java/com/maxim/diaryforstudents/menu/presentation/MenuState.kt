package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import android.widget.TextView
import java.io.Serializable

interface MenuState: Serializable {
    fun show(textView: TextView)

    data class Initial(private val newNewsCount: Int): MenuState {
        override fun show(textView: TextView) {
            textView.visibility = if (newNewsCount == 0) View.GONE else View.VISIBLE
            textView.text = newNewsCount.toString()
        }
    }
}