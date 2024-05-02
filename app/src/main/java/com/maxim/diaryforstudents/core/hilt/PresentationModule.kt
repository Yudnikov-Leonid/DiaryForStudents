package com.maxim.diaryforstudents.core.hilt

import com.maxim.diaryforstudents.core.presentation.Navigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideNavigation(): Navigation.Mutable {
        return Navigation.Base()
    }
}