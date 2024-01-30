package com.maxim.diaryforstudents.performance.analytics.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsCommunication
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsViewModel

class AnalyticsModule(private val core: Core, private val clearViewModel: ClearViewModel) : Module<AnalyticsViewModel> {
    override fun viewModel() =
        AnalyticsViewModel(
            core.marksModule().marksInteractor(),
            core.analyticsStorage(),
            AnalyticsCommunication.Base(),
            core.navigation(),
            clearViewModel
        )

}