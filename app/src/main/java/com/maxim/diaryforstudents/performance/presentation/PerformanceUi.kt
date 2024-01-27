package com.maxim.diaryforstudents.performance.presentation

import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.io.Serializable

interface PerformanceUi : Serializable {
    fun showName(textView: TextView) {}
    fun showDate(textView: TextView) {}
    fun showGrades(adapter: PerformanceGradesAdapter) {}
    fun showAverage(titleTextView: TextView, textView: TextView) {}
    fun same(item: PerformanceUi): Boolean
    fun sameContent(item: PerformanceUi): Boolean = false

    object Empty : PerformanceUi {
        override fun same(item: PerformanceUi) = item is Empty
    }

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val isFinal: Boolean,
        private val average: Float
    ) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showGrades(adapter: PerformanceGradesAdapter) {
            adapter.update(marks)
        }

        override fun showAverage(titleTextView: TextView, textView: TextView) {
            if (isFinal) {
                titleTextView.visibility = View.GONE
                textView.visibility = View.GONE
                return
            } else {
                titleTextView.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
            val avr = average.toString()
            textView.text = if (avr.length > 3) avr.substring(0, 4) else avr
            val color =
                if (average <= 2.5) R.color.red else if (average <= 3.5) R.color.yellow
                else if (average <= 4.5) R.color.green else R.color.light_green
            textView.setTextColor(textView.context.getColor(color))
        }

        override fun same(item: PerformanceUi) = item is Lesson && item.name == name

        override fun sameContent(item: PerformanceUi) =
            item is Lesson && item.name == name && item.marks == marks && item.average == average
    }

    data class Mark(
        private val grade: Int,
        private val date: String,
        private val isFinal: Boolean
    ) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = grade.toString()
            val color = if (isFinal) R.color.blue else when (grade) {
                1, 2 -> R.color.red
                3 -> R.color.yellow
                4 -> R.color.green
                5 -> R.color.light_green
                else -> R.color.black
            }
            textView.setTextColor(textView.context.getColor(color))
        }

        override fun showDate(textView: TextView) {
            val dateUi = if (isFinal && date.toInt() in 1..7) {
                when (date.toInt()) {
                    1 -> "I"
                    2 -> "II"
                    3 -> "III"
                    4 -> "IV"
                    else -> "Year"
                }
            } else date
            textView.text = dateUi
        }

        override fun same(item: PerformanceUi) = item is Mark && item.date == date

        override fun sameContent(item: PerformanceUi) = item is Mark && item.grade == grade
    }

    data class Error(private val message: String): PerformanceUi {
        override fun same(item: PerformanceUi) = item is Error
    }
}