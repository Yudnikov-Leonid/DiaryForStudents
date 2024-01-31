package com.maxim.diaryforstudents.performance.analytics.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface AnalyticsCommunication: Communication.All<AnalyticsState> {
    class Base: Communication.RegularWithDeath<AnalyticsState>(), AnalyticsCommunication
}