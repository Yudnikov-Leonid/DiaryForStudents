package com.maxim.diaryforstudents.selectUser.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface SelectUserCommunication: Communication.Mutable<SelectUserState> {
    class Base: Communication.Regular<SelectUserState>(MutableStateFlow(SelectUserState.Empty)), SelectUserCommunication
}