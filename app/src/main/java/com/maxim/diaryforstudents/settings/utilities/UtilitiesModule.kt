package com.maxim.diaryforstudents.settings.utilities

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class UtilitiesModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<UtilitiesViewModel> {
    override fun viewModel() = UtilitiesViewModel(
        core.navigation(),
        clearViewModel
    )
}