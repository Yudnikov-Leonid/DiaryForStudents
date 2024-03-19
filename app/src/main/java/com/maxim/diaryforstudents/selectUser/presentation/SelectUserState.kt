package com.maxim.diaryforstudents.selectUser.presentation

interface SelectUserState {
    fun show(adapter: SelectUserAdapter)

    data class Base(private val users: List<SelectUserUi>): SelectUserState {
        override fun show(adapter: SelectUserAdapter) {
            adapter.update(users)
        }
    }

    object Empty: SelectUserState {
        override fun show(adapter: SelectUserAdapter) = Unit
    }
}