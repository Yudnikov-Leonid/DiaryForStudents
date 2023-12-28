package com.maxim.diaryforstudents.profile.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.maxim.diaryforstudents.R

interface ProfileState {
    fun show(
        nameTextView: TextView,
        gradeTextView: TextView,
        emailTextView: TextView,
        progressBar: ProgressBar,
        imageView: ImageView,
        signOutButton: Button
    )

    data class Base(
        private val name: String,
        private val grade: String,
        private val email: String
    ) : ProfileState {
        override fun show(
            nameTextView: TextView,
            gradeTextView: TextView,
            emailTextView: TextView,
            progressBar: ProgressBar,
            imageView: ImageView,
            signOutButton: Button
        ) {
            nameTextView.text = name
            if (grade.isNotEmpty()) {
                gradeTextView.visibility = View.VISIBLE
                gradeTextView.text =
                    gradeTextView.context.resources.getString(R.string.student_of, grade)
            } else
                gradeTextView.visibility = View.GONE
            emailTextView.text = email
            progressBar.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            signOutButton.isEnabled = true
        }
    }

    object Loading : ProfileState {
        override fun show(
            nameTextView: TextView,
            gradeTextView: TextView,
            emailTextView: TextView,
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