package com.maxim.diaryforstudents.profile

import android.widget.TextView

interface ProfileState {
    fun show(textView: TextView)
    data class Initial(private val text: String): ProfileState {
        override fun show(textView: TextView) {
            textView.text = text
        }
    }
}