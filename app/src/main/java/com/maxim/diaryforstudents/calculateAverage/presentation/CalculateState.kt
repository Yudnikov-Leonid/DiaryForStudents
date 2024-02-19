package com.maxim.diaryforstudents.calculateAverage.presentation

import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

interface CalculateState {
    fun show(adapter: PerformanceMarksAdapter, averageTextView: TextView)

    class Base(private val marks: List<PerformanceUi>, private val average: Float): CalculateState {
        override fun show(adapter: PerformanceMarksAdapter, averageTextView: TextView) {
            adapter.update(marks, false)
            val avr = average.toString()
            averageTextView.text = if (avr.length > 3) avr.substring(0, 4) else avr
            val colorId = when(average) {
                in 0f..2.49f -> R.color.red
                in 2.5f..3.49f -> R.color.yellow
                in 3.5f..4.49f -> R.color.green
                in 4.5f..5f -> R.color.light_green
                else -> R.color.black
            }
            averageTextView.setTextColor(averageTextView.context.getColor(colorId))
        }
    }
}