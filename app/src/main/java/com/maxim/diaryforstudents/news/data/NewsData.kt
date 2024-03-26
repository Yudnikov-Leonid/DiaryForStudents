package com.maxim.diaryforstudents.news.data

interface NewsData {
    interface Mapper<T> {
        fun map(
            title: String,
            content: String,
            date: Long,
            photoUrl: String,
            downloadUrl: String,
            fileName: String
        ): T

        fun map(): T
        fun map(message: String): T
    }

    fun <T> map(mapper: Mapper<T>): T

    fun hasChecked(lastCheck: Long): Boolean = true
    fun latest(item: NewsData): NewsData = Empty

    data class Base(
        private val title: String,
        private val content: String,
        private val date: Long,
        private val photoUrl: String,
        private val downloadUrl: String,
        private val fileName: String
    ) : NewsData {
        override fun <T> map(mapper: Mapper<T>) =
            mapper.map(title, content, date, photoUrl, downloadUrl, fileName)

        override fun hasChecked(lastCheck: Long) = date > lastCheck

        override fun latest(item: NewsData): NewsData {
            return if (item is Base && item.date > date) item else this
        }
    }

    object Empty : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map()
    }

    data class Failure(private val message: String) : NewsData {
        override fun <T> map(mapper: Mapper<T>) = mapper.map(message)
    }
}