package com.maxim.diaryforstudents.selectUser.sl

import com.maxim.diaryforstudents.selectUser.presentation.SelectUserCommunication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SelectUserModule {

    @Provides
    fun provideCommunication(): SelectUserCommunication = SelectUserCommunication.Base()
}