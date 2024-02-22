package com.maxim.diaryforstudents.menu.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.ReloadWithError
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen

class MenuViewModel(
    private val communication: MenuCommunication,
    private val performanceInteractor: PerformanceInteractor,
    private val newsRepository: NewsRepository,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, SaveAndRestore, Communication.Observe<MenuState>, ReloadWithError {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            handle {
                //performanceInteractor.loadData()
                newsRepository.init(this)
            }
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
        newsRepository.checkNews()
        navigation.update(NewsScreen)
    }

    fun analytics() {
        navigation.update(AnalyticsScreen)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        performanceInteractor.save(bundleWrapper)
        communication.save(RESTORE_KEY, bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        performanceInteractor.restore(bundleWrapper)
        communication.restore(RESTORE_KEY, bundleWrapper)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MenuState>) {
        communication.observe(owner, observer)
    }

    override fun error(message: String) = Unit

    override fun reload() {
        communication.update(MenuState.Initial(newsRepository.checkNewNews()))
    }

    companion object {
        private const val RESTORE_KEY = "menu_restore_key"
    }
}