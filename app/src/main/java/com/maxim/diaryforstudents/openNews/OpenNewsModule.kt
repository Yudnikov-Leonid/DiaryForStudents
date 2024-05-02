package com.maxim.diaryforstudents.openNews

import android.content.Context
import com.maxim.diaryforstudents.openNews.data.Downloader
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@dagger.Module
@InstallIn(ViewModelComponent::class)
class OpenNewsModule {

    @Provides
    fun provideCommunication(): OpenNewsCommunication = OpenNewsCommunication.Base()

    @Provides
    fun provideDownloader(@ApplicationContext context: Context): Downloader {
        return Downloader.Base(context)
    }
}