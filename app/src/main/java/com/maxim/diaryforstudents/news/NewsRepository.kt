package com.maxim.diaryforstudents.news

interface NewsRepository {
    fun data(): List<NewsData>
    fun init(reload: Reload)

    class Base(private val dataSource: NewsCloudDataSource) : NewsRepository {
        override fun data() = try {
            val list = dataSource.data()
            list
        } catch (e: Exception) {
            listOf(NewsData.Failure(e.message ?: "error"))
        }

        override fun init(reload: Reload) = dataSource.init(reload)
    }
}