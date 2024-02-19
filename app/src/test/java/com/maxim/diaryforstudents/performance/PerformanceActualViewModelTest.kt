package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakePerformanceInteractor
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import junit.framework.TestCase.assertEquals
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
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                false,
                ProgressType.AWeekAgo
            )
        )

        interactor.progressTypeMustReturn(ProgressType.AMonthAgo)
        viewModel.reload()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                false,
                ProgressType.AMonthAgo
            )
        )
    }

    @Test
    fun test_init_first_run_data_is_not_empty() {
        interactor.dataIsEmptyMustReturn(false)
        viewModel.init(true)
        communication.checkCalledTimes(2)
        interactor.checkCurrentQuarterCalledTimes(1)
        communication.checkCalledWith(
            listOf(
                PerformanceState.Loading, PerformanceState.Base(
                    0,
                    listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                    false,
                    ProgressType.Hide
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
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                false,
                ProgressType.Hide
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
                listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                false,
                ProgressType.Hide
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
                listOf(PerformanceUi.Mark(5, "12.34.5678", "lesson name", false)),
                false,
                ProgressType.Hide
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

private class FakePerformanceCommunication : PerformanceCommunication {
    private val list = mutableListOf<PerformanceState>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: PerformanceState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledWith(expected: List<PerformanceState>) {
        assertEquals(expected, list)
    }

    override fun update(value: PerformanceState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        throw IllegalStateException("not used in tests")
    }

    private var key = ""
    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.key = key
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        assertEquals(this.key, key)
        assertEquals(this.bundleWrapper, bundleWrapper)
        restoreCounter++
    }

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }
}