package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarksType
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceUi
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PerformanceViewModelTest {
    private lateinit var viewModel: PerformanceViewModel
    private lateinit var interactor: FakePerformanceInteractor
    private lateinit var communication: FakePerformanceCommunication
    private val order = Order()
    private val navigation = FakeNavigation(order)
    private val clearViewModel = FakeClearViewModel(order)
    private val runAsync = FakeRunAsync()

    @Before
    fun setUp() {
        interactor = FakePerformanceInteractor()
        communication = FakePerformanceCommunication()
        viewModel = PerformanceViewModel(
            interactor,
            communication,
            navigation,
            clearViewModel,
            PerformanceDomainToUiMapper(), runAsync
        )
    }

    @Test
    fun test_init_success() {
        interactor.actualQuarterMustReturn(3)
        interactor.initMustThrowError(false)

        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)
        interactor.checkInitCalledTimes(1)

        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                3,
                listOf(PerformanceUi.Lesson("Lesson name", emptyList(), false, 5.0f)),
                false
            )
        )
    }

    @Test
    fun test_init_fail() {
        interactor.actualQuarterMustReturn(3)
        interactor.initMustThrowError(true)

        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)
        interactor.checkInitCalledTimes(1)

        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(PerformanceState.Error("error message from repository"))
    }

    @Test
    fun test_search_actual_marks() {
        viewModel.changeQuarter(2)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        interactor.checkActualSearchCalledTimes(1)

        viewModel.search("text")
        interactor.checkActualSearchCalledTimes(2)
        interactor.checkActualSearchCalledWith("text")

        communication.checkCalledTimes(3)
        communication.checkCalledWith(
            PerformanceState.Base(
                2,
                listOf(PerformanceUi.Lesson("Lesson name", emptyList(), false, 5.0f)),
                false
            )
        )
    }

    @Test
    fun test_search_final_marks() {
        viewModel.changeQuarter(2)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        interactor.checkActualSearchCalledTimes(1)

        viewModel.changeType(MarksType.Final)
        communication.checkCalledTimes(3)


        viewModel.search("text")
        interactor.checkFinalSearchCalledTimes(2)
        interactor.checkFinalSearchCalledWith("text")

        communication.checkCalledTimes(4)
        communication.checkCalledWith(
            PerformanceState.Base(
                2,
                listOf(PerformanceUi.Lesson("Lesson name", emptyList(), true, 5.0f)),
                true
            )
        )
    }

    @Test
    fun test_go_back_empty_search() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(PerformanceViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_go_back_not_empty_search() {
        viewModel.search("22")
        viewModel.goBack()
        order.check(emptyList())
        communication.checkCalledWith(
            PerformanceState.Base(
                0,
                listOf(PerformanceUi.Lesson("Lesson name", emptyList(), false, 5.0f)),
                false
            )
        )
    }

    @Test
    fun test_change_quarter() {
        viewModel.changeQuarter(3)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)
        interactor.checkChangeQuarterCalledTimes(1)
        interactor.checkChangeQuarterCalledWith(3)

        runAsync.returnResult()

        communication.checkCalledWith(
            PerformanceState.Base(
                3,
                listOf(PerformanceUi.Lesson("Lesson name", emptyList(), false, 5.0f)),
                false
            )
        )
    }
}

private class FakePerformanceCommunication : PerformanceCommunication {
    private val list = mutableListOf<PerformanceState>()

    override fun update(value: PerformanceState) {
        list.add(value)
    }

    fun checkCalledWith(expected: PerformanceState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        throw IllegalStateException("not using in tests")
    }
}

private class FakePerformanceInteractor : PerformanceInteractor {

    private var initCounter = 0
    private var initThrowError = false
    override suspend fun initActual() {
        initCounter++
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initCounter)
    }

    fun initMustThrowError(value: Boolean) {
        initThrowError = value
    }

    private val cachedDataList = mutableListOf<String>()
    override fun data(search: String): List<PerformanceDomain> {
        cachedDataList.add(search)
        return if (initThrowError)
            listOf(PerformanceDomain.Error("error message from repository"))
        else
            listOf(PerformanceDomain.Lesson("Lesson name", emptyList(), false, 5.0f))
    }

    fun checkActualSearchCalledTimes(expected: Int) {
        assertEquals(expected, cachedDataList.size)
    }

    fun checkActualSearchCalledWith(expected: String) {
        assertEquals(expected, cachedDataList.last())
    }

    private val cachedFinalDataList = mutableListOf<String>()
    override fun finalData(search: String): List<PerformanceDomain> {
        cachedFinalDataList.add(search)
        return listOf(PerformanceDomain.Lesson("Lesson name", emptyList(), true, 5.0f))
    }

    fun checkFinalSearchCalledTimes(expected: Int) {
        assertEquals(expected, cachedFinalDataList.size)
    }

    fun checkFinalSearchCalledWith(expected: String) {
        assertEquals(expected, cachedFinalDataList.last())
    }

    private val changeQuarterList = mutableListOf<Int>()
    override suspend fun changeQuarter(quarter: Int) {
        changeQuarterList.add(quarter)
    }

    fun checkChangeQuarterCalledTimes(expected: Int) {
        assertEquals(expected, changeQuarterList.size)
    }

    fun checkChangeQuarterCalledWith(expected: Int) {
        assertEquals(expected, changeQuarterList.last())
    }

    private var actualQuarterValue = 0
    override fun actualQuarter() = actualQuarterValue

    fun actualQuarterMustReturn(value: Int) {
        actualQuarterValue = value
    }
}