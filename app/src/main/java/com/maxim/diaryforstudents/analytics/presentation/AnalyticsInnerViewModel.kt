package com.maxim.diaryforstudents.analytics.presentation

import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

class AnalyticsInnerViewModel(
    interactor: PerformanceInteractor,
    analyticsStorage: AnalyticsStorage.Read,
    communication: AnalyticsCommunication,
    navigation: Navigation.Update,
    clearViewModel: ClearViewModel,
) : AnalyticsViewModel(
    interactor, analyticsStorage, communication, navigation, clearViewModel
) {
    override val showFinal = true

    override val RESTORE_KEY = "inner_analytics_restore"
    override val QUARTER_RESTORE_KEY = "inner_quarter_analytics_restore"
    override val INTERVAL_RESTORE_KEY = "inner_interval_analytics_restore"
}