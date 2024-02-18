package com.maxim.diaryforstudents.performance.actualMarks

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication

class PerformanceActualModule(private val core: Core): Module<PerformanceActualViewModel> {
    override fun viewModel() = PerformanceActualViewModel(
        core.marksModule().marksInteractor(),
        PerformanceCommunication.Base(),
        core.calculateStorage(),
        core.lessonDetailsStorage(),
        core.analyticsStorage(),
        core.navigation(),
        PerformanceDomainToUiMapper(),
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper())
    )
}