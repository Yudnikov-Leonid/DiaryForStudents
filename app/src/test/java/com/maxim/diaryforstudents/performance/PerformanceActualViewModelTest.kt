package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import org.junit.Before
import org.junit.Test

class PerformanceActualViewModelTest {
    private lateinit var viewModel: PerformanceActualViewModel
    private lateinit var interactor: FakePerformanceInteractor
    private lateinit var communication: FakePerformanceCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun setUp() {
        interactor = FakePerformanceInteractor()
        communication = FakePerformanceCommunication()
        navigation = FakeNavigation(Order())
        clearViewModel = FakeClearViewModel(Order())
        runAsync = FakeRunAsync()
        viewModel = PerformanceActualViewModel(
            interactor,
            communication,
            CalculateStorage.Base(),
            LessonDetailsStorage.Base(),
            AnalyticsStorage.Base(),
            navigation,
            PerformanceDomainToUiMapper(),
            DiaryDomainToUiMapper(PerformanceDomainToUiMapper()),
            runAsync
        )
    }

    @Test
    fun test_reload_failure() {
        interactor.actualDataMustReturnFail("some error message")
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Error("some error message"))
    }

    @Test
    fun test_reload_success() {
        interactor.progressTypeMustReturn(ProgressType.AWeekAgo)
        interactor.showTypeMustReturn(true)
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(
                    PerformanceUi.Mark(
                        5,
                        MarkType.Current,
                        "12.34.5678",
                        "lesson name",
                        false,
                        true,
                    )
                ),
                false,
                ProgressType.AWeekAgo,
                true
            )
        )

        interactor.progressTypeMustReturn(ProgressType.AMonthAgo)
        interactor.showTypeMustReturn(false)
        viewModel.reload()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(
                    PerformanceUi.Mark(
                        5,
                        MarkType.Current,
                        "12.34.5678",
                        "lesson name",
                        false,
                        true
                    )
                ),
                false,
                ProgressType.AMonthAgo,
                false
            )
        )
    }

    @Test
    fun test_init_first_run_data_is_not_empty() {
        interactor.dataIsEmptyMustReturn(false)
        viewModel.init(true)
        interactor.checkCurrentQuarterCalledTimes(1)
        interactor.checkChangeQuarterCalledTimes(1)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            listOf(
                PerformanceState.Loading, PerformanceState.Base(
                    0,
                    listOf(
                        PerformanceUi.Mark(
                            5,
                            MarkType.Current,
                            "12.34.5678",
                            "lesson name",
                            false,
                            true
                        )
                    ),
                    false,
                    ProgressType.Hide,
                    true
                )
            )
        )
    }

    @Test
    fun test_init_first_run_data_is_empty() {
        interactor.dataIsEmptyMustReturn(true)
        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)

        interactor.dataIsEmptyRunCallback()
        interactor.checkCurrentQuarterCalledTimes(1)
        interactor.checkChangeQuarterCalledTimes(1)
        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(
                    PerformanceUi.Mark(
                        5,
                        MarkType.Current,
                        "12.34.5678",
                        "lesson name",
                        false,
                        true
                    )
                ),
                false,
                ProgressType.Hide,
                true
            )
        )
    }

    @Test
    fun test_init_not_first_run() {
        viewModel.init(false)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(
                    PerformanceUi.Mark(
                        5,
                        MarkType.Current,
                        "12.34.5678",
                        "lesson name",
                        false,
                        true
                    )
                ),
                false,
                ProgressType.Hide,
                true
            )
        )
    }

    @Test
    fun test_change_quarter() {
        viewModel.changeQuarter(2)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)
        interactor.checkChangeQuarterCalledTimes(1)
        interactor.checkChangeQuarterCalledWith(2)

        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                2,
                listOf(
                    PerformanceUi.Mark(
                        5,
                        MarkType.Current,
                        "12.34.5678",
                        "lesson name",
                        false,
                        true
                    )
                ),
                false,
                ProgressType.Hide,
                true
            )
        )
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        communication.checkSaveCalledTimes(1)
        viewModel.restore(bundleWrapper)
        communication.checkRestoreCalledTimes(1)
    }
}