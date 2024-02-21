package com.maxim.diaryforstudents.login.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.main.HideKeyboard
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserScreen

class LoginViewModel(
    private val repository: LoginRepository,
    private val communication: LoginCommunication,
    private val loginValidator: UiValidator,
    private val passwordValidator: UiValidator,
    private val navigation: Navigation.Update,
    private val manageResource: ManageResource,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<LoginState>, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(LoginState.Initial)
    }

    fun login(login: String, password: String, hideKeyboard: HideKeyboard) {
        try {
            loginValidator.isValid(login, manageResource)
            passwordValidator.isValid(password, manageResource)
            communication.update(LoginState.Loading)
            handle({ repository.login(login, password) }) { result ->
                if (result.isSuccessful()) {
                    navigation.update(SelectUserScreen)
                    hideKeyboard.hideKeyboard()
                    communication.update(LoginState.Initial)
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

    fun hideError() {
        communication.update(LoginState.Initial)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) {
        communication.observe(owner, observer)
    }
}