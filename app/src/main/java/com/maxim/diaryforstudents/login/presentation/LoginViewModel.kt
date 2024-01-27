package com.maxim.diaryforstudents.login.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.menu.presentation.MenuScreen

class LoginViewModel(
    private val repository: LoginRepository,
    private val communication: LoginCommunication,
    private val loginValidator: UiValidator,
    private val passwordValidator: UiValidator,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<LoginState>, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(LoginState.Initial)
    }

    fun login(login: String, password: String) {
        try {
            loginValidator.isValid(login)
            passwordValidator.isValid(password)
            communication.update(LoginState.Loading)
            handle({ repository.login(login, password) }) { result ->
                if (result.isSuccessful()) {
                    navigation.update(MenuScreen)
                    clearViewModel.clearViewModel(LoginViewModel::class.java)
                }
                else
                    communication.update(LoginState.Error(result.message()))
            }
        } catch (e: LoginException) {
            communication.update(LoginState.LoginError(e.message!!))
        } catch (e: PasswordException) {
            communication.update(LoginState.PasswordError(e.message!!))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) {
        communication.observe(owner, observer)
    }
}