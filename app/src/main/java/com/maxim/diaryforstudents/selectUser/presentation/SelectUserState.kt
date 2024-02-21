package com.maxim.diaryforstudents.selectUser.presentation

interface SelectUserState {
    data class Base(private val users: List<SelectUserUi>): SelectUserState
}