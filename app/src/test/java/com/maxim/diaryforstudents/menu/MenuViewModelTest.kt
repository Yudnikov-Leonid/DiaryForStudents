package com.maxim.diaryforstudents.menu

import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen
import org.junit.Before
import org.junit.Test

class MenuViewModelTest {
    private lateinit var navigation: FakeNavigation
    private lateinit var viewModel: MenuViewModel
    @Before
    fun before() {
        navigation = FakeNavigation(Order())
        viewModel = MenuViewModel(navigation)
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
}