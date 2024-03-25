package com.maxim.diaryforstudents.performance.common.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import com.facebook.shimmer.ShimmerFrameLayout
import java.io.Serializable

interface PerformanceState: Serializable {
    fun show(
        quarterSpinner: Spinner,
        settingsImageButton: ImageButton,
    ) {}

    fun show(adapter: PerformanceLessonsAdapter, errorTextView: TextView, retryButton: Button, skeletonLoading: ShimmerFrameLayout)

    object Loading : PerformanceState {
        override fun show(quarterSpinner: Spinner, settingsImageButton: ImageButton) {
            settingsImageButton.visibility = View.GONE
            quarterSpinner.visibility = View.GONE
        }

        override fun show(
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            skeletonLoading: ShimmerFrameLayout
        ) {
            adapter.update(emptyList(), ProgressType.Hide, false)
            skeletonLoading.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : PerformanceState {
        override fun show(quarterSpinner: Spinner, settingsImageButton: ImageButton) {
            settingsImageButton.visibility = View.GONE
            quarterSpinner.visibility = View.GONE
        }

        override fun show(
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            skeletonLoading: ShimmerFrameLayout
        ) {
            adapter.update(emptyList(), ProgressType.Hide, false)
            skeletonLoading.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            errorTextView.text = message
        }
    }

    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>,
        private val isFinal: Boolean,
        private val progressType: ProgressType,
        private val showType: Boolean
    ) : PerformanceState {
        override fun show(quarterSpinner: Spinner, settingsImageButton: ImageButton) {
            quarterSpinner.visibility = View.VISIBLE
            settingsImageButton.visibility = if (isFinal) View.GONE else View.VISIBLE
            quarterSpinner.setSelection(quarter - 1)
        }

        override fun show(
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            retryButton: Button,
            skeletonLoading: ShimmerFrameLayout
        ) {
            adapter.update(lessons, progressType, showType)
            skeletonLoading.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }
}