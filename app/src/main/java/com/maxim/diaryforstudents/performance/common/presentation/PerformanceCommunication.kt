package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface PerformanceCommunication: Communication.Mutable<PerformanceState> {
    class Base : Communication.Regular<PerformanceState>(MutableStateFlow(PerformanceState.Empty)), PerformanceCommunication
}