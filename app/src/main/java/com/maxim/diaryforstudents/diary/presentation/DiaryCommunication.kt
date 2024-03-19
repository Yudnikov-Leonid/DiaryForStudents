package com.maxim.diaryforstudents.diary.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface DiaryCommunication: Communication.Mutable<DiaryState> {
    class Base : Communication.Regular<DiaryState>(MutableStateFlow(DiaryState.Empty)), DiaryCommunication
}