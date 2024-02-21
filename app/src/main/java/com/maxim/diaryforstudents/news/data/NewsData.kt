package com.maxim.diaryforstudents.news.data

interface NewsData {
    interface Mapper<T> {
        fun map(title: String, content: String, date: Long, photoUrl: String): T
        fun map(): T
        fun map(message: String): T
    }
    fun <T> map(mapper: Mapper<T>): T

    data class Base(
        private val title: String,
        private val content: String,
        private val date: Long,
        private val photoUrl: String
    ) : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(title, content, date, photoUrl)
    }

    object Empty : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Failure(private val message: String) : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(message)
    }
}