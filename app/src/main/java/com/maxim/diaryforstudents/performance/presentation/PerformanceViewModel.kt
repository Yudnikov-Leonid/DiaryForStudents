package com.maxim.diaryforstudents.performance.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.data.PerformanceRepository

class PerformanceViewModel(
    private val repository: PerformanceRepository,
    private val communication: PerformanceCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
) : BaseViewModel(), Communication.Observe<PerformanceState>, Reload {
    private var type = ACTUAL
    private var search = ""
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(PerformanceState.Loading)
            repository.init(this)
        }
    }

    fun changeType(type: String) {
        this.type = type
        repository.changeType(type)
        repository.init(this)
    }

    fun search(search: String) {
        this.search = search
        reload()
    }

    fun changeQuarter(new: Int) {
        repository.changeQuarter(new)
        if (type != ACTUAL)
            changeType(ACTUAL)
        communication.update(
            PerformanceState.Base(
                new,
                repository.data(search).map { it.toUi() },
                true
            )
        )
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }

    override fun reload() {
        communication.update(
            PerformanceState.Base(
                repository.actualQuarter(),
                repository.data(search).map { it.toUi() },
                type == ACTUAL
            )
        )
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceViewModel::class.java)
    }

    override fun error(message: String) {
        communication.update(PerformanceState.Error(message))
    }

    companion object {
        const val ACTUAL = "grades"
        const val FINAL = "final-grades"
    }
}