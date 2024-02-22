package com.maxim.diaryforstudents.news.presentation

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface NewsState : Serializable {
    fun show(
        mainNewsLayout: View,
        mainNewsImageView: ImageView,
        mainNewsTitleTextView: TextView,
        mainNewsContentTextView: TextView,
        mainNewsTimeTextView: TextView,
        importantAdapter: NewsAdapter,
        defaultAdapter: NewsAdapter,
        listener: NewsAdapter.Listener,
        progressBar: ProgressBar
    )

    data class Error(
        private val message: String
    ): NewsState {
        override fun show(
            mainNewsLayout: View,
            mainNewsImageView: ImageView,
            mainNewsTitleTextView: TextView,
            mainNewsContentTextView: TextView,
            mainNewsTimeTextView: TextView,
            importantAdapter: NewsAdapter,
            defaultAdapter: NewsAdapter,
            listener: NewsAdapter.Listener,
            progressBar: ProgressBar
        ) {
            TODO("Not yet implemented")
        }
    }

    data class Base(
        private val mainNews: NewsUi,
        private val importantNews: List<NewsUi>,
        private val defaultNews: List<NewsUi>,
    ) : NewsState {
        override fun show(
            mainNewsLayout: View,
            mainNewsImageView: ImageView,
            mainNewsTitleTextView: TextView,
            mainNewsContentTextView: TextView,
            mainNewsTimeTextView: TextView,
            importantAdapter: NewsAdapter,
            defaultAdapter: NewsAdapter,
            listener: NewsAdapter.Listener,
            progressBar: ProgressBar
        ) {
            mainNews.showFitImage(mainNewsImageView)
            mainNews.showTitle(mainNewsTitleTextView)
            mainNews.showContent(mainNewsContentTextView)
            mainNews.showTime(mainNewsTimeTextView)
            mainNewsLayout.setOnClickListener {
                listener.open(mainNews)
            }
            progressBar.visibility = View.GONE
            importantAdapter.update(importantNews)
            defaultAdapter.update(defaultNews)
        }
    }

    object Loading : NewsState {
        override fun show(
            mainNewsLayout: View,
            mainNewsImageView: ImageView,
            mainNewsTitleTextView: TextView,
            mainNewsContentTextView: TextView,
            mainNewsTimeTextView: TextView,
            importantAdapter: NewsAdapter,
            defaultAdapter: NewsAdapter,
            listener: NewsAdapter.Listener,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.VISIBLE
        }
    }
}