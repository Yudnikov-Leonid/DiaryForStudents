package com.maxim.diaryforstudents.menu.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.news.data.NewsCloudDataSource
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper

class MenuModule(private val core: Core) : Module<MenuViewModel> {
    override fun viewModel() = MenuViewModel(
        MenuCommunication.Base(),
        core.diaryInteractor(),
        core.lessonDetailsStorage(),
        core.marksModule().marksInteractor(),
        NewsRepository.Base(NewsCloudDataSource.Base(core.service()), core.simpleStorage()),
        core.settingsStorage(),
        core.navigation(),
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper())
    )
}