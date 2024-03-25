package com.maxim.diaryforstudents.selectUser.presentation

interface SelectUserState {
    fun show(showUsersToSelect: ShowUsersToSelect)

    data class Base(private val users: List<SelectUserUi>): SelectUserState {
        override fun show(showUsersToSelect: ShowUsersToSelect) {
            showUsersToSelect.show(users)
        }
    }
}