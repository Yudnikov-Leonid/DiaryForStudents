package com.maxim.diaryforstudents.performance.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.data.DiaryService
import com.maxim.diaryforstudents.performance.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.data.FailureHandler
import com.maxim.diaryforstudents.performance.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel

class PerformanceModule(private val core: Core, private val clear: ClearViewModel) :
    Module<PerformanceViewModel> {
    override fun viewModel() = PerformanceViewModel(
        PerformanceInteractor.Base(
            PerformanceRepository.Base(
                PerformanceCloudDataSource.Base(
                    core.retrofit().create(DiaryService::class.java), core.eduUser()
                )
            ), FailureHandler.Base(), PerformanceDataToDomainMapper()
        ),
        PerformanceCommunication.Base(),
        core.navigation(),
        clear,
        PerformanceDomainToUiMapper()
    )
}