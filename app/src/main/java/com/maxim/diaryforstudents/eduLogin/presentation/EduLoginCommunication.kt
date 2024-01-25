package com.maxim.diaryforstudents.eduLogin.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface EduLoginCommunication: Communication.Mutable<EduLoginState> {
    class Base: Communication.Regular<EduLoginState>(), EduLoginCommunication
}