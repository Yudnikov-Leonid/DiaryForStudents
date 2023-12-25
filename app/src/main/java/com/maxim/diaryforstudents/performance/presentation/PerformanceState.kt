package com.maxim.diaryforstudents.performance.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.maxim.diaryforstudents.R

interface PerformanceState {
    fun show(
        quarterLayout: View,
        first: Button,
        second: Button,
        third: Button,
        fourth: Button,
        actualButton: Button,
        finalButton: Button,
        adapter: PerformanceLessonsAdapter,
        errorTextView: TextView,
        progressBar: ProgressBar,
        searchEditText: TextInputEditText
    )

    object Loading : PerformanceState {
        override fun show(
            quarterLayout: View,
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            actualButton: Button,
            finalButton: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar,
            searchEditText: TextInputEditText
        ) {
            progressBar.visibility = View.VISIBLE
            searchEditText.visibility = View.GONE
            first.isEnabled = false
            second.isEnabled = false
            third.isEnabled = false
            fourth.isEnabled = false
            actualButton.isEnabled = false
            finalButton.isEnabled = false
        }
    }

    data class Error(private val message: String) : PerformanceState {
        override fun show(
            quarterLayout: View,
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            actualButton: Button,
            finalButton: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar,
            searchEditText: TextInputEditText
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            searchEditText.visibility = View.GONE
        }
    }

    data class Base(
        private val quarter: Int,
        private val lessons: List<PerformanceUi>,
        private val isActual: Boolean
    ) : PerformanceState {
        override fun show(
            quarterLayout: View,
            first: Button,
            second: Button,
            third: Button,
            fourth: Button,
            actualButton: Button,
            finalButton: Button,
            adapter: PerformanceLessonsAdapter,
            errorTextView: TextView,
            progressBar: ProgressBar,
            searchEditText: TextInputEditText
        ) { //todo deprecated
            val resourceManager = first.context.resources
            val enableColor = resourceManager.getColor(R.color.blue)
            val disableColor = resourceManager.getColor(R.color.disable_button)
            listOf(first, second, third, fourth).forEachIndexed { i, b ->
                b.setBackgroundColor(if (quarter == i + 1) enableColor else disableColor)
            }
            adapter.update(lessons as List<PerformanceUi.Lesson>)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            quarterLayout.visibility = if (isActual) View.VISIBLE else View.GONE
            actualButton.setBackgroundColor(if (isActual) enableColor else disableColor)
            finalButton.setBackgroundColor(if (!isActual) enableColor else disableColor)
            searchEditText.visibility = View.VISIBLE
            first.isEnabled = true
            second.isEnabled = true
            third.isEnabled = true
            fourth.isEnabled = true
            actualButton.isEnabled = true
            finalButton.isEnabled = true
        }
    }
}