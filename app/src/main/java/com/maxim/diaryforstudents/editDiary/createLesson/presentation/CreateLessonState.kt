package com.maxim.diaryforstudents.editDiary.createLesson.presentation

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

interface CreateLessonState {
    fun show(
        startTimeEditText: TextInputLayout,
        endTimeEditText: TextInputLayout,
        themeEditText: EditText,
        homeworkEditText: EditText,
        progressBar: View,
        dismiss: Dismiss
    )

    object Initial: CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            progressBar.visibility = View.GONE
        }
    }

    object Loading : CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            startTimeEditText.editText!!.isEnabled = false
            endTimeEditText.editText!!.isEnabled = false
            themeEditText.isEnabled = false
            homeworkEditText.isEnabled = false
            progressBar.visibility = View.VISIBLE
        }
    }

    data class StartTimeError(private val message: String) : CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            startTimeEditText.error = message
            startTimeEditText.isErrorEnabled = true
        }
    }

    data class EndTimeError(private val message: String) : CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            endTimeEditText.error = message
            endTimeEditText.isErrorEnabled = true
        }
    }

    data class Error(private val message: String) : CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            dismiss.provideError(message)
        }
    }

    object Success : CreateLessonState {
        override fun show(
            startTimeEditText: TextInputLayout,
            endTimeEditText: TextInputLayout,
            themeEditText: EditText,
            homeworkEditText: EditText,
            progressBar: View,
            dismiss: Dismiss
        ) {
            dismiss.dismiss()
        }
    }
}

interface Dismiss {
    fun dismiss()
    fun provideError(message: String)
}