package com.maxim.diaryforstudents.lessonDetails.sl

import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsCommunication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class LessonDetailsModule {

    @Provides
    fun provideCommunication(): LessonDetailsCommunication = LessonDetailsCommunication.Base()
}