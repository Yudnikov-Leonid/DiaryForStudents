package com.maxim.diaryforstudents.performance.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.eduData.DiaryService
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel

class PerformanceModule(private val core: Core, private val clear: ClearViewModel) :
    Module<PerformanceViewModel> {
    override fun viewModel() = PerformanceViewModel(
        EduPerformanceRepository.Base(
            EduPerformanceCloudDataSource.Base(
                core.retrofit().create(DiaryService::class.java), core.simpleStorage()
            )
        ),
        PerformanceCommunication.Base(),
        core.navigation(),
        clear
    )
}