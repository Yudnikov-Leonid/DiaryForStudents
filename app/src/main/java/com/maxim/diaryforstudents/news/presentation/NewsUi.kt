package com.maxim.diaryforstudents.news.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar

abstract class NewsUi {
    open fun showTitle(textView: TextView) {}
    open fun showDate(textView: TextView) {}
    open fun showContent(textView: TextView) {}
    open fun showImage(imageView: ImageView) {}
    abstract fun same(item: NewsUi): Boolean
    open fun sameContent(item: NewsUi): Boolean = false
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
            val formatter = SimpleDateFormat.getDateInstance()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            textView.text = formatter.format(calendar.time)
        }

        override fun showContent(textView: TextView) {
            textView.text = content
        }

        override fun showImage(imageView: ImageView) {
            if (photoUrl.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                Picasso.get().load(photoUrl).into(imageView)
            } else
                imageView.visibility = View.GONE
        }

        override fun same(item: NewsUi) =
            item is Base && item.title == title

        override fun sameContent(item: NewsUi) =
            item is Base && item.content == content && item.date == date && item.photoUrl == photoUrl
    }

    object Empty : NewsUi() {
        override fun same(item: NewsUi) = item is Empty
    }

    data class Failure(private val message: String) : NewsUi() {
        override fun showTitle(textView: TextView) {
            textView.text = message
        }

        override fun same(item: NewsUi) =
            item is Failure

        override fun sameContent(item: NewsUi) =
            item is Failure && item.message == message
    }
}