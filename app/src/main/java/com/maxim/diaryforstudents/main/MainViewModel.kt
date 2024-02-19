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

class MainViewModel(
    private val interactor: MainInteractor,
    private val performanceInteractor: PerformanceInteractor,
    private val navigation: Navigation.Mutable,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<Screen>, Init, SaveAndRestore {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun && interactor.isLogged()) {
            handle { performanceInteractor.loadData() }
            navigation.update(MenuScreen)
        }
        else if (isFirstRun)
            navigation.update(LoginScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: androidx.lifecycle.Observer<Screen>) {
        navigation.observe(owner, observer)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        performanceInteractor.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        performanceInteractor.restore(bundleWrapper)
    }
}