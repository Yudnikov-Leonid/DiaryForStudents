package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.analytics.data.AnalyticsStorage
import com.maxim.diaryforstudents.calculateAverage.data.CalculateStorage
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.performance.actualMarks.PerformanceActualViewModel
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
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
    fun test_init_first_run() {
        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)
        interactor.checkCurrentQuarterCalledTimes(1)

        interactor.checkLoadOrder(listOf(FINAL, ACTUAL))
        runAsync.returnResult()

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
        viewModel.restore(bundleWrapper)
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

    override fun update(value: PerformanceState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        throw IllegalStateException("not used in tests")
    }

    private var key = ""
    private var bundleWrapper: BundleWrapper.Mutable? = null

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.key = key
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        assertEquals(this.key, key)
        assertEquals(this.bundleWrapper, bundleWrapper)
    }
}

private class FakePerformanceInteractor : PerformanceInteractor {
    private var currentQuarter = 0
    private val loadList = mutableListOf<String>()

    override suspend fun loadActualData() {
        loadList.add(ACTUAL)
    }

    override suspend fun loadFinalData() {
        loadList.add(FINAL)
    }

    fun checkLoadOrder(expected: List<String>) {
        assertEquals(expected, loadList)
    }

    private var actualDataErrorMessage = ""
    fun actualDataMustReturnFail(message: String) {
        actualDataErrorMessage = message
    }

    override fun actualData(): List<PerformanceDomain> {
        return if (actualDataErrorMessage.isNotEmpty())
            listOf(PerformanceDomain.Error(actualDataErrorMessage))
        else listOf(PerformanceDomain.Mark(5, "12.34.5678", "lesson name", false))
    }

    override fun finalData(): List<PerformanceDomain> {
        TODO("Not yet implemented")
    }

    private var progressTypeValue: ProgressType = ProgressType.Hide
    fun progressTypeMustReturn(value: ProgressType) {
        progressTypeValue = value
    }

    override fun currentProgressType() = progressTypeValue

    private var currentQuarterCounter = 0
    fun checkCurrentQuarterCalledTimes(expected: Int) {
        assertEquals(expected, currentQuarterCounter)
    }

    override fun currentQuarter(): Int {
        currentQuarterCounter++
        return currentQuarter
    }

    override fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson {
        TODO("Not yet implemented")
    }

    private val changeQuarterList = mutableListOf<Int>()
    fun checkChangeQuarterCalledTimes(expected: Int) {
        assertEquals(expected, changeQuarterList.size)
    }

    fun checkChangeQuarterCalledWith(expected: Int) {
        assertEquals(expected, changeQuarterList.last())
    }

    override fun changeQuarter(quarter: Int) {
        changeQuarterList.add(quarter)
        currentQuarter = quarter
    }
}

private const val ACTUAL = "ACTUAL"
private const val FINAL = "ACTUAL"