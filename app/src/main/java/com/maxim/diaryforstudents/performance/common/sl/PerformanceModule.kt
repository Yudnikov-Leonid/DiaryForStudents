package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceViewModel

class PerformanceModule(private val core: Core, private val clear: ClearViewModel) :
    Module<PerformanceViewModel> {
    override fun viewModel() = PerformanceViewModel(
        core.marksModule(),
        core.navigation(),
        clear,
    )
}