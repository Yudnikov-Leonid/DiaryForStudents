package com.maxim.diaryforstudents.performance.finalMarks.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.finalMarks.presentation.PerformanceFinalViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication

class PerformanceFinalModule(private val core: Core): Module<PerformanceFinalViewModel> {
    override fun viewModel() = PerformanceFinalViewModel(
        core.marksModule().marksInteractor(),
        PerformanceCommunication.Base(),
        PerformanceDomainToUiMapper()
    )
}