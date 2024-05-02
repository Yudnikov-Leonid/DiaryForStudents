package com.maxim.diaryforstudents.analytics.sl

import com.maxim.diaryforstudents.analytics.presentation.AnalyticsCommunication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AnalyticsModule {

    @Provides
    fun provideCommunication(): AnalyticsCommunication {
        return AnalyticsCommunication.Base()
    }
}