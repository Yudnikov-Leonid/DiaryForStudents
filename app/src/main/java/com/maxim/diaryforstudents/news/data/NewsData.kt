package com.maxim.diaryforstudents.news.data

interface NewsData {
    interface Mapper<T> {
        fun map(title: String, content: String, date: Long, photoUrl: String, isMain: Boolean): T
        fun map(): T
        fun map(message: String): T
    }
    fun <T> map(mapper: Mapper<T>): T

    data class Main(
        private val title: String,
        private val content: String,
        private val date: Long,
        private val photoUrl: String
    ): NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(title, content, date, photoUrl, true)
    }

    data class Base(
        private val title: String,
        private val content: String,
        private val date: Long,
        private val photoUrl: String
    ) : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(title, content, date, photoUrl, false)
    }

    object Empty : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Failure(private val message: String) : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(message)
    }
}