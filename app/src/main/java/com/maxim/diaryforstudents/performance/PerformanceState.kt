package com.maxim.diaryforstudents.performance

import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.maxim.diaryforstudents.R

interface PerformanceState {
    fun show(
        first: Button,
        second: Button,
        third: Button,
        fourth: Button,
        adapter: PerformanceLessonsAdapter,
        errorTextView: TextView,
        progressBar: ProgressBar
    )

    object Loading: PerformanceState {
        override fun show(
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.VISIBLE
        }
    }
    data class Error(private val message: String): PerformanceState {
        override fun show(
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
        }
    }

    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>
    ) : PerformanceState {
        override fun show(
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar
        ) {
            val resourceManager = first.context.resources
            //todo deprecated
            val enableColor = R.color.blue
            val disableColor = R.color.disable_button
            first.setBackgroundColor(
                if (quarter == 1) resourceManager.getColor(enableColor) else resourceManager.getColor(
                    disableColor
                )
            )
            second.setBackgroundColor(
                if (quarter == 2) resourceManager.getColor(enableColor) else resourceManager.getColor(
                    disableColor
                )
            )
            third.setBackgroundColor(
                if (quarter == 3) resourceManager.getColor(enableColor) else resourceManager.getColor(
                    disableColor
                )
            )
            fourth.setBackgroundColor(
                if (quarter == 4) resourceManager.getColor(enableColor) else resourceManager.getColor(
                    disableColor
                )
            )
            adapter.update(lessons as List<PerformanceUi.Lesson>)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
        }
    }
}