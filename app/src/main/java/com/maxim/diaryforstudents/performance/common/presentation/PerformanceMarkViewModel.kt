package com.maxim.diaryforstudents.performance.common.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import kotlinx.coroutines.flow.StateFlow

abstract class PerformanceMarkViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    private val mapper: PerformanceDomain.Mapper<PerformanceUi>,
    runAsync: RunAsync
) : BaseViewModel(runAsync), Reload, Communication.Observe<PerformanceState> {
    protected abstract val type: MarksType
    protected var quarter = 0

    override fun reload() {
        val list = type.getDataList(interactor)
        if (list.first().message().isNotEmpty())
            communication.update(PerformanceState.Error(list.first().message()))
        else
            communication.update(
                PerformanceState.Base(
                    quarter,
                    list.map { it.map(mapper) },
                    type.isFinal(),
                    interactor.currentProgressType(),
                    interactor.showType()
                )
            )
    }

    override fun state() = communication.state()
}