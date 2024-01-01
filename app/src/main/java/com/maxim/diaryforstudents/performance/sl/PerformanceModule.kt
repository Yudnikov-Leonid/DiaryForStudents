package com.maxim.diaryforstudents.performance.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel

class PerformanceModule(private val core: Core, private val clear: ClearViewModel) :
    Module<PerformanceViewModel> {
    override fun viewModel() = PerformanceViewModel(
        PerformanceRepository.Base(
            PerformanceCloudDataSource.Base(
                core.service(),
                core.myUser(),
                core.lessonsMapper()
            )
        ),
        PerformanceCommunication.Base(),
        core.navigation(),
        clear
    )
}