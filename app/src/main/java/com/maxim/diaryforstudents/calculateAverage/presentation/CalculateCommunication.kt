package com.maxim.diaryforstudents.calculateAverage.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface CalculateCommunication: Communication.Mutable<CalculateState> {
    class Base: Communication.Regular<CalculateState>(), CalculateCommunication
}