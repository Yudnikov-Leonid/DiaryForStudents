package com.maxim.diaryforstudents.settings.themes

import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.settings.data.SettingsThemesRepository

class SettingsThemesModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<SettingsThemesViewModel> {
    override fun viewModel() = SettingsThemesViewModel(
        SettingsThemesRepository.Base(ColorManager.Base(core.simpleStorage())),
        core.navigation(),
        clearViewModel
    )
}