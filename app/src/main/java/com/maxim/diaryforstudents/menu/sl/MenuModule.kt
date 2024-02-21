package com.maxim.diaryforstudents.menu.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.news.data.NewsCloudDataSource
import com.maxim.diaryforstudents.news.data.NewsRepository

class MenuModule(private val core: Core) : Module<MenuViewModel> {
    override fun viewModel() = MenuViewModel(
        MenuCommunication.Base(),
        core.marksModule().marksInteractor(),
        NewsRepository.Base(NewsCloudDataSource.Base(core.service()), core.simpleStorage()),
        core.navigation()
    )
}