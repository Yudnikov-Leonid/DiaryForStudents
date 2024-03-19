package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import android.widget.TextView
import java.io.Serializable

interface MenuState: Serializable {
    fun showNewsCount(textView: TextView)
    fun showMarksCount(textView: TextView)

    data class Initial(
        private val newMarksCount: Int,
        private val newNewsCount: Int
    ): MenuState {
        override fun showNewsCount(textView: TextView) {
            textView.visibility = if (newNewsCount == 0) View.GONE else View.VISIBLE
            textView.text = newNewsCount.toString()
        }

        override fun showMarksCount(textView: TextView) {
            textView.visibility = if (newMarksCount == 0) View.GONE else View.VISIBLE
            textView.text = newMarksCount.toString()
        }
    }

    object Empty: MenuState {
        override fun showNewsCount(textView: TextView) = Unit
        override fun showMarksCount(textView: TextView) = Unit
    }
}