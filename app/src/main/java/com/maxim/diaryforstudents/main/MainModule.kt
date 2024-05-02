package com.maxim.diaryforstudents.main

import com.maxim.diaryforstudents.core.service.EduUser
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class MainModule {

    @Provides
    fun provideMainInteractor(eduUser: EduUser): MainInteractor {
        return MainInteractor.Base(eduUser)
    }
}