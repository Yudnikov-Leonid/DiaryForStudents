package com.maxim.diaryforstudents.core.sl

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel
import com.maxim.diaryforstudents.actualPerformanceSettings.sl.ActualSettingsModule
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsViewModel
import com.maxim.diaryforstudents.analytics.sl.AnalyticsModule
import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateViewModel
import com.maxim.diaryforstudents.calculateAverage.sl.CalculateModule
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.diary.sl.DiaryModule
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsViewModel
import com.maxim.diaryforstudents.lessonDetails.sl.LessonDetailsModule
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.sl.LoginModule
import com.maxim.diaryforstudents.main.MainModule
import com.maxim.diaryforstudents.main.MainViewModel
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.menu.sl.MenuModule
import com.maxim.diaryforstudents.news.presentation.NewsViewModel
import com.maxim.diaryforstudents.news.sl.NewsModule
import com.maxim.diaryforstudents.openNews.OpenNewsModule
import com.maxim.diaryforstudents.openNews.OpenNewsViewModel
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualModule
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommonViewModel
import com.maxim.diaryforstudents.performance.common.sl.PerformanceModule
import com.maxim.diaryforstudents.performance.finalMarks.PerformanceFinalModule
import com.maxim.diaryforstudents.performance.finalMarks.PerformanceFinalViewModel
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel
import com.maxim.diaryforstudents.profile.sl.ProfileModule
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserViewModel
import com.maxim.diaryforstudents.selectUser.sl.SelectUserModule
import com.maxim.diaryforstudents.settings.presentation.SettingsViewModel
import com.maxim.diaryforstudents.settings.sl.SettingsModule
import com.maxim.diaryforstudents.settings.themes.SettingsThemesModule
import com.maxim.diaryforstudents.settings.themes.SettingsThemesViewModel
import com.maxim.diaryforstudents.settings.utilities.UtilitiesModule
import com.maxim.diaryforstudents.settings.utilities.UtilitiesViewModel

interface DependencyContainer {
    fun <T : ViewModel> module(clasz: Class<T>): Module<T>

    class Error : DependencyContainer {
        override fun <T : ViewModel> module(clasz: Class<T>) =
            throw IllegalStateException("unknown viewModel $clasz")
    }

    class Base(
        private val core: Core,
        private val clear: ClearViewModel,
        private val dependencyContainer: DependencyContainer = Error()
    ) : DependencyContainer {
        override fun <T : ViewModel> module(clasz: Class<T>) = when (clasz) {
            MainViewModel::class.java -> MainModule(core)
            LoginViewModel::class.java -> LoginModule(core)
            SelectUserViewModel::class.java -> SelectUserModule.Base(core, clear)
            MenuViewModel::class.java -> MenuModule(core)
            ProfileViewModel::class.java -> ProfileModule(core, clear)
            NewsViewModel::class.java -> NewsModule(core, clear)
            OpenNewsViewModel::class.java -> OpenNewsModule(core, clear)
            PerformanceCommonViewModel::class.java -> PerformanceModule(core, clear)
            DiaryViewModel::class.java -> DiaryModule(core, clear)
            LessonDetailsViewModel::class.java -> LessonDetailsModule(core, clear)
            CalculateViewModel::class.java -> CalculateModule(core, clear)
            ActualSettingsViewModel::class.java -> ActualSettingsModule(core, clear)
            PerformanceActualViewModel::class.java -> PerformanceActualModule(core)
            PerformanceFinalViewModel::class.java -> PerformanceFinalModule(core)
            AnalyticsViewModel::class.java -> AnalyticsModule(core, clear)
            SettingsViewModel::class.java -> SettingsModule(core, clear)
            SettingsThemesViewModel::class.java -> SettingsThemesModule(core, clear)
            UtilitiesViewModel::class.java -> UtilitiesModule(core, clear)
            else -> dependencyContainer.module(clasz)
        } as Module<T>
    }
}