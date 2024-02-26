package com.maxim.diaryforstudents.settings

import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.settings.presentation.SettingsViewModel
import com.maxim.diaryforstudents.settings.themes.SettingsThemesScreen
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var order: Order
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel

    @Before
    fun setUp() {
        order = Order()
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        viewModel = SettingsViewModel(navigation, clearViewModel)
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(SettingsViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_themes() {
        viewModel.themes()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(SettingsThemesScreen)
    }
}