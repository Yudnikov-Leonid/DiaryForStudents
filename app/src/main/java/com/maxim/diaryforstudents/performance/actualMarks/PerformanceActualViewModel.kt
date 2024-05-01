package com.maxim.diaryforstudents.performance.actualMarks

import com.maxim.diaryforstudents.actualPerformanceSettings.presentation.ActualSettingsScreen
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.calculateAverage.presentation.CalculateScreen
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.core.presentation.SerializableLambda
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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PerformanceActualViewModel @Inject constructor(
    private val interactor: PerformanceInteractor,
    private val communication: PerformanceCommunication,
    private val calculateStorage: CalculateStorage.Save,
    private val detailsStorage: LessonDetailsStorage.Save,
    private val analyticsStorage: AnalyticsStorage.Save,
    private val navigation: Navigation.Update,
    mapper: PerformanceDomain.Mapper<PerformanceUi>,
    private val diaryMapper: DiaryDomain.Mapper<DiaryUi>,
    runAsync: RunAsync = RunAsync.Base()
) : PerformanceMarkViewModel(interactor, communication, mapper, runAsync), Init, SaveAndRestore {
    override val type = MarksType.Base
    private var lastOpenDetailsTime = 0L

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(PerformanceState.Loading)
            if (!interactor.dataIsLoading {
                    quarter = interactor.currentQuarter()
                    handle ({ interactor.changeQuarter(quarter) } ) {
                        reload()
                    }
                }) {
                quarter = interactor.currentQuarter()
                handle ({ interactor.changeQuarter(quarter) } ) {
                    reload()
                }
            }
        } else
            reload()
    }

    //not tested
    fun retry() {
        handle ({ interactor.loadData() } ) {
            reload()
        }
    }

    fun changeQuarter(quarter: Int) {
        this.quarter = quarter
        communication.update(PerformanceState.Loading)
        handle({ interactor.changeQuarter(quarter) }) {
            reload()
        }
    }

    //not tested
    fun openDetails(mark: PerformanceUi) {
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

    //not tested
    fun calculateAverage(marks: List<PerformanceUi>, marksSum: Int) {
        calculateStorage.save(marks, marksSum)
        navigation.update(CalculateScreen)
    }

    //not tested
    fun analytics(lessonName: String) {
        analyticsStorage.save(lessonName, quarter)
        navigation.update(AnalyticsScreen)
    }

    //not tested
    fun settings() {
        navigation.update(ActualSettingsScreen(object : SerializableLambda {
            override fun invoke() {
                reload()
            }
        }))
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(RESTORE_KEY, bundleWrapper)
        bundleWrapper.save(QUARTER_KEY, quarter)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(RESTORE_KEY, bundleWrapper)
        quarter = bundleWrapper.restore<Int>(QUARTER_KEY) ?: interactor.currentQuarter()
    }

    companion object {
        private const val RESTORE_KEY = "performance_actual_communication_key"
        private const val QUARTER_KEY = "performance_actual_quarter_key"
    }
}