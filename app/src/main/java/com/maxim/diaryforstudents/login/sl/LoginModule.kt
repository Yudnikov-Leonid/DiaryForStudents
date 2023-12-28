package com.maxim.diaryforstudents.login.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.login.data.LoginCloudDataSource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel

class LoginModule(private val core: Core, private val clear: ClearViewModel): Module<LoginViewModel> {
    override fun viewModel() = LoginViewModel(
        LoginRepository.Base(LoginCloudDataSource.Base(core.dataBase())),
        LoginCommunication.Base(),
        core.navigation(),
        clear,
        core
    )
}