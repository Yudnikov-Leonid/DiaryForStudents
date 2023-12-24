package com.maxim.diaryforstudents.core

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.login.data.LoginCloudDataSource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.main.MainViewModel
import com.maxim.diaryforstudents.menu.MenuViewModel
import com.maxim.diaryforstudents.news.NewsCloudDataSource
import com.maxim.diaryforstudents.news.NewsCommunication
import com.maxim.diaryforstudents.news.NewsRepository
import com.maxim.diaryforstudents.news.NewsViewModel
import com.maxim.diaryforstudents.profile.ProfileCommunication
import com.maxim.diaryforstudents.profile.ProfileRepository
import com.maxim.diaryforstudents.profile.ProfileViewModel

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
                    ProfileRepository.Base(),
                    ProfileCommunication.Base(),
                    core.navigation(),
                    clear,
                    core.manageResource()
                )

                MenuViewModel::class.java -> MenuViewModel(core.navigation())
                NewsViewModel::class.java -> NewsViewModel(
                    NewsRepository.Base(NewsCloudDataSource.Base(core.dataBase())),
                    NewsCommunication.Base(),
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