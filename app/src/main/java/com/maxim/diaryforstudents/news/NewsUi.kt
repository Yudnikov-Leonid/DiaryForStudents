package com.maxim.diaryforstudents.news

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar

abstract class NewsUi {
    open fun showTitle(textView: TextView) {}
    open fun showDate(textView: TextView) {}
    abstract fun same(item: NewsUi): Boolean
    abstract fun sameContent(item: NewsUi): Boolean
    data class Base(
        private val title: String,
        private val content: String,
        private val date: Int,
        private val photoUrl: String
    ) : NewsUi() {
        override fun showTitle(textView: TextView) {
            textView.text = title
        }
        override fun showDate(textView: TextView) {
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            textView.text = formatter.format(calendar.time)
        }

        override fun same(item: NewsUi) =
            item is Base && item.title == title

        override fun sameContent(item: NewsUi) =
            item is Base && item.content == content && item.date == date && item.photoUrl == photoUrl
    }

    data class Failure(private val message: String): NewsUi() {
        override fun showTitle(textView: TextView) {
            textView.text = message
        }

        override fun same(item: NewsUi) =
            item is Failure

        override fun sameContent(item: NewsUi) =
            item is Failure && item.message == message
    }
}