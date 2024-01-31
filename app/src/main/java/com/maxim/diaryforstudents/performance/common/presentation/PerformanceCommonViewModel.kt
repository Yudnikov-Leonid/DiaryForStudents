package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.actualMarks.presentation.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsViewModel
import com.maxim.diaryforstudents.performance.common.sl.MarksModule
import com.maxim.diaryforstudents.performance.finalMarks.presentation.PerformanceFinalViewModel

class PerformanceCommonViewModel(
    private val marksModule: MarksModule.Clear,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), GoBack {

    override fun goBack() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceFinalViewModel::class.java)
        clear.clearViewModel(PerformanceActualViewModel::class.java)
        clear.clearViewModel(AnalyticsViewModel::class.java)
        marksModule.clear()
        clear.clearViewModel(PerformanceCommonViewModel::class.java)
    }
}