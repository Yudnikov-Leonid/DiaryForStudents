package com.maxim.diaryforstudents.menu.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassScreen
import com.maxim.diaryforstudents.menu.domain.MenuInteractor
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen

class MenuViewModel(
    private val interactor: MenuInteractor,
    private val communication: MenuCommunication,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<MenuState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(MenuState.Loading)
            handle({ interactor.userStatus() }) {
                communication.update(it.mapToState())
            }
        }
    }

    fun diary() {
        navigation.update(DiaryScreen)
    }

    fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    fun diaryForTeacher() {
        navigation.update(SelectClassScreen)
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

    companion object {
        private const val RESTORE_KEY = "menu_restore"
    }
}