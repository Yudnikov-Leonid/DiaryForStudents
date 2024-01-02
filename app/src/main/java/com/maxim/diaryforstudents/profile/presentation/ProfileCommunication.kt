package com.maxim.diaryforstudents.profile.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface ProfileCommunication: Communication.All<ProfileState> {
    class Base : Communication.RegularWithDeath<ProfileState>(), ProfileCommunication
}