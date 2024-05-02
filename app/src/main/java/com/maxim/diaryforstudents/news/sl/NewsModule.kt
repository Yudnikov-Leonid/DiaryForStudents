package com.maxim.diaryforstudents.news.sl

import com.maxim.diaryforstudents.core.data.SimpleStorage
import com.maxim.diaryforstudents.core.service.Service
import com.maxim.diaryforstudents.news.data.NewsCloudDataSource
import com.maxim.diaryforstudents.news.data.NewsData
import com.maxim.diaryforstudents.news.data.NewsDataToUiMapper
import com.maxim.diaryforstudents.news.data.NewsRepository
import com.maxim.diaryforstudents.news.presentation.NewsCommunication
import com.maxim.diaryforstudents.news.presentation.NewsUi
import com.maxim.diaryforstudents.openNews.OpenNewsStorage
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class NewsModule {

    @Provides
    fun provideCommunication(): NewsCommunication = NewsCommunication.Base()

    @Provides
    fun provideNewsRepository(service: Service, simpleStorage: SimpleStorage): NewsRepository {
        return NewsRepository.Base(NewsCloudDataSource.Base(service), simpleStorage)
    }

    @Provides
    fun provideOpenNewsStorage(): OpenNewsStorage.Mutable {
        return OpenNewsStorage.Base()
    }

    @Provides
    fun provideOpenNewsStorageRead(): OpenNewsStorage.Read = provideOpenNewsStorage()

    @Provides
    fun provideOpenNewsStorageSave(): OpenNewsStorage.Save = provideOpenNewsStorage()

    @Provides
    fun provideMapper(): NewsData.Mapper<NewsUi> = NewsDataToUiMapper()
}