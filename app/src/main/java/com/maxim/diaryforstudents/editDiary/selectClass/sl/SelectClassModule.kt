package com.maxim.diaryforstudents.editDiary.selectClass.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.selectClass.data.SelectClassRepository
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassCommunication
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassViewModel

class SelectClassModule(private val core: Core, private val clear: ClearViewModel): Module<SelectClassViewModel> {
    override fun viewModel() = SelectClassViewModel(
        SelectClassRepository.Base(core.dataBase()),
        SelectClassCommunication.Base(),
        SelectedClassCache.Base(),
        core.navigation(),
        clear
    )
}