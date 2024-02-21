package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.presentation.ReloadWithError

interface NewsRepository {
    fun mainNews(): NewsData
    fun importantNews(): List<NewsData>
    fun defaultNews(): List<NewsData>
    fun init(reload: ReloadWithError)

    fun checkNewNews(): Int

    class Base(
        private val dataSource: NewsCloudDataSource,
        private val simpleStorage: SimpleStorage
    ) : NewsRepository {
        override fun mainNews(): NewsData = try {
            dataSource.data(0).first()
        } catch (e: Exception) {
            NewsData.Empty
        }

        override fun importantNews() = try {
            val list = dataSource.data(1)
            list.ifEmpty { listOf(NewsData.Empty) }
        } catch (e: Exception) {
            listOf(NewsData.Failure(e.message ?: "error"))
        }

        override fun defaultNews() = try {
            val list = dataSource.data(0)
            list.ifEmpty { listOf(NewsData.Empty) }
        } catch (e: Exception) {
            listOf(NewsData.Failure(e.message ?: "error"))
        }

        override fun init(reload: ReloadWithError) {
            simpleStorage.save(LAST_CHECK, System.currentTimeMillis())
            dataSource.init(reload)
        }

        override fun checkNewNews() = dataSource.checkNewNews(simpleStorage.read(LAST_CHECK, 0L))

        companion object {
            private const val LAST_CHECK = "news_last_check"
        }
    }
}