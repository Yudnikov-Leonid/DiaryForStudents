package com.maxim.diaryforstudents.analytics.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface AnalyticsState: Serializable {
    fun show(
        titleTextView: TextView,
        adapter: AnalyticsAdapter,
        progressBar: ProgressBar,
        errorTextView: TextView,
        retryButton: Button
    )

    object Loading : AnalyticsState {
        override fun show(
            titleTextView: TextView,
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            titleTextView.visibility = View.GONE
            adapter.update(emptyList())
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : AnalyticsState {
        override fun show(
            titleTextView: TextView,
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            titleTextView.visibility = View.GONE
            adapter.update(listOf(AnalyticsUi.Error))
            progressBar.visibility = View.GONE
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
        }
    }

    class Base(
        private val data: List<AnalyticsUi>,
        private val lessonName: String
    ) : AnalyticsState {
        override fun show(
            titleTextView: TextView,
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            titleTextView.text = lessonName
            titleTextView.visibility = if (lessonName.isEmpty()) View.GONE else View.VISIBLE

            adapter.update(data)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }
}