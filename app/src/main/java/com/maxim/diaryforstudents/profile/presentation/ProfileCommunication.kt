package com.maxim.diaryforstudents.profile.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface ProfileCommunication {
    interface Update: Communication.Update<ProfileState>
    interface Observe: Communication.Observe<ProfileState>
    interface Mutable: Update, Observe
    class Base: Communication.Abstract<ProfileState>(), Mutable
}