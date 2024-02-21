package com.maxim.diaryforstudents.selectUser.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface SelectUserCommunication: Communication.Mutable<SelectUserState> {
    class Base: Communication.Regular<SelectUserState>(), SelectUserCommunication
}