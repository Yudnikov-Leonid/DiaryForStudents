package com.maxim.diaryforstudents.login.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.presentation.UiValidator

class LoginModule(private val core: Core) :
    Module<LoginViewModel> {
    override fun viewModel() = LoginViewModel(
        core.loginRepository(),
        LoginCommunication.Base(),
        UiValidator.Login(),
        UiValidator.Password(2),
        core.navigation(),
        core
    )
}