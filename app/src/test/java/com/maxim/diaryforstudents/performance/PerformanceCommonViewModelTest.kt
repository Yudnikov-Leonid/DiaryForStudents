package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.MARKS_MODULE
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommonViewModel
import com.maxim.diaryforstudents.performance.common.sl.MarksModule
import com.maxim.diaryforstudents.performance.finalMarks.PerformanceFinalViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PerformanceCommonViewModelTest {
    private lateinit var marksModule: FakeMarksModule
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var viewModel: PerformanceCommonViewModel
    private lateinit var order: Order

    @Before
    fun setUp() {
        order = Order()
        marksModule = FakeMarksModule(order)
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        viewModel = PerformanceCommonViewModel(marksModule, navigation, clearViewModel)
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        marksModule.checkClearCalledTimes(1)
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(
            listOf(
                PerformanceActualViewModel::class.java,
                PerformanceFinalViewModel::class.java,
                PerformanceCommonViewModel::class.java,
            )
        )
        order.check(listOf(NAVIGATION, CLEAR, CLEAR, CLEAR, MARKS_MODULE))
    }
}

private class FakeMarksModule(private val order: Order): MarksModule.Clear {
    private var counter = 0

    fun checkClearCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    override fun clear() {
        order.add(MARKS_MODULE)
        counter++
    }
}