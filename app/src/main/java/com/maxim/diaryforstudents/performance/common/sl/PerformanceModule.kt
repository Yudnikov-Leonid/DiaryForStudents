package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommonViewModel

class PerformanceModule(private val core: Core, private val clear: ClearViewModel) :
    Module<PerformanceCommonViewModel> {
    override fun viewModel() = PerformanceCommonViewModel(
        core.navigation(),
        clear,
    )
}