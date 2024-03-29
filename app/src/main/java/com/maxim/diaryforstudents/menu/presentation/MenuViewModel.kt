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
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsScreen
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen
import com.maxim.diaryforstudents.settings.data.LessonsInMenuSettings
import com.maxim.diaryforstudents.settings.presentation.SettingsScreen

class MenuViewModel(
    private val communication: MenuCommunication,
    private val diaryInteractor: DiaryInteractor,
    private val storage: LessonDetailsStorage.Save,
    private val performanceInteractor: PerformanceInteractor,
    private val newsRepository: NewsRepository,
    private val showLessonsInMenuSettings: LessonsInMenuSettings.Read,
    private val navigation: Navigation.Update,
    private val mapper: DiaryDomain.Mapper<DiaryUi>,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, SaveAndRestore, Communication.Observe<MenuState>,
    ReloadWithError {
    private var newMarksCount = 0

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            showLessonsInMenuSettings.setCallback(this)
            handle {
                performanceInteractor.loadData()
                newMarksCount = performanceInteractor.newMarksCount()
                newsRepository.init(this)
            }
        }
    }

    fun updateLessons() {
        handle({ diaryInteractor.initMenuLessons() }) {
            reload()
        }
    }

    fun settings() {
        navigation.update(SettingsScreen)
    }

    fun diary() {
        navigation.update(DiaryScreen)
    }

    fun performance() {
        newMarksCount = 0
        reload()
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

    //not tested
    fun lesson(item: DiaryUi.Lesson) {
        storage.save(item)
        navigation.update(LessonDetailsScreen)
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
        communication.update(
            MenuState.Initial(
                newMarksCount,
                newsRepository.checkNewNews(),
                if (showLessonsInMenuSettings.isShow())
                    diaryInteractor.menuLessons()
                        .map { it.map(mapper) as DiaryUi.Lesson } else emptyList(),
                diaryInteractor.currentLesson(),
            )
        )
    }

    companion object {
        private const val RESTORE_KEY = "menu_restore_key"
    }
}