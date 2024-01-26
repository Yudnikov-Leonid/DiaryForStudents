package com.maxim.diaryforstudents.performance.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository

class PerformanceViewModel(
    private val repository: EduPerformanceRepository,
    private val communication: PerformanceCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<PerformanceState> {
    private var type = ACTUAL
    private var search = ""
    fun init() {
        communication.update(PerformanceState.Loading)
        handle({repository.data()}) { list ->
            communication.update(PerformanceState.Base(3, list.map { it.toUi() }, true))
        }
    }

    fun changeType(type: String) {
//        this.type = type
//        repository.changeType(type)
//        repository.init(this)
    }

    fun search(search: String) {
//        this.search = search
//        reload()
    }

    fun changeQuarter(new: Int) {
//        repository.changeQuarter(new)
//        if (type != ACTUAL)
//            changeType(ACTUAL)
//        communication.update(
//            PerformanceState.Base(
//                new,
//                repository.data(search).map { it.toUi() },
//                true
//            )
//        )
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }

    companion object {
        const val ACTUAL = "grades"
        const val FINAL = "final-grades"
    }
}