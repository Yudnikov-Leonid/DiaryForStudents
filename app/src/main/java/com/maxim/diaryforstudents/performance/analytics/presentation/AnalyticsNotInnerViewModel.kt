package com.maxim.diaryforstudents.performance.analytics.presentation

import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

class AnalyticsNotInnerViewModel(
    interactor: PerformanceInteractor,
    analyticsStorage: AnalyticsStorage.Read,
    communication: AnalyticsCommunication,
    navigation: Navigation.Update,
    clearViewModel: ClearViewModel
) : AnalyticsViewModel(
    interactor, analyticsStorage, communication, navigation, clearViewModel
) {
    override val showFinal = false
}