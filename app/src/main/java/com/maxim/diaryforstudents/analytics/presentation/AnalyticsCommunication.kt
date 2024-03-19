package com.maxim.diaryforstudents.analytics.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface AnalyticsCommunication: Communication.Mutable<AnalyticsState> {
    class Base: Communication.Regular<AnalyticsState>(MutableStateFlow(AnalyticsState.Empty)), AnalyticsCommunication
}