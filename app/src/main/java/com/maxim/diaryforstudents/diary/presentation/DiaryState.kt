package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface DiaryState: Serializable {
    fun show(
        lessonsAdapter: DiaryLessonsAdapter,
        daysAdapter: DiaryDaysAdapter,
        monthTitle: TextView,
        progressBar: ProgressBar,
        errorTextView: TextView,
        previousDayButton: ImageButton,
        nextDayButton: ImageButton,
        daysRecyclerView: View,
        lessonsRecyclerView: View
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
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            day.showLessons(lessonsAdapter)
            daysAdapter.update(days)
            day.showName(monthTitle)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            previousDayButton.visibility = View.VISIBLE
            nextDayButton.visibility = View.VISIBLE
            daysRecyclerView.visibility = View.VISIBLE
            lessonsRecyclerView.visibility = View.VISIBLE
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
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            errorTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
            daysRecyclerView.visibility = View.GONE
            lessonsRecyclerView.visibility = View.GONE
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
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
            daysRecyclerView.visibility = View.GONE
            lessonsRecyclerView.visibility = View.GONE
        }
    }
}