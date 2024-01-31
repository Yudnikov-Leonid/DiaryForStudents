package com.maxim.diaryforstudents.performance.actualMarks.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsScreen
import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.SaveActualSettingsCommunication
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateScreen
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.lessonDetails.bottomFragment.LessonDetailsBottomFragmentScreen
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.performance.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.performance.analytics.presentation.AnalyticsViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarksType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceMarkViewModel
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi

class PerformanceActualViewModel(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    private val reloadCommunication: SaveActualSettingsCommunication.Save,
    private val calculateStorage: CalculateStorage.Save,
    private val detailsStorage: LessonDetailsStorage.Save,
    private val analyticsStorage: AnalyticsStorage.Save,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel,
    mapper: PerformanceDomain.Mapper<PerformanceUi>,
    private val diaryMapper: DiaryDomain.Mapper<DiaryUi>
) : PerformanceMarkViewModel(interactor, communication, mapper), Init, SaveAndRestore {
    override val type = MarksType.Base
    private var lastOpenDetailsTime = 0L

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            reloadCommunication.setCallback(this)
            communication.update(PerformanceState.Loading)
            handle({
                if (interactor.finalDataIsEmpty())
                    interactor.initFinal()
                interactor.initActual()
            }) {
                quarter = interactor.actualQuarter()
                reload()
            }
        } else
            reload()
    }

    fun openDetails(mark: PerformanceUi.Mark) {
        if (System.currentTimeMillis() - 500 > lastOpenDetailsTime) {
            lastOpenDetailsTime = System.currentTimeMillis()
            navigation.update(LessonDetailsBottomFragmentScreen)
            handle({
                mark.getLesson(interactor, diaryMapper)
            }) {
                detailsStorage.save(it)
            }
        }
    }

    fun changeQuarter(quarter: Int) {
        this.quarter = quarter
        communication.update(PerformanceState.Loading)
        handle({ interactor.changeQuarter(quarter) }) {
            reload()
        }
    }

    fun calculateAverage(marks: List<PerformanceUi.Mark>, marksSum: Int) {
        calculateStorage.save(marks, marksSum)
        navigation.update(CalculateScreen)
    }

    fun analytics(lessonName: String) {
        analyticsStorage.save(lessonName)
        clearViewModel.clearViewModel(AnalyticsViewModel::class.java)
        navigation.update(AnalyticsScreen)
    }

    fun settings() {
        navigation.update(ActualSettingsScreen)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(QUARTER_KEY, quarter)
        interactor.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
        quarter = bundleWrapper.restore<Int>(QUARTER_KEY) ?: interactor.actualQuarter()
        interactor.restore(bundleWrapper)
    }

    companion object {
        private const val RESTORE_KEY = "performance_actual_communication_key"
        private const val QUARTER_KEY = "performance_actual_quarter_key"
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }
}