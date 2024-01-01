package com.maxim.diaryforstudents.editDiary.selectClass.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.editDiary.selectClass.data.SelectClassRepository
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassCommunication
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassViewModel

class SelectClassModule(private val core: Core, private val clear: ClearViewModel) :
    Module<SelectClassViewModel> {
    override fun viewModel() = SelectClassViewModel(
        SelectClassRepository.Base(core.service()),
        SelectClassCommunication.Base(),
        core.selectedClassCache(),
        core.navigation(),
        clear
    )
}