package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class PerformanceModule {

    @Provides
    fun provideMapper(): PerformanceDomain.Mapper<PerformanceUi> = PerformanceDomainToUiMapper()

    @Provides
    fun provideCommunication(): PerformanceCommunication = PerformanceCommunication.Base()
}