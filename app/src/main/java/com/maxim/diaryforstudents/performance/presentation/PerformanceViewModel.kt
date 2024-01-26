package com.maxim.diaryforstudents.performance.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.data.PerformanceData
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository

class PerformanceViewModel(
    private val repository: EduPerformanceRepository,
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
            quarter = repository.actualQuarter()
            communication.update(PerformanceState.Loading)
            handle({ repository.init() }) {
                val list = repository.cachedData()
                if (list.first() is PerformanceData.Error)
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
        val list = type.search(repository, search)
        if (list.first() is PerformanceData.Error)
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
        handle({ repository.changeQuarter(quarter) }) {
            val list = repository.cachedData(search)
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
    fun search(repository: EduPerformanceRepository, search: String): List<PerformanceData>
    fun isFinal(): Boolean

    object Base : MarksType {
        override fun search(
            repository: EduPerformanceRepository,
            search: String
        ) = repository.cachedData(search)

        override fun isFinal() = false
    }

    object Final : MarksType {
        override fun search(
            repository: EduPerformanceRepository,
            search: String
        ) = repository.cachedFinalData(search)

        override fun isFinal() = true
    }
}