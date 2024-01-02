package com.maxim.diaryforstudents.diary.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface DiaryCommunication: Communication.All<DiaryState> {
    class Base : Communication.RegularWithDeath<DiaryState>(), DiaryCommunication
}