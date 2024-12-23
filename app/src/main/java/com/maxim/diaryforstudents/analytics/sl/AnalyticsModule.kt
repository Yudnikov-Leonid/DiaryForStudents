package com.maxim.diaryforstudents.analytics.sl

import com.maxim.diaryforstudents.analytics.presentation.AnalyticsCommunication
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module

class AnalyticsModule(private val core: Core, private val clearViewModel: ClearViewModel) : Module<AnalyticsViewModel> {
    override fun viewModel() =
        AnalyticsViewModel(
            core.marksModule().marksInteractor(),
            core.analyticsStorage(),
            core.settingsStorage(),
            AnalyticsCommunication.Base(),
            core.navigation(),
            clearViewModel
        )

}