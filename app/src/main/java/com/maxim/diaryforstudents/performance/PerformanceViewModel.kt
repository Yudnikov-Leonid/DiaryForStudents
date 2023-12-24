package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.BaseViewModel
import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Communication
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.news.Reload

class PerformanceViewModel(
    private val repository: PerformanceRepository,
    private val communication: PerformanceCommunication.Mutable,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel
) : BaseViewModel(), Communication.Observe<PerformanceState>, Reload {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            repository.init(this)
    }

    fun changeQuarter(new: Int) {
        repository.changeQuarter(new)
        communication.update(PerformanceState.Base(new, repository.data().map { it.toUi() }))
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }

    override fun reload() {
        communication.update(
            PerformanceState.Base(
                repository.actualQuarter(),
                repository.data().map { it.toUi() })
        )
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceViewModel::class.java)
    }

    override fun error(message: String) {
        TODO("Not yet implemented")
    }
}