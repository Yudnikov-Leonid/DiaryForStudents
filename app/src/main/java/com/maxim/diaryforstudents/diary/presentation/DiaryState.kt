package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

interface DiaryState {
    fun show(
        lessonsAdapter: DiaryLessonsAdapter,
        daysAdapter: DiaryDaysAdapter,
        monthTitle: TextView,
        progressBar: ProgressBar,
        errorTextView: TextView,
        previousDayButton: ImageButton,
        nextDayButton: ImageButton
    )

    data class Base(
        private val day: DiaryUi.Day,
        private val days: List<DayUi>
    ) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton
        ) {
            day.showLessons(lessonsAdapter)
            daysAdapter.update(days)
            day.showName(monthTitle)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            previousDayButton.visibility = View.VISIBLE
            nextDayButton.visibility = View.VISIBLE
        }
    }

    object Progress : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton
        ) {
            errorTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
        }
    }
}