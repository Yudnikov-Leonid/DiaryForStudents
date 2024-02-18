package com.maxim.diaryforstudents.analytics.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsCommunication
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsInnerViewModel
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsViewModel

class AnalyticsInnerModule(private val core: Core, private val clearViewModel: ClearViewModel) : Module<AnalyticsViewModel> {
    override fun viewModel() =
        AnalyticsInnerViewModel(
           core.marksModule().marksInteractor(),
            core.analyticsStorage(),
            AnalyticsCommunication.Base(),
            core.navigation(),
            clearViewModel
        )

}