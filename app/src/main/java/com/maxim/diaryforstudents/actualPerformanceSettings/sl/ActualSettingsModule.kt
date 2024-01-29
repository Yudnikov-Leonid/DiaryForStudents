package com.maxim.diaryforstudents.actualPerformanceSettings.sl

import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class ActualSettingsModule(private val core: Core, private val clearViewModel: ClearViewModel) :
    Module<ActualSettingsViewModel> {
    override fun viewModel() = ActualSettingsViewModel(
        core.actualSettingsCommunication(),
        core.simpleStorage(),
        clearViewModel
    )
}