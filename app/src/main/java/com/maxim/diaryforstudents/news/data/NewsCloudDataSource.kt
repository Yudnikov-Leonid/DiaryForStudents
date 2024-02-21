package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.core.presentation.ReloadWithError
import com.maxim.diaryforstudents.core.service.CloudNews
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener

interface NewsCloudDataSource {
    fun init(reload: ReloadWithError)
    fun data(): List<NewsData>

    class Base(
        private val service: Service
    ) : NewsCloudDataSource {
        private val news = mutableListOf<NewsData>()
        override fun init(reload: ReloadWithError) {
            service.listen(
                "news",
                CloudNews::class.java,
                object : ServiceValueEventListener<CloudNews> {
                    override fun valueChanged(value: List<Pair<String, CloudNews>>) {
                        news.clear()
                        val sorted = value.map { it.second }.sortedByDescending { it.date }
                        val mainNews = sorted.first()
                        news.add(NewsData.Main(mainNews.title, mainNews.content, mainNews.date, mainNews.photoUrl))
                        news.addAll(sorted.subList(1, sorted.size).map { NewsData.Base(it.title, it.content, it.date, it.photoUrl) })
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                })
        }

        override fun data() = news
    }
}