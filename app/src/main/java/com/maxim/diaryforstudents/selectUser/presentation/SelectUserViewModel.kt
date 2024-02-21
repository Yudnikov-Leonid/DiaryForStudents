package com.maxim.diaryforstudents.selectUser.presentation

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.presentation.SimpleInit
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.selectUser.data.SelectUserData

class SelectUserViewModel(
    private val repository: LoginRepository,
    private val communication: SelectUserCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    private val mapper: SelectUserData.Mapper<SelectUserUi>
): ViewModel(), GoBack, SimpleInit {

    override fun init() {
        communication.update(SelectUserState.Base(repository.users().map { it.map(mapper) }))
    }

    fun select(position: Int) {
        repository.select(position)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(SelectUserViewModel::class.java)
    }
}