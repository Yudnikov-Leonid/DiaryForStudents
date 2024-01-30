package com.maxim.diaryforstudents.performance.analytics.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

class PerformanceAnalyticsViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: AnalyticsCommunication
) : BaseViewModel(), Communication.Observe<AnalyticsState>, Init, Reload {
    private var quarter = 1

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            //quarter = interactor.actualQuarter()
            reload()
        }
    }

    fun changeQuarter(value: Int) {
        quarter = value
        reload()
    }

    override fun reload() {
        communication.update(AnalyticsState.Loading)
        handle({ interactor.analytics(quarter) }) {
            if (it.first().message().isNotEmpty())
                communication.update(AnalyticsState.Error(it.first().message()))
            else
                communication.update(
                    AnalyticsState.Base(
                        it.map { it.toUi() }
                    )
                )
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AnalyticsState>) {
        communication.observe(owner, observer)
    }
}