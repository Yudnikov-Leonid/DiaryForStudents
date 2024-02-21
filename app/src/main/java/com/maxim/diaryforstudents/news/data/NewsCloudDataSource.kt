package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.core.presentation.ReloadWithError
import com.maxim.diaryforstudents.core.service.CloudNews
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.core.service.ServiceValueEventListener

interface NewsCloudDataSource {
    fun init(reload: ReloadWithError)
    fun data(status: Int): List<NewsData>

    class Base(
        private val service: Service
    ) : NewsCloudDataSource {
        private val news = mutableMapOf<Int, ArrayList<NewsData>>()
        override fun init(reload: ReloadWithError) {

            service.listen(
                "news",
                CloudNews::class.java,
                object : ServiceValueEventListener<CloudNews> {
                    override fun valueChanged(value: List<Pair<String, CloudNews>>) {
                        news.clear()
                        value.sortedByDescending { it.second.date }.forEach {
                            if (news[it.second.status] == null)
                                news[it.second.status] = ArrayList()
                            news[it.second.status]!!.add(
                                NewsData.Base(
                                    it.second.title,
                                    it.second.content,
                                    it.second.date,
                                    it.second.photoUrl
                                )
                            )
                        }
                        reload.reload()
                    }

                    override fun error(message: String) = reload.error(message)
                })
        }

        override fun data(status: Int): List<NewsData> = news[status] ?: emptyList()
    }
}