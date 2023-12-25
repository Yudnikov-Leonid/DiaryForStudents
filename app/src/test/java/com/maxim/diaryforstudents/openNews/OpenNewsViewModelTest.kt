package com.maxim.diaryforstudents.openNews

import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.news.presentation.NewsUi
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class OpenNewsViewModelTest {
    private lateinit var openNewsData: FakeOpenNewsData
    private lateinit var viewModel: OpenNewsViewModel
    private lateinit var clear: FakeClearViewModel
    private lateinit var navigation: FakeNavigation
    private lateinit var order: Order

    @Before
    fun before() {
        order = Order()
        openNewsData = FakeOpenNewsData()
        clear = FakeClearViewModel(order)
        navigation = FakeNavigation(order)
        viewModel = OpenNewsViewModel(openNewsData, navigation, clear)
    }

    @Test
    fun test_data() {
        val actual = viewModel.data()
        assertEquals(NewsUi.Base("Test title", "Test content", 55, ""), actual)
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(OpenNewsViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }
}

private class FakeOpenNewsData: OpenNewsData.Read {
    override fun read(): NewsUi {
        return NewsUi.Base("Test title", "Test content", 55, "")
    }
}