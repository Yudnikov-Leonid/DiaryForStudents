package com.maxim.diaryforstudents.main

import androidx.lifecycle.LifecycleOwner
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val interactor: MainInteractor,
    private val navigation: Navigation.Mutable,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, Communication.Observe<Screen> {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun && interactor.isLogged())
            navigation.update(MenuScreen)
        else if (isFirstRun)
            navigation.update(LoginScreen)
    }

    override fun state() = navigation.state()
}