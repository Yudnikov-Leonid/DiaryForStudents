package com.maxim.diaryforstudents.performance.analytics.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsCommunication
import com.maxim.diaryforstudents.performance.analytics.presentation.PerformanceAnalyticsViewModel

class AnalyticsModule(private val core: Core): Module<PerformanceAnalyticsViewModel> {
    override fun viewModel() =
        PerformanceAnalyticsViewModel(AnalyticsCommunication.Base())

}