package com.maxim.diaryforstudents.menu.sl

import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class MenuModule {

    @Provides
    fun provideCommunication(): MenuCommunication = MenuCommunication.Base()
}