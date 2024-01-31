package com.maxim.diaryforstudents.performance.analytics.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

class AnalyticsViewModel(
    private val interactor: PerformanceInteractor,
    private val analyticsStorage: AnalyticsStorage.Read,
    private val communication: AnalyticsCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : BaseViewModel(), Communication.Observe<AnalyticsState>, Reload, GoBack {
    private var quarter = 1
    private var lessonName = ""

    fun init(isFirstRun: Boolean, isDependent: Boolean) {
        if (isFirstRun && isDependent) {
            lessonName = analyticsStorage.read()
        }
        if (isFirstRun) {
            quarter = interactor.actualQuarter()
        }
        reload()
    }

    fun changeQuarter(value: Int) {
        quarter = value
        reload()
    }

    override fun reload() {
        communication.update(AnalyticsState.Loading)
        handle({ interactor.analytics(quarter, lessonName, 1) }) {
            if (it.first().message().isNotEmpty())
                communication.update(AnalyticsState.Error(it.first().message()))
            else
                communication.update(
                    AnalyticsState.Base(
                        it.map { it.toUi() },
                        lessonName
                    )
                )
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<AnalyticsState>) {
        communication.observe(owner, observer)
    }

    override fun goBack() {
        analyticsStorage.clear()
        navigation.update(Screen.Pop)
        clearViewModel.clearViewModel(AnalyticsViewModel::class.java)
    }
}