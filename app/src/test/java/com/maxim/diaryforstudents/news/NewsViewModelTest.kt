package com.maxim.diaryforstudents.news

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.OPEN_NEWS_DATA
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.news.data.NewsData
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsCommunication
import com.maxim.diaryforstudents.news.presentation.NewsState
import com.maxim.diaryforstudents.news.presentation.NewsUi
import com.maxim.diaryforstudents.news.presentation.NewsViewModel
import com.maxim.diaryforstudents.openNews.OpenNewsData
import com.maxim.diaryforstudents.openNews.OpenNewsScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class NewsViewModelTest {
    private lateinit var viewModel: NewsViewModel
    private lateinit var repository: FakeNewsRepository
    private lateinit var communication: FakeNewsCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var openNewsData: FakeOpenNewsData

    @Before
    fun before() {
        order = Order()
        repository = FakeNewsRepository()
        communication = FakeNewsCommunication()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        openNewsData = FakeOpenNewsData(order)
        viewModel = NewsViewModel(repository, communication, navigation, clear, openNewsData)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        repository.checkCalledTimes(1)
        repository.checkCalledWith(viewModel)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsState.Loading)

        viewModel.init(false)
        repository.checkCalledTimes(1)
        communication.checkCalledTimes(1)
    }

    @Test
    fun test_reload() {
        repository.expected(
            listOf(
                NewsData.Base("Title", "Content", 55, "url1"),
                NewsData.Base("Title1", "Content1", 56, "url2")
            )
        )
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            NewsState.Base(
                listOf(
                    NewsUi.Base("Title", "Content", 55, "url1"),
                    NewsUi.Base("Title1", "Content1", 56, "url2")
                )
            )
        )
    }

    @Test
    fun test_error() {
        viewModel.error("message")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsState.Base(listOf(NewsUi.Failure("message"))))
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(NewsViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_open() {
        val news = NewsUi.Base("Title", "Content", 55, "")
        viewModel.open(news)
        openNewsData.checkCalledWith(news)
        navigation.checkCalledWith(OpenNewsScreen)
        order.check(listOf(OPEN_NEWS_DATA, NAVIGATION))
    }
}

private class FakeOpenNewsData(private val order: Order) : OpenNewsData.Save {
    private lateinit var value: NewsUi
    fun checkCalledWith(expected: NewsUi) {
        assertEquals(expected, value)
    }

    override fun save(value: NewsUi) {
        this.value = value
        order.add(OPEN_NEWS_DATA)
    }
}

private class FakeNewsCommunication : NewsCommunication.Mutable {
    private val list = mutableListOf<NewsState>()
    override fun update(value: NewsState) {
        list.add(value)
    }

    fun checkCalledWith(expected: NewsState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsState>) {
        throw IllegalStateException("not using in test")
    }
}

private class FakeNewsRepository : NewsRepository {
    private var counter = 0
    private lateinit var reload: Reload
    private val data = mutableListOf<NewsData>()
    override fun data() = data

    fun expected(list: List<NewsData>) {
        data.addAll(list)
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    fun checkCalledWith(expected: Reload) {
        assertEquals(expected, reload)
    }

    override fun init(reload: Reload) {
        counter++
        this.reload = reload
    }
}