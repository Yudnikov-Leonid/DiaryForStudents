package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.domain.DayDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {

    override fun viewModel() = DiaryViewModel(
        core.diaryInteractor(),
        DiaryCommunication.Base(),
        core.lessonDetailsStorage(),
        core.navigation(),
        clear,
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper()), DayDomainToUiMapper()
    )
}