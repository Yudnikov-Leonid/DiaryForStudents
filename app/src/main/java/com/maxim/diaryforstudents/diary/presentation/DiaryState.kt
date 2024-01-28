package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.io.Serializable
import java.util.Locale

interface DiaryState : Serializable {
    fun show(
        lessonsAdapter: DiaryLessonsAdapter,
        daysAdapter: DiaryDaysAdapter,
        shareHomework: ImageButton,
        filterTextView: TextView,
        homeworkType: TextView,
        monthSelector: View,
        monthTitle: TextView,
        progressBar: ProgressBar,
        errorTextView: TextView,
        retryButton: Button,
        previousDayButton: ImageButton,
        nextDayButton: ImageButton,
        daysRecyclerView: View,
        lessonsRecyclerView: View
    )

    data class Base(
        private val day: DiaryUi,
        private val days: List<DayUi>,
        private val filterCount: Int,
        private val homeworkFrom: Boolean
    ) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            shareHomework: ImageButton,
            filterTextView: TextView,
            homeworkType: TextView,
            monthSelector: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            val resourceManager = shareHomework.resources

            val text = resourceManager.getString(R.string.filters, filterCount.toString())
            filterTextView.text = text
            val homeworkTypeText = resourceManager.getString(
                R.string.homework_type,
                resourceManager.getString(if (homeworkFrom) R.string.actual else R.string.previous)
                    .lowercase(Locale.getDefault())
            )
            homeworkType.text = homeworkTypeText
            day.showLessons(lessonsAdapter, homeworkFrom)
            daysAdapter.update(days)
            day.showName(monthTitle)
            monthSelector.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            previousDayButton.visibility = View.VISIBLE
            nextDayButton.visibility = View.VISIBLE
            daysRecyclerView.visibility = View.VISIBLE
            lessonsRecyclerView.visibility = View.VISIBLE
            shareHomework.visibility = View.VISIBLE
            filterTextView.visibility = View.VISIBLE
            homeworkType.visibility = View.VISIBLE
        }
    }

    object Progress : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            shareHomework: ImageButton,
            filterTextView: TextView,
            homeworkType: TextView,
            monthSelector: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            monthSelector.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
            daysRecyclerView.visibility = View.GONE
            lessonsRecyclerView.visibility = View.GONE
            shareHomework.visibility = View.GONE
            filterTextView.visibility = View.GONE
            homeworkType.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : DiaryState {
        override fun show(
            lessonsAdapter: DiaryLessonsAdapter,
            daysAdapter: DiaryDaysAdapter,
            shareHomework: ImageButton,
            filterTextView: TextView,
            homeworkType: TextView,
            monthSelector: View,
            monthTitle: TextView,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button,
            previousDayButton: ImageButton,
            nextDayButton: ImageButton,
            daysRecyclerView: View,
            lessonsRecyclerView: View
        ) {
            monthSelector.visibility = View.GONE
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            errorTextView.visibility = View.VISIBLE
            errorTextView.text = message
            previousDayButton.visibility = View.GONE
            nextDayButton.visibility = View.GONE
            daysRecyclerView.visibility = View.GONE
            lessonsRecyclerView.visibility = View.GONE
            shareHomework.visibility = View.GONE
            filterTextView.visibility = View.GONE
            homeworkType.visibility = View.GONE
        }
    }
}