package com.maxim.diaryforstudents.calculateAverage.presentation

import com.maxim.diaryforstudents.core.presentation.Communication
import kotlinx.coroutines.flow.MutableStateFlow

interface CalculateCommunication: Communication.Mutable<CalculateState> {
    class Base: Communication.Regular<CalculateState>(MutableStateFlow(CalculateState.Empty)), CalculateCommunication
}