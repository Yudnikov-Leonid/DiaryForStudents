package com.maxim.diaryforstudents

import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.main.MainViewModel
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var navigation: FakeNavigation

    @Before
    fun init() {
        navigation = FakeNavigation()
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