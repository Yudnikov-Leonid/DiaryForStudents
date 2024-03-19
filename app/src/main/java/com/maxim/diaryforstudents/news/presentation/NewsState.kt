package com.maxim.diaryforstudents.news.presentation

import android.view.View
import android.widget.Button
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
    ) {}

    fun show(
        errorTextView: TextView,
        retryButton: Button,
        mainNewsLayout: View,
        importantNewsLayout: View,
        defaultNewsLayout: View,
        progressBar: ProgressBar
    )

    data class Error(
        private val message: String
    ): NewsState {

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            mainNewsLayout: View,
            importantNewsLayout: View,
            defaultNewsLayout: View,
            progressBar: ProgressBar
        ) {
            errorTextView.text = message
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            mainNewsLayout.visibility = View.GONE
            importantNewsLayout.visibility = View.GONE
            defaultNewsLayout.visibility = View.GONE
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
        ) {
            mainNews.showFitImage(mainNewsImageView)
            mainNews.showTitle(mainNewsTitleTextView)
            mainNews.showContent(mainNewsContentTextView)
            mainNews.showTime(mainNewsTimeTextView)
            mainNewsLayout.setOnClickListener {
                listener.open(mainNews)
            }
            importantAdapter.update(importantNews)
            defaultAdapter.update(defaultNews)
        }

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            mainNewsLayout: View,
            importantNewsLayout: View,
            defaultNewsLayout: View,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            mainNewsLayout.visibility = View.VISIBLE
            importantNewsLayout.visibility = View.VISIBLE
            defaultNewsLayout.visibility = View.VISIBLE
        }
    }

    object Loading : NewsState {

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            mainNewsLayout: View,
            importantNewsLayout: View,
            defaultNewsLayout: View,
            progressBar: ProgressBar
        ) {
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            mainNewsLayout.visibility = View.GONE
            importantNewsLayout.visibility = View.GONE
            defaultNewsLayout.visibility = View.GONE
        }
    }

    object Empty: NewsState {
        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            mainNewsLayout: View,
            importantNewsLayout: View,
            defaultNewsLayout: View,
            progressBar: ProgressBar
        ) = Unit
    }
}