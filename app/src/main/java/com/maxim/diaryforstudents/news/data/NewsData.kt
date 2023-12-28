package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.news.presentation.NewsUi

interface NewsData {
    fun toUi(): NewsUi
    data class Base(
        val title: String = "",
        val content: String = "",
        val date: Int = 0,
        val photoUrl: String = ""
    ) : NewsData {
        override fun toUi() = NewsUi.Base(title, content, date, photoUrl)
    }
    object Empty: NewsData {
        override fun toUi() = NewsUi.Empty
    }

    data class Failure(private val message: String): NewsData {
        override fun toUi() = NewsUi.Failure(message)
    }
}