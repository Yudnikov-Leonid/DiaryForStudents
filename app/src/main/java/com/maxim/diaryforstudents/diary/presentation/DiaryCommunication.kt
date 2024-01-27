package com.maxim.diaryforstudents.diary.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface DiaryCommunication: Communication.Mutable<DiaryState> {
    class Base : Communication.Regular<DiaryState>(), DiaryCommunication
}