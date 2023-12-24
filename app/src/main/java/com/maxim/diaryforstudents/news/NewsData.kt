package com.maxim.diaryforstudents.news

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

    data class Failure(private val message: String): NewsData {
        override fun toUi() = NewsUi.Failure(message)
    }
}