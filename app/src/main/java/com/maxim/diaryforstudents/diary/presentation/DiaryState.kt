package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface DiaryState : Serializable {
    fun show(
        topLayout: View,
        daysViewPager: View,
        monthTitle: TextView,
        progressBar: ProgressBar,
        errorTextView: TextView,
        retryButton: Button,
        errorBackButton: ImageButton,
        lessonsRecyclerView: View
    )

    fun show(
        lessonsAdapter: DiaryLessonsAdapter,
        daysAdapter: DaysRecyclerViewAdapter,
    ) {}

    data class Base(
        private val day: DiaryUi,
        private val daysOne: List<DayUi>,
        private val daysTwo: List<DayUi>,
        private val daysThree: List<DayUi>,
        private val actualHomework: Boolean
    ) : DiaryState {
        override fun show(
            topLayout: View,
            daysViewPager: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            errorBackButton: ImageButton,
            lessonsRecyclerView: View
        ) {
            day.showName(monthTitle)
            topLayout.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            daysViewPager.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            errorBackButton.visibility = View.GONE
            retryButton.visibility = View.GONE
            lessonsRecyclerView.visibility = View.VISIBLE
        }

        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DaysRecyclerViewAdapter,
        ) {
            day.showLessons(lessonsAdapter, actualHomework)
            daysAdapter.update(daysOne, daysTwo, daysThree)
        }
    }

    object Progress : DiaryState {
        override fun show(
            topLayout: View,
            daysViewPager: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            errorBackButton: ImageButton,
            lessonsRecyclerView: View
        ) {
            topLayout.visibility = View.GONE
            errorTextView.visibility = View.GONE
            errorBackButton.visibility = View.VISIBLE
            daysViewPager.visibility = View.GONE
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            lessonsRecyclerView.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : DiaryState {
        override fun show(
            topLayout: View,
            daysViewPager: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            errorBackButton: ImageButton,
            lessonsRecyclerView: View
        ) {
            topLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            daysViewPager.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorBackButton.visibility = View.VISIBLE
            errorTextView.text = message
            lessonsRecyclerView.visibility = View.GONE
        }
    }
}