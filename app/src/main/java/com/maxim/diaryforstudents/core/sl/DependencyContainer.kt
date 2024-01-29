package com.maxim.diaryforstudents.core.sl

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel
import com.maxim.diaryforstudents.actualPerformanceSettings.sl.ActualSettingsModule
import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateViewModel
import com.maxim.diaryforstudents.calculateAverage.sl.CalculateModule
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.diary.sl.DiaryModule
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.sl.EduLoginModule
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsViewModel
import com.maxim.diaryforstudents.lessonDetails.sl.LessonDetailsModule
import com.maxim.diaryforstudents.main.MainModule
import com.maxim.diaryforstudents.main.MainViewModel
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.menu.sl.MenuModule
import com.maxim.diaryforstudents.news.presentation.NewsViewModel
import com.maxim.diaryforstudents.news.sl.NewsModule
import com.maxim.diaryforstudents.openNews.OpenNewsModule
import com.maxim.diaryforstudents.openNews.OpenNewsViewModel
import com.maxim.diaryforstudents.performance.actualMarks.presentation.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.actualMarks.sl.PerformanceActualModule
import com.maxim.diaryforstudents.performance.finalMarks.presentation.PerformanceFinalViewModel
import com.maxim.diaryforstudents.performance.finalMarks.sl.PerformanceFinalModule
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel
import com.maxim.diaryforstudents.performance.sl.PerformanceModule
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel
import com.maxim.diaryforstudents.profile.sl.ProfileModule

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
            LoginViewModel::class.java -> EduLoginModule(core, clear)
            MenuViewModel::class.java -> MenuModule(core)
            ProfileViewModel::class.java -> ProfileModule(core, clear)
            NewsViewModel::class.java -> NewsModule(core, clear)
            OpenNewsViewModel::class.java -> OpenNewsModule(core, clear)
            PerformanceViewModel::class.java -> PerformanceModule(core, clear)
            DiaryViewModel::class.java -> DiaryModule(core, clear)
            LessonDetailsViewModel::class.java -> LessonDetailsModule(core, clear)
            CalculateViewModel::class.java -> CalculateModule(core, clear)
            ActualSettingsViewModel::class.java -> ActualSettingsModule(core, clear)
            PerformanceActualViewModel::class.java -> PerformanceActualModule(core)
            PerformanceFinalViewModel::class.java -> PerformanceFinalModule(core)
            else -> dependencyContainer.module(clasz)
        } as Module<T>
    }
}