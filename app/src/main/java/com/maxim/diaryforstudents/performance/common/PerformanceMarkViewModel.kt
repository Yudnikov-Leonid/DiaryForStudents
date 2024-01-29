package com.maxim.diaryforstudents.performance.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.performance.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.presentation.MarksType
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

abstract class PerformanceMarkViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    private val mapper: PerformanceDomain.Mapper<PerformanceUi>
): BaseViewModel(), Reload, Communication.Observe<PerformanceState> {
    protected abstract val type: MarksType
    protected var quarter = 0

    override fun reload() {
        val list = type.search(interactor, "")
        communication.update(
            PerformanceState.Base(
                quarter,
                list.map { it.map(mapper) },
                type.isFinal(),
                interactor.progressType()
            )
        )
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }
}