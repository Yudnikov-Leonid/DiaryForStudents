package com.maxim.diaryforstudents.performance.common.presentation

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import java.io.Serializable
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

interface PerformanceUi : Serializable {
    fun showName(textView: TextView) {}
    fun showName(textView: TextView, colorManager: ColorManager) {}
    fun showDate(textView: TextView) {}
    fun showMarks(adapter: PerformanceMarksAdapter) {}
    fun showAverage(titleTextView: TextView, textView: TextView, colorManager: ColorManager) {}
    fun same(item: PerformanceUi): Boolean
    fun sameContent(item: PerformanceUi): Boolean = false
    fun showCalculateButton(view: View) {}
    fun calculate(listener: PerformanceLessonsAdapter.Listener) {}
    fun analytics(listener: PerformanceLessonsAdapter.Listener) {}
    fun compare(value: Int): Boolean = false
    fun openDetails(listener: PerformanceMarksAdapter.Listener) {}

    fun showType(view: View) {}
    fun showIsChecked(view: View) {}

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
        private val marks: List<PerformanceUi>,
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

        override fun showAverage(
            titleTextView: TextView,
            textView: TextView,
            colorManager: ColorManager
        ) {
            if (isFinal) {
                titleTextView.visibility = View.GONE
                textView.visibility = View.GONE
                return
            } else {
                titleTextView.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
            val avr = average.toString()
            textView.text =
                if (avr.length > 3) ((average * 100).roundToInt() / 100f).toString() else avr
            colorManager.showColor(
                textView, when (average) {
                    in 0f..1.49f -> 1
                    in 1.5f..2.49f -> 2
                    in 2.5f..3.49f -> 3
                    in 3.5f..4.49f -> 4
                    in 4.5f..5f -> 5
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

        override fun analytics(listener: PerformanceLessonsAdapter.Listener) {
            listener.analytics(name)
        }

        override fun same(item: PerformanceUi) = item is Lesson && item.name == name

        override fun sameContent(item: PerformanceUi) =
            item is Lesson && item.name == name && item.marks == marks && item.average == average
    }

    data class Mark(
        private val mark: Int,
        private val type: MarkType,
        private val date: String,
        private val lessonName: String,
        private val isFinal: Boolean,
        private val isChecked: Boolean
    ) : PerformanceUi {
        override fun showName(textView: TextView, colorManager: ColorManager) {
            textView.text = mark.toString()
            if (isFinal)
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.blue))
            else
                colorManager.showColor(
                    textView, mark.toString(), when (mark) {
                        1, 2 -> R.color.red
                        3 -> R.color.yellow
                        4 -> R.color.green
                        5 -> R.color.light_green
                        else -> R.color.black
                    }
                )
        }

        override fun showType(view: View) {
            type.show(view)
        }

        override fun showIsChecked(view: View) {
            val drawable = if (isChecked) R.drawable.mark_current else when (mark) {
                1, 2 -> R.drawable.new_mark_2
                3 -> R.drawable.new_mark_3
                4 -> R.drawable.new_mark_4
                5 -> R.drawable.new_mark_5
                else -> R.drawable.new_mark_unknown
            }
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                drawable,
                view.context.theme
            )
        }

        override fun openDetails(listener: PerformanceMarksAdapter.Listener) {
            if (!isFinal)
                listener.details(this)
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
            interactor.getLessonByMark(lessonName, date).map(mapper) as DiaryUi.Lesson

        override fun compare(value: Int) = value == mark

        override fun same(item: PerformanceUi) = item is Mark && item.date == date

        override fun sameContent(item: PerformanceUi) = item is Mark && item.mark == mark
    }

    data class SeveralMarks(
        private val marks: List<Int>,
        private val types: List<MarkType>,
        private val date: String,
        private val lessonName: String,
        private val isChecked: Boolean
    ) : PerformanceUi {
        override fun same(item: PerformanceUi) = item is SeveralMarks && item.date == date

        override fun sameContent(item: PerformanceUi) = item is SeveralMarks && item.marks == marks

        override fun openDetails(listener: PerformanceMarksAdapter.Listener) {
            listener.details(this)
        }

        override fun showIsChecked(view: View) {
            val drawable = if (isChecked) R.drawable.mark_current else when (marks.min()) {
                1, 2 -> R.drawable.new_mark_2
                3 -> R.drawable.new_mark_3
                4 -> R.drawable.new_mark_4
                5 -> R.drawable.new_mark_5
                else -> R.drawable.new_mark_unknown
            }
            view.background = ResourcesCompat.getDrawable(
                view.resources,
                drawable,
                view.context.theme
            )
        }

        override fun showName(textView: TextView, colorManager: ColorManager) {
            var markText = SpannableString(marks.first().toString())
            markText.setSpan(
                ForegroundColorSpan(getColor(marks.first(), colorManager, textView.context)),
                0,
                1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = markText

            val slash = SpannableString("/")
            slash.setSpan(
                ForegroundColorSpan(textView.context.getColor(R.color.slash)),
                0,
                1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            for (i in 1..marks.lastIndex) {
                textView.append(slash)
                markText = SpannableString(marks[i].toString())
                markText.setSpan(
                    ForegroundColorSpan(getColor(marks[i], colorManager, textView.context)),
                    0,
                    1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                textView.append(markText)
            }
        }

        override suspend fun getLesson(
            interactor: PerformanceInteractor,
            mapper: DiaryDomain.Mapper<DiaryUi>
        ): DiaryUi.Lesson =
            interactor.getLessonByMark(lessonName, date).map(mapper) as DiaryUi.Lesson

        //todo
        private fun getColor(mark: Int, colorManager: ColorManager, context: Context): Int {
            return colorManager.getColor(
                mark.toString(), when (mark) {
                    1, 2 -> context.resources.getColor(R.color.red, context.theme)
                    3 -> context.resources.getColor(R.color.yellow, context.theme)
                    4 -> context.resources.getColor(R.color.green, context.theme)
                    5 -> context.resources.getColor(R.color.light_green, context.theme)
                    else -> context.resources.getColor(R.color.black, context.theme)
                }
            )
        }

        override fun showDate(textView: TextView) {
            val dateUi = date.substring(0, date.length - 5)
            textView.text = dateUi
        }
    }

    data class Error(private val message: String) : PerformanceUi {
        override fun same(item: PerformanceUi) = item is Error
    }
}