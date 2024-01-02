package com.maxim.diaryforstudents.login.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface LoginCommunication: Communication.Mutable<LoginState> {
    class Base : Communication.Regular<LoginState>(), LoginCommunication
}