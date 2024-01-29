package com.maxim.diaryforstudents.performance.actualMarks.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.performance.actualMarks.presentation.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication

class PerformanceActualModule(private val core: Core): Module<PerformanceActualViewModel> {
    override fun viewModel() = PerformanceActualViewModel(
        core.marksInteractor(),
        PerformanceCommunication.Base(),
        core.actualSettingsCommunication(),
        core.calculateStorage(),
        core.lessonDetailsStorage(),
        core.navigation(),
        PerformanceDomainToUiMapper(),
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper())
    )
}