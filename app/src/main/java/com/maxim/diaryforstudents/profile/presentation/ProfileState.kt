package com.maxim.diaryforstudents.profile.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.maxim.diaryforstudents.profile.data.GradeResult
import java.io.Serializable

interface ProfileState: Serializable {
    fun show(
        nameTextView: TextView,
        gradeTextView: TextView,
        progressBar: ProgressBar,
        imageView: ImageView,
        signOutButton: Button
    )

    data class Base(
        private val name: String,
        private val grade: GradeResult,
        private val email: String
    ) : ProfileState {
        override fun show(
            nameTextView: TextView,
            gradeTextView: TextView,
            progressBar: ProgressBar,
            imageView: ImageView,
            signOutButton: Button
        ) {
            nameTextView.text = name
            grade.show(gradeTextView)
            progressBar.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            signOutButton.isEnabled = true
        }
    }

    object Loading : ProfileState {
        override fun show(
            nameTextView: TextView,
            gradeTextView: TextView,
            progressBar: ProgressBar,
            imageView: ImageView,
            signOutButton: Button
        ) {
            progressBar.visibility = View.VISIBLE
            imageView.visibility = View.INVISIBLE
            signOutButton.isEnabled = false
        }

    }
}