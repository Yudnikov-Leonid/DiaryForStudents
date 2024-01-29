package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.core.presentation.ReloadWithError

interface NewsRepository {
    fun data(): List<NewsData>
    fun init(reload: ReloadWithError)

    class Base(private val dataSource: NewsCloudDataSource) : NewsRepository {
        override fun data() = try {
            val list = dataSource.data()
            list.ifEmpty { listOf(NewsData.Empty) }
        } catch (e: Exception) {
            listOf(NewsData.Failure(e.message ?: "error"))
        }

        override fun init(reload: ReloadWithError) = dataSource.init(reload)
    }
}