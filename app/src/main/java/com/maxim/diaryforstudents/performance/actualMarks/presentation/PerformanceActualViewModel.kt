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
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.lessonDetails.bottomFragment.LessonDetailsBottomFragmentScreen
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
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
    private val navigation: Navigation.Update,
    mapper: PerformanceDomain.Mapper<PerformanceUi>,
    private val diaryMapper: DiaryDomain.Mapper<DiaryUi>
) : PerformanceMarkViewModel(interactor, communication, mapper), Init, SaveAndRestore {

    override val type = MarksType.Base

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            reloadCommunication.setCallback(this)
            quarter = interactor.actualQuarter()
            communication.update(PerformanceState.Loading)
            handle({
                interactor.initFinal()
                interactor.initActual()
            }) {
                reload()
            }
        }
    }

    fun openDetails(mark: PerformanceUi.Mark) {
        handle({
            detailsStorage.save(mark.getLesson(interactor, diaryMapper))
        }) {
            navigation.update(LessonDetailsBottomFragmentScreen)
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

    fun settings() {
        navigation.update(ActualSettingsScreen)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(QUARTER_KEY, quarter)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
        quarter = bundleWrapper.restore<Int>(QUARTER_KEY) ?: interactor.actualQuarter()
        handle({ interactor.initActual() }) {}
    }

    companion object {
        private const val RESTORE_KEY = "performance_communication_key"
        private const val QUARTER_KEY = "performance_quarter_key"
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }
}