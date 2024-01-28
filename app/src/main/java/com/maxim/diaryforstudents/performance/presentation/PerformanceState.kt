package com.maxim.diaryforstudents.performance.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import java.io.Serializable

interface PerformanceState: Serializable {
    fun show(
        quarterSpinner: Spinner,
        adapter: PerformanceLessonsAdapter,
        errorTextView: TextView,
        retryButton: Button,
        progressBar: ProgressBar,
    )

    object Loading : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            quarterSpinner.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            adapter.update(emptyList())
            quarterSpinner.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            errorTextView.text = message
        }
    }

    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>,
        private val isFinal: Boolean
    ) : PerformanceState {
        override fun show(
            quarterSpinner: Spinner,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            progressBar: ProgressBar
        ) {
            adapter.update(lessons as List<PerformanceUi.Lesson>)
            quarterSpinner.visibility = if (isFinal) View.GONE else View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            quarterSpinner.setSelection(quarter - 1)
        }
    }
}