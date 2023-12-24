package com.maxim.diaryforstudents.profile

import com.maxim.diaryforstudents.core.Communication

interface ProfileCommunication {
    interface Update: Communication.Update<ProfileState>
    interface Observe: Communication.Observe<ProfileState>
    interface Mutable: Update, Observe
    class Base: Communication.Abstract<ProfileState>(), Mutable
}