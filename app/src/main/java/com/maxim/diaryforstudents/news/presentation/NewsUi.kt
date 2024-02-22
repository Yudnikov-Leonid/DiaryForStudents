package com.maxim.diaryforstudents.news.presentation

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.openNews.Share
import com.squareup.picasso.Picasso
import java.io.Serializable

abstract class NewsUi: Serializable {
    open fun showTitle(textView: TextView) {}
    open fun showTime(textView: TextView) {}
    open fun showDate(textView: TextView) {}
    open fun showContent(textView: TextView) {}
    open fun showImage(imageView: ImageView) {}
    open fun showFitImage(imageView: ImageView) {}
    abstract fun same(item: NewsUi): Boolean
    open fun sameContent(item: NewsUi): Boolean = false

    open fun share(share: Share) {}

    data class Base(
        private val title: String,
        private val content: String,
        private val date: Long,
        private val photoUrl: String
    ) : NewsUi() {
        override fun showTitle(textView: TextView) {
            textView.text = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY)
        }

        override fun showDate(textView: TextView) {
            textView.text = Formatter.Base.day((date / 86400000).toInt())
        }

        override fun share(share: Share) {
            share.share("$title\n\n${Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)}")
        }

        override fun showTime(textView: TextView) {
            val timePassed = System.currentTimeMillis() - date
            val text = if (timePassed / 2_592_000_000 > 0) "${timePassed / 2_592_000_000} months ago"
            else if (timePassed / 604_800_000 > 0) "${timePassed / 604_800_000} weeks ago"
            else if (timePassed / 86_400_000 > 0) "${timePassed / 86_400_000} days ago"
            else if (timePassed / 3_600_000 > 0) "${timePassed / 3_600_000} hours ago"
            else if (timePassed / 60000 > 0) "${timePassed / 60000} minutes ago"
            else "Just once"
            textView.text = text
        }

        override fun showContent(textView: TextView) {
            val text = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
            textView.text = text
        }

        override fun showImage(imageView: ImageView) {
            if (photoUrl.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                Picasso.get().load(photoUrl).into(imageView)
            } else
                imageView.visibility = View.GONE
        }

        override fun showFitImage(imageView: ImageView) {
            if (photoUrl.isNotEmpty()) {
                imageView.visibility = View.VISIBLE
                Picasso.get().load(photoUrl).fit().into(imageView)
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