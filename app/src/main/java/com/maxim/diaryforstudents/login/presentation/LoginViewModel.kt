package com.maxim.diaryforstudents.login.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.ManageResource
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.RunAsync
import com.maxim.diaryforstudents.login.data.AuthResultWrapper
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.menu.MenuScreen

class LoginViewModel(
    private val repository: LoginRepository,
    private val communication: LoginCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val resource: ManageResource,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<LoginState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            if (repository.userNotLoggedIn())
                communication.update(LoginState.Initial)
            else {
                navigation.update(MenuScreen)
                clear.clearViewModel(LoginViewModel::class.java)
            }
        }
    }

    fun handleResult(authResult: AuthResultWrapper) = handle({
        repository.handleResult(authResult)
    }) {
        it.map(communication, navigation, clear)
    }

    fun login() = communication.update(LoginState.Auth(resource))

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) {
        communication.observe(owner, observer)
    }
}