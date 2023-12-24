package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.core.Communication

interface PerformanceCommunication {
    interface Update : Communication.Update<PerformanceState>
    interface Observe : Communication.Observe<PerformanceState>
    interface Mutable : Update, Observe
    class Base : Communication.Abstract<PerformanceState>(), Mutable
}