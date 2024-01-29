package com.maxim.diaryforstudents.calculateAverage.sl

import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateCommunication
import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class CalculateModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<CalculateViewModel> {
    override fun viewModel() = CalculateViewModel(
        CalculateCommunication.Base(),
        core.calculateStorage(),
        clearViewModel
    )
}