package com.maxim.diaryforstudents.calculateAverage.sl

import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateCommunication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class CalculateModule {

    @Provides
    fun provideCommunication(): CalculateCommunication = CalculateCommunication.Base()
}