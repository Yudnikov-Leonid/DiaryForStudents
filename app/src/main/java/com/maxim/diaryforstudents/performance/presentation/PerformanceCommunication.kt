package com.maxim.diaryforstudents.performance.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface PerformanceCommunication: Communication.Mutable<PerformanceState> {
    class Base : Communication.Single<PerformanceState>(), PerformanceCommunication
}