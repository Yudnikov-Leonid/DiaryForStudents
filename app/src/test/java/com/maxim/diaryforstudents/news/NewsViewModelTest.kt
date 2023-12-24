package com.maxim.diaryforstudents.news

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
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

    @Before
    fun before() {
        order = Order()
        repository = FakeNewsRepository()
        communication = FakeNewsCommunication()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        viewModel = NewsViewModel(repository, communication, navigation, clear)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        repository.checkCalledTimes(1)
        repository.checkCalledWith(viewModel)

        viewModel.init(false)
        repository.checkCalledTimes(1)
    }

    @Test
    fun test_reload() {
        repository.expected(
            listOf(
                NewsData.Base("Title", "Content"),
                NewsData.Base("Title1", "Content1")
            )
        )
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            NewsState.Base(
                listOf(NewsUi.Base("Title", "Content", 0, ""),
                NewsUi.Base("Title1", "Content1", 0, ""))
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

private class FakeNewsRepository(): NewsRepository {
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