package com.maxim.diaryforstudents.performance.analytics.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface AnalyticsCommunication: Communication.Mutable<AnalyticsState> {
    class Base: Communication.Regular<AnalyticsState>(), AnalyticsCommunication
}