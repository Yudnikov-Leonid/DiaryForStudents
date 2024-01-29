package com.maxim.diaryforstudents.performance.presentation

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import java.io.Serializable

interface PerformanceState: Serializable {
    fun show(
        quarterSpinner: Spinner,
        settingsBar: LinearLayout,
        adapter: PerformanceLessonsAdapter,
        errorTextView: TextView,
        retryButton: Button,
        progressBar: ProgressBar,
    )

    object Loading : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            settingsBar: LinearLayout,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            settingsBar.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            settingsBar: LinearLayout,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            adapter.update(emptyList())
            settingsBar.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            errorTextView.text = message
        }
    }

    @Suppress("UNCHECKED_CAST")
    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>,
        private val isFinal: Boolean
    ) : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            settingsBar: LinearLayout,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            adapter.update(lessons as List<PerformanceUi.Lesson>)
            settingsBar.visibility = if (isFinal) View.GONE else View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            quarterSpinner.setSelection(quarter - 1)
        }
    }
}