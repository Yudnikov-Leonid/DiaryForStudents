package com.maxim.diaryforstudents.editDiary.edit.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface EditDiaryCommunication: Communication.Mutable<EditDiaryState> {
    class Base: Communication.Abstract<EditDiaryState>(), EditDiaryCommunication
}