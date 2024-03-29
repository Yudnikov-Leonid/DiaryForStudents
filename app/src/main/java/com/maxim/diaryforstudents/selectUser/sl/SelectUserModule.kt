package com.maxim.diaryforstudents.selectUser.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserCommunication
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserViewModel

interface SelectUserModule: Module<SelectUserViewModel> {
    fun clear()

    class Base(private val core: Core, private val clearViewModel: ClearViewModel): SelectUserModule {
        override fun viewModel() = SelectUserViewModel(
            core.loginRepository(),
            SelectUserCommunication.Base(),
            core.navigation(),
            clearViewModel,
            this
        )

        override fun clear() {
            core.clearLoginRepository()
        }
    }
}