package com.maxim.diaryforstudents.settings.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.settings.presentation.SettingsViewModel

class SettingsModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<SettingsViewModel> {
    override fun viewModel() = SettingsViewModel(core.navigation(), clearViewModel)
}