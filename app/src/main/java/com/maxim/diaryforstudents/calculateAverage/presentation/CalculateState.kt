package com.maxim.diaryforstudents.calculateAverage.presentation

import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.performance.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

interface CalculateState {
    fun show(adapter: PerformanceMarksAdapter, averageTextView: TextView)

    class Base(private val marks: List<PerformanceUi.Mark>, private val average: Float): CalculateState {
        override fun show(adapter: PerformanceMarksAdapter, averageTextView: TextView) {
            adapter.update(marks, false)
            val avr = average.toString()
            averageTextView.text = if (avr.length > 3) avr.substring(0, 4) else avr
            val color =
                if (average <= 2.5) R.color.red else if (average <= 3.5) R.color.yellow
                else if (average <= 4.5) R.color.green else R.color.light_green
            averageTextView.setTextColor(averageTextView.context.getColor(color))
        }
    }
}