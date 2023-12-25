package com.maxim.diaryforstudents.news.presentation

import android.view.View
import android.widget.ProgressBar

interface NewsState {
    fun show(adapter: NewsAdapter, progressBar: ProgressBar)
    data class Base(private val list: List<NewsUi>): NewsState {
        override fun show(adapter: NewsAdapter, progressBar: ProgressBar) {
            progressBar.visibility = View.GONE
            adapter.update(list)
        }
    }

    object Loading: NewsState {
        override fun show(adapter: NewsAdapter, progressBar: ProgressBar) {
            progressBar.visibility = View.VISIBLE
        }
    }
}