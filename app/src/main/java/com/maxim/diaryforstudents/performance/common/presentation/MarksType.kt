package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

interface MarksType {
    fun search(interactor: PerformanceInteractor, search: String): List<PerformanceDomain>
    fun isFinal(): Boolean

    object Base : MarksType {
        override fun search(
            interactor: PerformanceInteractor,
            search: String
        ): List<PerformanceDomain> = interactor.data(search)

        override fun isFinal() = false
    }

    object Final : MarksType {
        override fun search(
            interactor: PerformanceInteractor,
            search: String
        ): List<PerformanceDomain> = interactor.finalData(search)

        override fun isFinal() = true
    }
}