package com.maxim.diaryforstudents.editDiary.selectClass.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface SelectClassCommunication: Communication.Mutable<SelectClassState> {
    class Base: Communication.Abstract<SelectClassState>(), SelectClassCommunication
}