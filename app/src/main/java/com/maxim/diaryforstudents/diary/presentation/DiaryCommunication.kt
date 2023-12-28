package com.maxim.diaryforstudents.diary.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface DiaryCommunication {
    interface Update: Communication.Update<DiaryState>
    interface Observe: Communication.Observe<DiaryState>
    interface Mutable: Update, Observe
    class Base: Communication.Abstract<DiaryState>(), Mutable
}