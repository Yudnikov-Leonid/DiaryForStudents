package com.maxim.diaryforstudents.eduLogin.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.eduLogin.data.EduLoginRepository
import com.maxim.diaryforstudents.menu.presentation.MenuScreen

class EduLoginViewModel(
    private val repository: EduLoginRepository,
    private val communication: EduLoginCommunication,
    private val loginValidator: UiValidator,
    private val passwordValidator: UiValidator,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<EduLoginState> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            communication.update(EduLoginState.Initial)
    }

    fun login(login: String, password: String) {
        try {
            loginValidator.isValid(login)
            passwordValidator.isValid(password)
            communication.update(EduLoginState.Loading)
            handle({ repository.login(login, password) }) { result ->
                if (result.isSuccessful()) {
                    navigation.update(MenuScreen)
                    clearViewModel.clearViewModel(EduLoginViewModel::class.java)
                }
                else
                    communication.update(EduLoginState.Error(result.message()))
            }
        } catch (e: LoginException) {
            communication.update(EduLoginState.LoginError(e.message!!))
        } catch (e: PasswordException) {
            communication.update(EduLoginState.PasswordError(e.message!!))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<EduLoginState>) {
        communication.observe(owner, observer)
    }
}