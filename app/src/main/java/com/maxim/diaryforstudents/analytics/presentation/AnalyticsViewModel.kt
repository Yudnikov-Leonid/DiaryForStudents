package com.maxim.diaryforstudents.analytics.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.GoBack
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor

class AnalyticsViewModel(
    private val interactor: PerformanceInteractor,
    private val analyticsStorage: AnalyticsStorage.Read,
    private val communication: AnalyticsCommunication,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<AnalyticsState>, Reload, GoBack, SaveAndRestore {
    private var quarter = 1
    private var lessonName = ""
    private var interval = 1

    fun init(isFirstRun: Boolean) {
        lessonName = analyticsStorage.read().first
        if (isFirstRun) {
            quarter = analyticsStorage.read().second
            if (quarter == -1)
                quarter = interactor.currentQuarter()
            reload()
        }
    }

    fun changeQuarter(value: Int) {
        quarter = value
        reload()
    }

    fun changeInterval(value: Int) {
        interval = value
        reload()
    }

    override fun reload() {
        communication.update(AnalyticsState.Loading)
        handle({ interactor.analytics(quarter, lessonName, interval, lessonName.isEmpty()) }) { list ->
            if (list.first().message().isNotEmpty())
                communication.update(AnalyticsState.Error(list.first().message()))
            else {
                val newList = mutableListOf<AnalyticsUi>()
                newList.add(AnalyticsUi.Title(lessonName))
                newList.addAll(list.map { it.toUi() })
                communication.update(AnalyticsState.Base(newList))
            }
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

    companion object {
        private const val RESTORE_KEY = "inner_analytics_restore"
        private const val QUARTER_RESTORE_KEY = "inner_quarter_analytics_restore"
        private const val INTERVAL_RESTORE_KEY = "inner_interval_analytics_restore"
    }
}