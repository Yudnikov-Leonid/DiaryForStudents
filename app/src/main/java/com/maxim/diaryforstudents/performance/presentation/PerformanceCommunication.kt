package com.maxim.diaryforstudents.performance.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface PerformanceCommunication {
    interface Update : Communication.Update<PerformanceState>
    interface Observe : Communication.Observe<PerformanceState>
    interface Mutable : Update, Observe
    class Base : Communication.Abstract<PerformanceState>(), Mutable
}