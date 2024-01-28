package com.maxim.diaryforstudents.news.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.news.data.NewsCloudDataSource
import com.maxim.diaryforstudents.news.data.NewsDataToUiMapper
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsCommunication
import com.maxim.diaryforstudents.news.presentation.NewsViewModel

class NewsModule(private val core: Core, private val clear: ClearViewModel) :
    Module<NewsViewModel> {
    override fun viewModel() = NewsViewModel(
        NewsRepository.Base(NewsCloudDataSource.Base(core.service())),
        NewsCommunication.Base(),
        core.navigation(),
        clear,
        core.openNewsData(),
        NewsDataToUiMapper()
    )
}