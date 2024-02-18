package com.maxim.diaryforstudents.performance.common.sl

import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.performance.common.domain.OldPerformanceInteractor
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

interface MarksModule {
    fun marksInteractor(): PerformanceInteractor

    interface Clear {
        fun clear()
    }

    interface Mutable : MarksModule, Clear

    class Base(private val core: Core) : Mutable {
        private var marksInteractor: OldPerformanceInteractor? = null

        override fun marksInteractor(): PerformanceInteractor {
            TODO("Not yet implemented")
        }

        override fun clear() {
            marksInteractor = null
        }
    }
}