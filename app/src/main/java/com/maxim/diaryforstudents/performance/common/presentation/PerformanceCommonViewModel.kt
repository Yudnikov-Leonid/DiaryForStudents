package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.finalMarks.PerformanceFinalViewModel

class PerformanceCommonViewModel(
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), GoBack {

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceActualViewModel::class.java)
        clear.clearViewModel(PerformanceFinalViewModel::class.java)
        clear.clearViewModel(PerformanceCommonViewModel::class.java)
    }
}