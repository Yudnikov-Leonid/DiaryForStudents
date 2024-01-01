package com.maxim.diaryforstudents.core.sl

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.diary.sl.DiaryModule
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.CreateLessonViewModel
import com.maxim.diaryforstudents.editDiary.createLesson.sl.CreateLessonModule
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryViewModel
import com.maxim.diaryforstudents.editDiary.edit.sl.EditDiaryModule
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassViewModel
import com.maxim.diaryforstudents.editDiary.selectClass.sl.SelectClassModule
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
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel
import com.maxim.diaryforstudents.performance.sl.PerformanceModule
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel
import com.maxim.diaryforstudents.profile.sl.ProfileModule

interface DependencyContainer {
    fun <T : ViewModel> module(clasz: Class<T>): Module<*>

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
            LoginViewModel::class.java -> LoginModule(core, clear)
            MenuViewModel::class.java -> MenuModule(core)
            ProfileViewModel::class.java -> ProfileModule(core, clear)
            NewsViewModel::class.java -> NewsModule(core, clear)
            OpenNewsViewModel::class.java -> OpenNewsModule(core, clear)
            PerformanceViewModel::class.java -> PerformanceModule(core, clear)
            DiaryViewModel::class.java -> DiaryModule(core, clear)
            SelectClassViewModel::class.java -> SelectClassModule(core, clear)
            EditDiaryViewModel::class.java -> EditDiaryModule(core, clear)
            CreateLessonViewModel::class.java -> CreateLessonModule(core, clear)
            else -> dependencyContainer.module(clasz)
        }
    }
}