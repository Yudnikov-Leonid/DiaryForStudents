package com.maxim.diaryforstudents.calculateAverage.presentation

import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarksAdapter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import kotlin.math.roundToInt

interface CalculateState {
    fun show(adapter: PerformanceMarksAdapter, averageTextView: TextView)

    class Base(
        private val marks: List<PerformanceUi>, private val average: Float,
        private val colorManager: ColorManager
    ) : CalculateState {
        override fun show(
            adapter: PerformanceMarksAdapter,
            averageTextView: TextView,
        ) {
            adapter.update(marks, showDate =  false, showType =  false)
            val avr = average.toString()
            averageTextView.text = if (avr.length > 3) ((average * 100).roundToInt() / 100f).toString() else avr
            colorManager.showColor(
                averageTextView, when (average) {
                    in 0f..1.49f -> 1
                    in 1.5f..2.49f -> 2
                    in 2.5f..3.49f -> 3
                    in 3.5f..4.49f -> 4
                    in 4.5f..5.49f -> 5
                    else -> 0
                }.toString(), when (average) {
                    in 0f..2.49f -> R.color.red
                    in 2.5f..3.49f -> R.color.yellow
                    in 3.5f..4.49f -> R.color.green
                    in 4.5f..5f -> R.color.light_green
                    else -> R.color.black
                }
            )
        }
    }
}