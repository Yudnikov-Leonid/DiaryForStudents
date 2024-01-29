package com.maxim.diaryforstudents.diary.presentation

import android.app.ActionBar.LayoutParams
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import com.maxim.diaryforstudents.R
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import java.io.Serializable
import java.lang.StringBuilder

interface DiaryUi : Serializable {
    fun same(item: DiaryUi): Boolean
    fun sameContent(item: DiaryUi): Boolean
    fun showTime(textView: TextView) {}
    fun showName(textView: TextView) {}
    fun showTeacherName(textView: TextView) {}
    fun showTopic(textView: TextView, title: TextView) {}
    fun showHomework(textView: TextView, title: TextView) {}
    fun showPreviousHomework(textView: TextView, title: TextView) {}
    fun showLessons(adapter: DiaryLessonsAdapter, homeworkFrom: Boolean) {}
    fun showMarks(linearLayout: LinearLayout) {}
    fun showNotes(textView: TextView, title: TextView) {}
    fun filter(mapper: Mapper<Boolean>): Day = Day(0, emptyList())
    fun map(mapper: Mapper<Boolean>): Boolean

    interface Mapper<T> {
        fun map(
            name: String,
            topic: String,
            homework: String,
            startTime: String,
            endTime: String,
            date: Int,
            marks: List<PerformanceUi.Mark>
        ): T
    }

    data class Day(
        private val date: Int,
        private val lessons: List<DiaryUi>
    ) : DiaryUi {
        override fun same(item: DiaryUi) = item is Day && item.date == date
        override fun showName(textView: TextView) {
            textView.text = Formatter.Base.format("LLLL yyyy", date)
        }

        override fun sameContent(item: DiaryUi) = item is Day && item.lessons == lessons
        override fun showLessons(adapter: DiaryLessonsAdapter, homeworkFrom: Boolean) {
            adapter.update(lessons, homeworkFrom)
        }

        override fun filter(mapper: Mapper<Boolean>) = Day(
            date,
            lessons.filter { it.map(mapper) }
        )

        override fun map(mapper: Mapper<Boolean>) = true
    }

    data class Lesson(
        private val name: String,
        private val teacherName: String,
        private val topic: String,
        private val homework: String,
        private val previousHomework: String,
        private val startTime: String,
        private val endTime: String,
        private val date: Int,
        private val marks: List<PerformanceUi.Mark>,
        private val absence: List<String>,
        private val notes: List<String>
    ) : DiaryUi {

        override fun showTime(textView: TextView) {
            val text = "$startTime - $endTime"
            textView.text = text
        }

        override fun showName(textView: TextView) {
            textView.text = name
        }

        override fun showTeacherName(textView: TextView) {
            textView.text = teacherName
        }

        override fun showTopic(textView: TextView, title: TextView) {
            textView.text = topic
            title.visibility = if (topic.isEmpty()) View.GONE else View.VISIBLE
            textView.visibility = if (topic.isEmpty()) View.GONE else View.VISIBLE
        }

        override fun showHomework(textView: TextView, title: TextView) {
            textView.text = homework
            val visibility = if (homework.isEmpty()) View.GONE else View.VISIBLE
            title.visibility = visibility
            textView.visibility = visibility
        }

        override fun showPreviousHomework(textView: TextView, title: TextView) {
            textView.text = previousHomework
            val visibility = if (previousHomework.isEmpty()) View.GONE else View.VISIBLE
            title.visibility = visibility
            textView.visibility = visibility
        }

        override fun showMarks(linearLayout: LinearLayout) {
            linearLayout.visibility = if (marks.isEmpty() && absence.isEmpty()) View.GONE else View.VISIBLE
            if (linearLayout.childCount > 1)
                linearLayout.removeViews(1, linearLayout.childCount - 1)
            val layoutParams = LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT
            )
            layoutParams.marginEnd = 15
            absence.forEach {
                val textView = TextView(linearLayout.context)
                textView.layoutParams = layoutParams
                textView.text = it
                textView.setTextColor(textView.context.getColor(R.color.blue))
                linearLayout.addView(textView)
            }
            marks.forEach { grade ->
                val textView = TextView(linearLayout.context)
                textView.layoutParams = layoutParams
                grade.showName(textView)
                linearLayout.addView(textView)
            }
        }

        override fun showNotes(textView: TextView, title: TextView) {
            val visibility = if (notes.isEmpty()) View.GONE else View.VISIBLE
            title.visibility = visibility
            textView.visibility = visibility

            val sb = StringBuilder()
            notes.forEach {
                sb.append("$it\n")
            }
            textView.text = sb.trim()
        }

        override fun map(mapper: Mapper<Boolean>): Boolean =
            mapper.map(name, topic, homework, startTime, endTime, date, marks)

        override fun same(item: DiaryUi) = item is Lesson && item.date == date

        override fun sameContent(item: DiaryUi) =
            item is Lesson && item.name == name && item.topic == topic
                    && item.homework == homework && item.startTime == startTime && item.endTime == endTime
    }

    object Empty : DiaryUi {
        override fun same(item: DiaryUi) = item is Empty
        override fun sameContent(item: DiaryUi) = false
        override fun map(mapper: Mapper<Boolean>) = true
    }
}