package com.maxim.diaryforstudents.performance

import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.text.SimpleDateFormat
import java.util.Calendar

interface PerformanceUi {
    fun showName(textView: TextView) {}
    fun showDate(textView: TextView) {}
    fun showGrades(adapter: PerformanceGradesAdapter) {}
    fun showAverage(textView: TextView) {}
    fun same(item: PerformanceUi): Boolean
    fun sameContent(item: PerformanceUi): Boolean = false
    object Empty: PerformanceUi {
        override fun same(item: PerformanceUi) = item is Empty
    }

    data class Lesson(
        private val name: String,
        private val grades: List<Grade>,
        private val average: Float
    ) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showGrades(adapter: PerformanceGradesAdapter) {
            adapter.update(grades)
        }

        override fun showAverage(textView: TextView) {
            textView.text = average.toString()
            val color =
                if (average <= 2.5) R.color.red else if (average <= 3.5) R.color.yellow
                else if (average <= 4.5) R.color.green else R.color.light_green
            textView.setTextColor(textView.context.getColor(color))
        }

        override fun same(item: PerformanceUi) = item is Lesson && item.name == name

        override fun sameContent(item: PerformanceUi) =
            item is Lesson && item.name == name && item.grades == grades && item.average == average
    }

    data class Grade(private val grade: Int, private val date: Int) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = grade.toString()
            val color = when (grade) {
                1, 2 -> R.color.red
                3 -> R.color.yellow
                4 -> R.color.green
                5 -> R.color.light_green
                else -> R.color.black
            }
            textView.setTextColor(textView.context.getColor(color))
        }

        override fun showDate(textView: TextView) {
            if (date in 100..400) {
                textView.text = when(date) {
                    100 -> "I"
                    200 -> "II"
                    300 -> "III"
                    400 -> "IV"
                    else -> "fail"
                }
            } else {
                val formatter = SimpleDateFormat("dd.MM")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date * 86400000L
                textView.text = formatter.format(calendar.time)
            }
        }

        override fun same(item: PerformanceUi) = item is Grade && item.date == date

        override fun sameContent(item: PerformanceUi) = item is Grade && item.grade == grade
    }
}