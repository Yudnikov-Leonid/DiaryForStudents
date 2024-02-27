package com.maxim.diaryforstudents.openNews

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.news.presentation.NewsUi
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

class OpenNewsViewModelTest {
    private lateinit var openNewsData: FakeOpenNewsData
    private lateinit var viewModel: OpenNewsViewModel
    private lateinit var clear: FakeClearViewModel
    private lateinit var navigation: FakeNavigation
    private lateinit var order: Order
    private lateinit var communication: FakeOpenNewsCommunication

    @Before
    fun before() {
        order = Order()
        openNewsData = FakeOpenNewsData()
        clear = FakeClearViewModel(order)
        navigation = FakeNavigation(order)
        communication = FakeOpenNewsCommunication()
        viewModel = OpenNewsViewModel(communication, openNewsData, navigation, clear)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsUi.Base("Test title", "Test content", 55, ""))
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(OpenNewsViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        openNewsData.checkSaveCalledTimes(1)
        communication.checkCalledTimes(0)

        viewModel.restore(bundleWrapper)
        openNewsData.checkRestoreCalledTimes(1)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsUi.Base("Test title", "Test content", 55, ""))
    }
}

private class FakeOpenNewsCommunication: OpenNewsCommunication {
    private val list = mutableListOf<NewsUi>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: NewsUi) {
        assertEquals(expected, list.last())
    }

    override fun update(value: NewsUi) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsUi>) {
        throw IllegalStateException("not using in tests")
    }
}

private class FakeOpenNewsData : OpenNewsStorage.Read {
    override fun read(): NewsUi {
        return NewsUi.Base("Test title", "Test content", 55, "")
    }

    private lateinit var bundleWrapper: BundleWrapper.Mutable
    private var saveCounter = 0
    private var restoreCounter = 0

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        assertEquals(bundleWrapper, this.bundleWrapper)
        restoreCounter++
    }
}