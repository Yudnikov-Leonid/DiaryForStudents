package com.maxim.diaryforstudents.performance.analytics.presentation

import android.view.View
import android.widget.ProgressBar

interface AnalyticsState {
    fun show(adapter: AnalyticsAdapter, progressBar: ProgressBar)

    class Base(private val data: List<AnalyticsUi>): AnalyticsState {
        override fun show(adapter: AnalyticsAdapter, progressBar: ProgressBar) {
            adapter.update(data)
            progressBar.visibility = View.GONE
        }
    }
}