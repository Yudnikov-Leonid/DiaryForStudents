package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.service.CloudNews
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener

interface NewsCloudDataSource {
    fun init(reload: Reload)
    fun data(): List<NewsData>

    class Base(
        private val service: Service
    ) : NewsCloudDataSource {
        private val news = mutableListOf<NewsData>()
        override fun init(reload: Reload) {
            service.listen(
                "news",
                CloudNews::class.java,
                object : ServiceValueEventListener<CloudNews> {
                    override fun valueChanged(value: List<Pair<String, CloudNews>>) {
                        news.clear()
                        news.addAll(value.map { it.second }.sortedBy { it.date }
                            .map { NewsData.Base(it.title, it.content, it.date, it.photoUrl) })
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                })
        }

        override fun data() = news
    }
}