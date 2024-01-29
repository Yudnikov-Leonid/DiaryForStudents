package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface PerformanceCommunication: Communication.All<PerformanceState> {
    class Base : Communication.SingleWithDeath<PerformanceState>(), PerformanceCommunication
}