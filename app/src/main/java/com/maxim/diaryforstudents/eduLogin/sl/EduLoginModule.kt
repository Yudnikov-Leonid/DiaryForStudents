package com.maxim.diaryforstudents.eduLogin.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.UiValidator
import com.maxim.diaryforstudents.eduLogin.data.EduLoginRepository
import com.maxim.diaryforstudents.eduLogin.data.LoginService
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginCommunication
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginViewModel

class EduLoginModule(private val core: Core, private val clear: ClearViewModel) :
    Module<EduLoginViewModel> {
    override fun viewModel() = EduLoginViewModel(
        EduLoginRepository.Base(core.retrofit().create(LoginService::class.java)),
        EduLoginCommunication.Base(),
        UiValidator.Login(),
        UiValidator.Password(2),
        core.navigation(),
        clear
    )
}