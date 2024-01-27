package com.maxim.diaryforstudents.performance.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.domain.PerformanceInteractor

class PerformanceViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<PerformanceState> {
    private var type: MarksType = MarksType.Base
    private var search = ""
    private var quarter = 0

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            quarter = interactor.actualQuarter()
            communication.update(PerformanceState.Loading)
            handle({ interactor.init() }) {
                val list = interactor.data("")
                if (list.first() is PerformanceDomain.Error)
                    communication.update(PerformanceState.Error(list.first().message()))
                else
                    communication.update(
                        PerformanceState.Base(
                            quarter,
                            list.map { it.toUi() },
                            false
                        )
                    )
            }
        }
    }

    fun changeType(type: MarksType) {
        this.type = type
        search(search)
    }

    fun search(search: String) {
        this.search = search
        val list = type.search(interactor, search)
        if (list.first() is PerformanceDomain.Error)
            communication.update(PerformanceState.Error(list.first().message()))
        else
            communication.update(
                PerformanceState.Base(
                    quarter,
                    list.map { it.toUi() },
                    type.isFinal()
                )
            )
    }

    fun changeQuarter(quarter: Int) {
        this.quarter = quarter
        communication.update(PerformanceState.Loading)
        handle({ interactor.changeQuarter(quarter) }) {
            val list = interactor.data(search)
            communication.update(
                PerformanceState.Base(
                    quarter,
                    list.map { it.toUi() },
                    false
                )
            )
        }
    }

    fun back() {
        if (search.isEmpty()) {
            navigation.update(Screen.Pop)
            clear.clearViewModel(PerformanceViewModel::class.java)
        } else {
            search = ""
            search(search)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }
}

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