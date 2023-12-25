package com.maxim.diaryforstudents.core

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.login.data.LoginCloudDataSource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.main.MainViewModel
import com.maxim.diaryforstudents.menu.MenuViewModel
import com.maxim.diaryforstudents.news.data.NewsCloudDataSource
import com.maxim.diaryforstudents.news.presentation.NewsCommunication
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsViewModel
import com.maxim.diaryforstudents.openNews.OpenNewsViewModel
import com.maxim.diaryforstudents.performance.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel
import com.maxim.diaryforstudents.profile.data.ProfileCloudDataSource
import com.maxim.diaryforstudents.profile.presentation.ProfileCommunication
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel

interface ViewModelFactory : ProvideViewModel, ClearViewModel {
    class Base(core: Core) : ViewModelFactory {
        //todo fix provider must be in constructor
        private val provider = ProvideViewModel.Base(core, this)
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()
        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            if (map[clazz] == null)
                map[clazz] = provider.viewModel(clazz)
            return map[clazz] as T
        }

        override fun clearViewModel(clazz: Class<out ViewModel>) {
            map.remove(clazz)
        }
    }
}

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(clazz: Class<T>): T

    class Base(private val core: Core, private val clear: ClearViewModel) : ProvideViewModel {
        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            return when (clazz) {
                MainViewModel::class.java -> MainViewModel(core.navigation())
                LoginViewModel::class.java -> LoginViewModel(
                    LoginRepository.Base(LoginCloudDataSource.Base(core.dataBase())),
                    LoginCommunication.Base(),
                    core.navigation(),
                    clear,
                    core.manageResource()
                )

                ProfileViewModel::class.java -> ProfileViewModel(
                    ProfileRepository.Base(ProfileCloudDataSource.Base(core.context(), core.dataBase())),
                    ProfileCommunication.Base(),
                    core.navigation(),
                    clear,
                )

                MenuViewModel::class.java -> MenuViewModel(core.navigation())
                NewsViewModel::class.java -> NewsViewModel(
                    NewsRepository.Base(NewsCloudDataSource.Base(core.dataBase())),
                    NewsCommunication.Base(),
                    core.navigation(),
                    clear,
                    core.openNewsData()
                )

                OpenNewsViewModel::class.java -> OpenNewsViewModel(
                    core.openNewsData(),
                    core.navigation(),
                    clear
                )

                PerformanceViewModel::class.java -> PerformanceViewModel(
                    PerformanceRepository.Base(
                        PerformanceCloudDataSource.Base(
                            core.dataBase(),
                            LessonMapper.Base(core.manageResource())
                        )
                    ),
                    PerformanceCommunication.Base(),
                    core.navigation(),
                    clear
                )

                else -> throw IllegalStateException("unknown viewModel $clazz")
            } as T
        }
    }
}

interface ClearViewModel {
    fun clearViewModel(clazz: Class<out ViewModel>)
}