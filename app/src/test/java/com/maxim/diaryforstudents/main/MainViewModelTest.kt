package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var navigation: FakeNavigation

    @Before
    fun init() {
        navigation = FakeNavigation(Order())
        viewModel = MainViewModel(navigation)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        navigation.checkCalledWith(LoginScreen)
        navigation.checkCalledTimes(1)

        viewModel.init(false)
        navigation.checkCalledTimes(1)
    }
}