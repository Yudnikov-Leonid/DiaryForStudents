package com.maxim.diaryforstudents.profile.data

import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R

interface GradeResult {
    fun show(textView: TextView)
    data class Student(private val value: String) : GradeResult {
        override fun show(textView: TextView) {
            textView.text =
                textView.context.resources.getString(R.string.student_of, value)
        }
    }

    data class Teacher(private val value: String) : GradeResult {
        override fun show(textView: TextView) {
            textView.text =
                textView.context.resources.getString(R.string.teacher_of, value)
        }
    }

    object Empty : GradeResult {
        override fun show(textView: TextView) {
            textView.visibility = View.GONE
        }
    }
}