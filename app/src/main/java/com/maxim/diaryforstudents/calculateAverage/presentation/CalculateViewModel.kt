package com.maxim.diaryforstudents.calculateAverage.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.presentation.ColorManager
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel @Inject constructor(
    private val communication: CalculateCommunication,
    private val colorManager: ColorManager,
    private val calculateStorage: CalculateStorage.Read,
    private val clearViewModel: ClearViewModel
) : ViewModel(), Communication.Observe<CalculateState> {
    private val list = mutableListOf<PerformanceUi>()
    private var sum = 0

    fun init() {
        list.clear()
        list.addAll(calculateStorage.marks())
        sum = calculateStorage.sum()

        reload()
    }

    fun add(value: Int) {
        list.add(PerformanceUi.Mark(value, MarkType.Current, "12.34.5678", "", false, isChecked = true))
        sum += value
        reload()
    }

    fun remove(value: Int) {
        var index = -1
        list.forEachIndexed { i, mark ->
            if (mark.compare(value))
                index = i
        }
        if (index != -1) {
            list.removeAt(index)
            sum -= value
            reload()
        }
    }

    fun reload() {
        communication.update(
            CalculateState.Base(
                list,
                sum.toFloat() / list.size,
                colorManager
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