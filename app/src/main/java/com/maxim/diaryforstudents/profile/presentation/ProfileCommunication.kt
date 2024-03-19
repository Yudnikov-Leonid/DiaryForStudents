package com.maxim.diaryforstudents.profile.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface ProfileCommunication: Communication.Mutable<ProfileState> {
    class Base : Communication.Regular<ProfileState>(MutableStateFlow(ProfileState.Empty)), ProfileCommunication
}