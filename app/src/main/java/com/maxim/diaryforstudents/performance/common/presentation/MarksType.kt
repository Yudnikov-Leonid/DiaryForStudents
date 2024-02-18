package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

interface MarksType {
    fun getDataList(interactor: PerformanceInteractor): List<PerformanceDomain>
    fun isFinal(): Boolean

    object Base : MarksType {
        override fun getDataList(
            interactor: PerformanceInteractor,
        ): List<PerformanceDomain> = interactor.actualData()

        override fun isFinal() = false
    }

    object Final : MarksType {
        override fun getDataList(
            interactor: PerformanceInteractor,
        ): List<PerformanceDomain> = interactor.finalData()

        override fun isFinal() = true
    }
}