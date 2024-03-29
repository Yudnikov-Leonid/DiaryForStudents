package com.maxim.diaryforstudents.menu.presentation

import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import java.io.Serializable

interface MenuState: Serializable {
    fun showNewsCount(textView: TextView)
    fun showMarksCount(textView: TextView)
    fun showLessons(viewPager: ViewPager2, adapter: MenuLessonsAdapter)

    data class Initial(
        private val newMarksCount: Int,
        private val newNewsCount: Int,
        private val lessons: List<DiaryUi.Lesson>,
        private val currentLesson: Int,
    ): MenuState {
        override fun showNewsCount(textView: TextView) {
            textView.visibility = if (newNewsCount == 0) View.GONE else View.VISIBLE
            textView.text = newNewsCount.toString()
        }

        override fun showMarksCount(textView: TextView) {
            textView.visibility = if (newMarksCount == 0) View.GONE else View.VISIBLE
            textView.text = newMarksCount.toString()
        }

        override fun showLessons(viewPager: ViewPager2, adapter: MenuLessonsAdapter) {
            viewPager.visibility = if (lessons.isNotEmpty()) View.VISIBLE else View.GONE
            adapter.update(lessons)
            viewPager.setCurrentItem(currentLesson, false)
        }
    }
}