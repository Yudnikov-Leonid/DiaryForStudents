package com.maxim.diaryforstudents.diary.presentation

import android.view.View
import android.widget.TextView
import com.maxim.diaryforstudents.core.presentation.Formatter
import java.io.Serializable

interface DiaryUi: Serializable {
    fun same(item: DiaryUi): Boolean
    fun sameContent(item: DiaryUi): Boolean
    fun showTime(textView: TextView) {}
    fun showName(textView: TextView) {}
    fun showTheme(textView: TextView, title: TextView) {}
    fun showHomework(textView: TextView, title: TextView) {}
    fun showLessons(adapter: DiaryLessonsAdapter) {}
    data class Day(
        private val date: Int,
        private val lessons: List<DiaryUi>
    ) : DiaryUi {
        override fun same(item: DiaryUi) = item is Day && item.date == date
        override fun showName(textView: TextView) {
            textView.text = Formatter.Base.format("LLLL", date)
        }

        override fun sameContent(item: DiaryUi) = item is Day && item.lessons == lessons
        override fun showLessons(adapter: DiaryLessonsAdapter) {
            adapter.update(lessons)
        }
    }

    data class Lesson(
        private val name: String,
        private val topic: String,
        private val homework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int
    ) : DiaryUi {

        override fun showTime(textView: TextView) {
            val text = "$startTime - $endTime"
            textView.text = text
        }

        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showTheme(textView: TextView, title: TextView) {
            textView.text = topic
            title.visibility = if (topic.isEmpty()) View.GONE else View.VISIBLE
            textView.visibility = if (topic.isEmpty()) View.GONE else View.VISIBLE
        }

        override fun showHomework(textView: TextView, title: TextView) {
            textView.text = homework
            title.visibility = if (homework.isEmpty()) View.GONE else View.VISIBLE
            textView.visibility = if (homework.isEmpty()) View.GONE else View.VISIBLE
        }

        override fun same(item: DiaryUi) = item is Lesson && item.date == date

        override fun sameContent(item: DiaryUi) =
            item is Lesson && item.name == name && item.topic == topic
                    && item.homework == homework && item.startTime == startTime && item.endTime == endTime
    }

    object Empty : DiaryUi {
        override fun same(item: DiaryUi) = item is Empty

        override fun sameContent(item: DiaryUi) = false
    }
}