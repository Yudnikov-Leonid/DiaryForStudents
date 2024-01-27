package com.maxim.diaryforstudents.login.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginService
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.presentation.UiValidator

class EduLoginModule(private val core: Core, private val clear: ClearViewModel) :
    Module<LoginViewModel> {
    override fun viewModel() = LoginViewModel(
        LoginRepository.Base(core.retrofit().create(LoginService::class.java), core.eduUser()),
        LoginCommunication.Base(),
        UiValidator.Login(),
        UiValidator.Password(2),
        core.navigation(),
        clear
    )
}