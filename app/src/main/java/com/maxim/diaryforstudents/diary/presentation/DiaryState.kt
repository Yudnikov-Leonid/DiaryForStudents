package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface DiaryState : Serializable {
    fun show(
        lessonsAdapter: DiaryLessonsAdapter,
        daysAdapter: DaysAdapter,
        monthSelector: View,
        shareButton: View,
        monthTitle: TextView,
        progressBar: ProgressBar,
        errorTextView: TextView,
        retryButton: Button,
        lessonsRecyclerView: View
    )

    data class Base(
        private val day: DiaryUi,
        private val daysOne: List<DayUi>,
        private val daysTwo: List<DayUi>,
        private val daysThree: List<DayUi>,
        private val listener: DaysAdapter.Listener,
        private val homeworkFrom: Boolean
    ) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DaysAdapter,
            monthSelector: View,
            shareButton: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            lessonsRecyclerView: View
        ) {
            day.showLessons(lessonsAdapter, homeworkFrom)
            daysAdapter.update(daysOne, daysTwo, daysThree, listener)
            day.showName(monthTitle)
            monthSelector.visibility = View.VISIBLE
            shareButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            lessonsRecyclerView.visibility = View.VISIBLE
        }
    }

    object Progress : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DaysAdapter,
            monthSelector: View,
            shareButton: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            lessonsRecyclerView: View
        ) {
            monthSelector.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            shareButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            lessonsRecyclerView.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DaysAdapter,
            monthSelector: View,
            shareButton: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            lessonsRecyclerView: View
        ) {
            monthSelector.visibility = View.GONE
            shareButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            lessonsRecyclerView.visibility = View.GONE
        }
    }
}