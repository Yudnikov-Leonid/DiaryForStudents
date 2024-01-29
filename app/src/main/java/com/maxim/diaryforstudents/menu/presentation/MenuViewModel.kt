package com.maxim.diaryforstudents.menu.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen

class MenuViewModel(
    private val communication: MenuCommunication,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<MenuState>, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(MenuState.Initial)
        }
    }

    fun diary() {
        navigation.update(DiaryScreen)
    }

    fun performance() {
        navigation.update(PerformanceScreen)
    }

    fun profile() {
        navigation.update(ProfileScreen)
    }

    fun news() {
        navigation.update(NewsScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MenuState>) {
        communication.observe(owner, observer)
    }
}