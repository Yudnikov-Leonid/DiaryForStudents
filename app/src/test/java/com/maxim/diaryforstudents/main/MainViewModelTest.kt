package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakePerformanceInteractor
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var viewModel: MainViewModel
    private lateinit var navigation: FakeNavigation
    private lateinit var interactor: FakeMainInteractor
    private lateinit var performanceInteractor: FakePerformanceInteractor
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun init() {
        navigation = FakeNavigation(Order())
        interactor = FakeMainInteractor()
        performanceInteractor = FakePerformanceInteractor()
        runAsync = FakeRunAsync()
        viewModel = MainViewModel(interactor, performanceInteractor, navigation, runAsync)
    }

    @Test
    fun test_init_logged() {
        interactor.mustReturn(true)

        viewModel.init(true)
        performanceInteractor.checkLoadDataCalledTimes(1)
        navigation.checkCalledWith(MenuScreen)
        navigation.checkCalledTimes(1)

        viewModel.init(false)
        performanceInteractor.checkLoadDataCalledTimes(1)
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

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        performanceInteractor.checkSaveCalledTimes(1)
        viewModel.restore(bundleWrapper)
        performanceInteractor.checkRestoreCalledTimes(1)
    }
}

private class FakeMainInteractor: MainInteractor {
    private var value = true

    fun mustReturn(value: Boolean) {
        this.value = value
    }

    override fun isLogged() = value
}