package com.maxim.diaryforstudents.news

import com.maxim.diaryforstudents.core.presentation.ReloadWithError
import com.maxim.diaryforstudents.news.data.NewsData
import com.maxim.diaryforstudents.news.data.NewsRepository
import junit.framework.TestCase

class FakeNewsRepository : NewsRepository {
    private var counter = 0
    private lateinit var reload: ReloadWithError
    private var mainNews: NewsData? = null
    private val importantNews = mutableListOf<NewsData>()
    private val defaultNews = mutableListOf<NewsData>()

    override fun mainNews() = mainNews!!

    override fun importantNews() = importantNews

    override fun defaultNews() = defaultNews

    fun expected(mainNews: NewsData, importantNews: List<NewsData>, defaultNews: List<NewsData>) {
        this.mainNews = mainNews
        this.importantNews.addAll(importantNews)
        this.defaultNews.addAll(defaultNews)
    }

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, counter)
    }

    fun checkCalledWith(expected: ReloadWithError) {
        TestCase.assertEquals(expected, reload)
    }

    override fun init(reload: ReloadWithError) {
        counter++
        this.reload = reload
    }

    private var checkNewNewsValue = 0

    fun checkNewNewsMustReturn(value: Int) {
        checkNewNewsValue = value
    }
    override fun checkNewNews() = checkNewNewsValue
}