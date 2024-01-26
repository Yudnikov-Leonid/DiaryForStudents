package com.maxim.diaryforstudents.profile.presentation

import android.widget.TextView

data class EduProfileUi(
    private val fullName: String,
    private val schoolName: String,
    private val grade: String
) {
    fun showName(textView: TextView) {
        textView.text = fullName
    }

    fun showSchoolAndGrade(textView: TextView) {
        val text = "$schoolName\n$grade"
        textView.text = text
    }
}