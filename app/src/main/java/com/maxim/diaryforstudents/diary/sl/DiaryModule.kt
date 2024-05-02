package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.diary.domain.DayDomain
import com.maxim.diaryforstudents.diary.domain.DayDomainToUiMapper
import com.maxim.diaryforstudents.diary.presentation.DayUi
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class DiaryModule {

    @Provides
    fun provideCommunication(): DiaryCommunication = DiaryCommunication.Base()

    @Provides
    fun provideMapper(): DayDomain.Mapper<DayUi> = DayDomainToUiMapper()
}