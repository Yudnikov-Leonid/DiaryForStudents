package com.maxim.diaryforstudents.menu

import com.maxim.diaryforstudents.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.FakePerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen
import org.junit.Before
import org.junit.Test

class MenuViewModelTest {
    private lateinit var navigation: FakeNavigation
    private lateinit var performanceInteractor: FakePerformanceInteractor
    private lateinit var viewModel: MenuViewModel
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun before() {
        navigation = FakeNavigation(Order())
        performanceInteractor = FakePerformanceInteractor()
        runAsync = FakeRunAsync()
        viewModel = MenuViewModel(performanceInteractor, navigation, runAsync)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        performanceInteractor.checkLoadDataCalledTimes(1)

        viewModel.init(false)
        performanceInteractor.checkLoadDataCalledTimes(1)
    }

    @Test
    fun test_diary() {
        viewModel.diary()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(DiaryScreen)
    }

    @Test
    fun test_performance() {
        viewModel.performance()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(PerformanceScreen)
    }

    @Test
    fun test_profile() {
        viewModel.profile()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(ProfileScreen)
    }

    @Test
    fun test_news() {
        viewModel.news()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(NewsScreen)
    }

    @Test
    fun test_analytics() {
        viewModel.analytics()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(AnalyticsScreen)
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        performanceInteractor.checkSaveCalledTimes(1)
        viewModel.restore(bundleWrapper)
        performanceInteractor.checkRestoreCalledTimes(1)
    }
}