package com.maxim.diaryforstudents.profile.presentation

import android.widget.TextView
import java.io.Serializable

interface ProfileState: Serializable {
    fun show(
        nameTextView: TextView,
    )

    data class Base(
        private val name: String
    ) : ProfileState {
        override fun show(
            nameTextView: TextView,
        ) {
            nameTextView.text = name
        }
    }

    object Empty: ProfileState {
        override fun show(nameTextView: TextView) = Unit
    }
}