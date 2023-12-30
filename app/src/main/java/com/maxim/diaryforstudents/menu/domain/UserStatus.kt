package com.maxim.diaryforstudents.menu.domain

import com.maxim.diaryforstudents.menu.presentation.MenuState

interface UserStatus {
    fun mapToState(): MenuState //todo make mapper
    object Student: UserStatus {
        override fun mapToState() = MenuState.Student
    }

    object Teacher: UserStatus {
        override fun mapToState() = MenuState.Teacher
    }
}