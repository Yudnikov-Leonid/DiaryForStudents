package com.maxim.diaryforstudents.calculateAverage.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.sl.ClearViewModel

class CalculateViewModel(
    private val communication: CalculateCommunication,
    private val calculateStorage: CalculateStorage.Read,
    private val clearViewModel: ClearViewModel
) : ViewModel(), Communication.Observe<CalculateState> {
    fun init() {
        communication.update(
            CalculateState.Base(
                calculateStorage.marks(),
                calculateStorage.sum().toFloat() / calculateStorage.marks().size
            )
        )
    }

    fun clear() {
        clearViewModel.clearViewModel(CalculateViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<CalculateState>) {
        communication.observe(owner, observer)
    }
}