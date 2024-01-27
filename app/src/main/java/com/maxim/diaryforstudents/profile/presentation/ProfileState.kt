package com.maxim.diaryforstudents.profile.presentation

import android.widget.TextView
import java.io.Serializable

interface ProfileState: Serializable {
    fun show(
        nameTextView: TextView,
        gradeTextView: TextView,
    )

    data class Base(
        private val profile: ProfileUi
    ) : ProfileState {
        override fun show(
            nameTextView: TextView,
            gradeTextView: TextView,
        ) {
            profile.showName(nameTextView)
            profile.showSchoolAndGrade(gradeTextView)
        }
    }
}