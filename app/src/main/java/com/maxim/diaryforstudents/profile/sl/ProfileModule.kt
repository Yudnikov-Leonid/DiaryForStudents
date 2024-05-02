package com.maxim.diaryforstudents.profile.sl

import com.maxim.diaryforstudents.core.service.EduUser
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import com.maxim.diaryforstudents.profile.presentation.ProfileCommunication
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@dagger.Module
@InstallIn(ViewModelComponent::class)
class ProfileModule {

    @Provides
    fun provideCommunication(): ProfileCommunication = ProfileCommunication.Base()

    @Provides
    fun provideProfileRepository(eduUser: EduUser): ProfileRepository {
        return ProfileRepository.Base(eduUser)
    }
}