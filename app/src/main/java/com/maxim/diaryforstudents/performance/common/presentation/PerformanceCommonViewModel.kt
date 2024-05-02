package com.maxim.diaryforstudents.performance.common.presentation

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PerformanceCommonViewModel @Inject constructor(
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), GoBack {

    override fun goBack() {
        navigation.update(Screen.Pop)
    }
}