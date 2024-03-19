package com.maxim.diaryforstudents.login.presentation

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserScreen

class LoginViewModel(
    private val repository: LoginRepository,
    private val communication: LoginCommunication.Mutable,
    private val loginValidator: UiValidator,
    private val passwordValidator: UiValidator,
    private val navigation: Navigation.Update,
    private val manageResource: ManageResource,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), LoginCommunication.Read, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.setErrorMessage("")
            communication.setLoginErrorMessage("")
            communication.setPasswordErrorMessage("")
        }
    }

    fun login(login: String, password: String) {
        try {
            loginValidator.isValid(login, manageResource)
            passwordValidator.isValid(password, manageResource)
            communication.setLoading(true)
            handle({ repository.login(login, password) }) { result ->
                communication.setLoading(false)
                if (result.isSuccessful()) {
                    navigation.update(SelectUserScreen)
                }
                else
                    communication.setErrorMessage(result.message())
            }
        } catch (e: LoginException) {
            communication.setLoginErrorMessage(e.message!!)
        } catch (e: PasswordException) {
            communication.setPasswordErrorMessage(e.message!!)
        }
    }

    fun hideErrors() {
        communication.setErrorMessage("")
        communication.setLoginErrorMessage("")
        communication.setPasswordErrorMessage("")
    }

    override fun loginErrorMessage() = communication.loginErrorMessage()
    override fun passwordErrorMessage() = communication.passwordErrorMessage()
    override fun errorMessage() = communication.errorMessage()
    override fun isLoading() = communication.isLoading()
}