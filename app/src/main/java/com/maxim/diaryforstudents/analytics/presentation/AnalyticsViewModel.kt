package com.maxim.diaryforstudents.analytics.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

abstract class AnalyticsViewModel(
    private val interactor: PerformanceInteractor,
    private val analyticsStorage: AnalyticsStorage.Read,
    private val communication: AnalyticsCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : BaseViewModel(), Communication.Observe<AnalyticsState>, Reload, GoBack, SaveAndRestore {
    private var quarter = 1
    private var lessonName = ""
    private var interval = 1

    protected abstract val showFinal: Boolean

    fun init(isFirstRun: Boolean, isDependent: Boolean) {
        if (isDependent) {
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

    fun changeInterval(value: Int) {
        interval = when (value) {
            0 -> 1
            1 -> 2
            2 -> 3
            3 -> 7
            else -> 28
        }
        reload()
    }

    override fun reload() {
        communication.update(AnalyticsState.Loading)
        handle({ interactor.analytics(quarter, lessonName, interval, showFinal) }) {
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
        clearViewModel.clearViewModel(AnalyticsNotInnerViewModel::class.java)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        analyticsStorage.save(bundleWrapper)
        communication.save(RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(QUARTER_RESTORE_KEY, quarter)
        bundleWrapper.save(INTERVAL_RESTORE_KEY, interval)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        analyticsStorage.restore(bundleWrapper)
        communication.restore(RESTORE_KEY, bundleWrapper)
        quarter = bundleWrapper.restore(QUARTER_RESTORE_KEY) ?: 1
        interval = bundleWrapper.restore(INTERVAL_RESTORE_KEY) ?: 1
    }

    protected abstract val RESTORE_KEY: String
    protected abstract val QUARTER_RESTORE_KEY: String
    protected abstract val INTERVAL_RESTORE_KEY: String
}