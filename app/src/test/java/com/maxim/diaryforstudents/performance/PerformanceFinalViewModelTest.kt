package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import com.maxim.diaryforstudents.performance.finalMarks.PerformanceFinalViewModel
import org.junit.Before
import org.junit.Test

class PerformanceFinalViewModelTest {
    private lateinit var viewModel: PerformanceFinalViewModel
    private lateinit var interactor: FakePerformanceInteractor
    private lateinit var communication: FakePerformanceCommunication

    @Before
    fun setUp() {
        interactor = FakePerformanceInteractor()
        communication = FakePerformanceCommunication()
        viewModel =
            PerformanceFinalViewModel(interactor, communication, PerformanceDomainToUiMapper())
    }

    @Test
    fun test_reload_data_is_not_empty_success() {
        interactor.dataIsEmptyMustReturn(false)
        interactor.progressTypeMustReturn(ProgressType.AWeekAgo)
        viewModel.reload()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            listOf(
                PerformanceState.Loading,
                PerformanceState.Base(
                    0,
                    listOf(PerformanceUi.Mark(4, "12.34.5678", "lesson name", true)),
                    true,
                    ProgressType.AWeekAgo
                )
            )
        )
    }

    @Test
    fun test_reload_data_is_not_empty_failure() {
        interactor.dataIsEmptyMustReturn(false)
        interactor.progressTypeMustReturn(ProgressType.AWeekAgo)
        interactor.finalDataMustReturnFail("some error message")
        viewModel.reload()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(PerformanceState.Error("some error message"))
    }

    @Test
    fun test_reload_data_is_empty_failure() {
        interactor.dataIsEmptyMustReturn(true)
        interactor.progressTypeMustReturn(ProgressType.TwoWeeksAgo)
        interactor.finalDataMustReturnFail("some error message")
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)

        interactor.dataIsEmptyRunCallback()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Error("some error message")
        )
    }

    @Test
    fun test_reload_data_is_empty_success() {
        interactor.dataIsEmptyMustReturn(true)
        interactor.progressTypeMustReturn(ProgressType.TwoWeeksAgo)
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)

        interactor.dataIsEmptyRunCallback()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(PerformanceUi.Mark(4, "12.34.5678", "lesson name", true)),
                true,
                ProgressType.TwoWeeksAgo
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