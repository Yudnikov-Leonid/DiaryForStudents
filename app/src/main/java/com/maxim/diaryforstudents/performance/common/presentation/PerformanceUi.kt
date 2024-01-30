package com.maxim.diaryforstudents.performance.common.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import java.io.Serializable
import kotlin.math.absoluteValue

interface PerformanceUi : Serializable {
    fun showName(textView: TextView) {}
    fun showDate(textView: TextView) {}
    fun showMarks(adapter: PerformanceMarksAdapter) {}
    fun showAverage(titleTextView: TextView, textView: TextView) {}
    fun same(item: PerformanceUi): Boolean
    fun sameContent(item: PerformanceUi): Boolean = false
    fun showCalculateButton(view: View) {}
    fun calculate(listener: PerformanceLessonsAdapter.Listener) {}
    fun compare(value: Int): Boolean = false

    suspend fun getLesson(
        interactor: PerformanceInteractor,
        mapper: DiaryDomain.Mapper<DiaryUi>
    ): DiaryUi.Lesson = throw IllegalStateException()

    fun showProgress(imageView: ImageView, textView: TextView, progressType: ProgressType) {}

    object Empty : PerformanceUi {
        override fun same(item: PerformanceUi) = item is Empty
    }

    data class Lesson(
        private val name: String,
        private val marks: List<Mark>,
        private val marksSum: Int,
        private val isFinal: Boolean,
        private val average: Float,
        private val weekProgress: Int,
        private val twoWeeksProgress: Int,
        private val monthProgress: Int,
        private val quarterProgress: Int
    ) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showMarks(adapter: PerformanceMarksAdapter) {
            adapter.update(marks, true)
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
            val colorId = when (average) {
                in 0f..2.49f -> R.color.red
                in 2.5f..3.49f -> R.color.yellow
                in 3.5f..4.49f -> R.color.green
                in 4.5f..5f -> R.color.light_green
                else -> R.color.black
            }
            textView.setTextColor(textView.context.getColor(colorId))
        }

        override fun showProgress(
            imageView: ImageView,
            textView: TextView,
            progressType: ProgressType
        ) {
            val progress =
                progressType.selectProgress(
                    weekProgress,
                    twoWeeksProgress,
                    monthProgress,
                    quarterProgress
                )

            val visibility =
                if (progress in -4..4 || !progressType.isVisible()) View.GONE else View.VISIBLE
            imageView.visibility = visibility
            textView.visibility = visibility

            if (!progressType.isVisible())
                return

            val color =
                imageView.context.getColor(if (progress < 0) R.color.dark_gray else R.color.green)
            imageView.setColorFilter(color)
            imageView.rotation = if (progress < 0) 180f else 0f

            val stringId =
                if (progress < 0) progressType.lowerStringId() else progressType.betterStringId()
            val text = textView.context.getString(stringId, progress.absoluteValue.toString())
            textView.text = text
        }

        override fun showCalculateButton(view: View) {
            view.visibility = if (isFinal) View.GONE else View.VISIBLE
        }

        override fun calculate(listener: PerformanceLessonsAdapter.Listener) {
            listener.calculate(marks, marksSum)
        }

        override fun same(item: PerformanceUi) = item is Lesson && item.name == name

        override fun sameContent(item: PerformanceUi) =
            item is Lesson && item.name == name && item.marks == marks && item.average == average
    }

    data class Mark(
        private val mark: Int,
        private val date: String,
        private val lessonName: String,
        private val isFinal: Boolean
    ) : PerformanceUi {
        override fun showName(textView: TextView) {
            textView.text = mark.toString()
            val color = if (isFinal) R.color.blue else when (mark) {
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
            } else date.substring(0, date.length - 5)
            textView.text = dateUi
        }

        override suspend fun getLesson(
            interactor: PerformanceInteractor,
            mapper: DiaryDomain.Mapper<DiaryUi>
        ): DiaryUi.Lesson =
            interactor.getLesson(lessonName, date).map(mapper) as DiaryUi.Lesson

        override fun compare(value: Int) = value == mark

        override fun same(item: PerformanceUi) = item is Mark && item.date == date

        override fun sameContent(item: PerformanceUi) = item is Mark && item.mark == mark
    }

    data class Error(private val message: String) : PerformanceUi {
        override fun same(item: PerformanceUi) = item is Error
    }
}