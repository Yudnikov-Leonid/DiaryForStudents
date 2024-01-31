package com.maxim.diaryforstudents.performance.actualMarks.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.performance.actualMarks.presentation.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication

class PerformanceActualModule(private val core: Core, private val clearViewModel: ClearViewModel): Module<PerformanceActualViewModel> {
    override fun viewModel() = PerformanceActualViewModel(
        core.marksModule().marksInteractor(),
        PerformanceCommunication.Base(),
        core.actualSettingsCommunication(),
        core.calculateStorage(),
        core.lessonDetailsStorage(),
        core.analyticsStorage(),
        core.navigation(),
        clearViewModel,
        PerformanceDomainToUiMapper(),
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper())
    )
}