package com.maxim.diaryforstudents.performance.finalMarks.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.finalMarks.presentation.PerformanceFinalViewModel
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication

class PerformanceFinalModule(private val core: Core): Module<PerformanceFinalViewModel> {
    override fun viewModel() = PerformanceFinalViewModel(
        core.marksInteractor(),
        PerformanceCommunication.Base(),
        PerformanceDomainToUiMapper()
    )
}