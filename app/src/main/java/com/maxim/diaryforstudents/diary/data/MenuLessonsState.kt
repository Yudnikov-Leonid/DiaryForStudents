package com.maxim.diaryforstudents.diary.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.maxim.diaryforstudents.R
import java.io.Serializable

abstract class MenuLessonState: Serializable {
    open fun show(indicator: ImageView, status: TextView) {
        status.visibility = View.VISIBLE
        indicator.visibility = View.VISIBLE
    }

    object IsGoingOnNow: MenuLessonState() {
        override fun show(indicator: ImageView, status: TextView) {
            super.show(indicator, status)
            status.text = status.context.getString(R.string.is_going_on_now)
            indicator.setImageResource(android.R.drawable.presence_online)
        }
    }

    object Passed: MenuLessonState() {
        override fun show(indicator: ImageView, status: TextView) {
            super.show(indicator, status)
            status.text = status.context.getString(R.string.lesson_passed)
            indicator.setImageResource(R.drawable.done_24)
        }
    }

    object Break: MenuLessonState() {
        override fun show(indicator: ImageView, status: TextView) {
            super.show(indicator, status)
            status.text = status.context.getString(R.string.lesson_starts_soon)
            indicator.setImageResource(R.drawable.wait_24)
        }
    }

    object Next: MenuLessonState() {
        override fun show(indicator: ImageView, status: TextView) {
            status.visibility = View.VISIBLE
            indicator.visibility = View.GONE
            status.text = status.context.getString(R.string.next_lesson)
        }
    }

    object Default: MenuLessonState() {
        override fun show(indicator: ImageView, status: TextView) {
            status.visibility = View.GONE
            indicator.visibility = View.GONE
        }
    }
}