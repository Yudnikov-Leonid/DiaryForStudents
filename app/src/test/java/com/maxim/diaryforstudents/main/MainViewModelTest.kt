package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var navigation: FakeNavigation
    private lateinit var interactor: FakeMainInteractor

    @Before
    fun init() {
        navigation = FakeNavigation(Order())
        interactor = FakeMainInteractor()
        viewModel = MainViewModel(interactor, navigation)
    }

    @Test
    fun test_init_logged() {
        interactor.mustReturn(true)

        viewModel.init(true)
        navigation.checkCalledWith(MenuScreen)
        navigation.checkCalledTimes(1)

        viewModel.init(false)
        navigation.checkCalledTimes(1)
    }

    @Test
    fun test_init_not_logged() {
        interactor.mustReturn(false)

        viewModel.init(true)
        navigation.checkCalledWith(LoginScreen)
        navigation.checkCalledTimes(1)

        viewModel.init(false)
        navigation.checkCalledTimes(1)
    }
}

private class FakeMainInteractor: MainInteractor {
    private var value = true

    fun mustReturn(value: Boolean) {
        this.value = value
    }

    override fun isLogged() = value
}