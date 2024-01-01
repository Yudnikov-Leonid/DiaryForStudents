package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.performance.data.PerformanceData
import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.presentation.PerformanceState
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi
import com.maxim.diaryforstudents.performance.presentation.PerformanceViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PerformanceViewModelTest {
    private lateinit var viewModel: PerformanceViewModel
    private lateinit var repository: FakePerformanceRepository
    private lateinit var communication: FakePerformanceCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order

    @Before
    fun before() {
        order = Order()
        repository = FakePerformanceRepository()
        communication = FakePerformanceCommunication()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        viewModel = PerformanceViewModel(repository, communication, navigation, clear)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith(viewModel)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Loading)

        viewModel.init(false)
        repository.checkInitCalledTimes(1)
        communication.checkCalledTimes(1)
    }

    @Test
    fun test_change_quarter() {
        repository.mustReturn(
            listOf(
                PerformanceData.Lesson(
                    "Math",
                    listOf(PerformanceData.Grade(3, 34)), 5f
                )
            )
        )

        viewModel.changeQuarter(2)
        repository.checkChangeQuarterCalledTimes(1)
        repository.checkChangeQuarterCalledWith(2)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            PerformanceState.Base(
                2,
                listOf(PerformanceUi.Lesson("Math", listOf(PerformanceUi.Grade(3, 34)), 5f)),
                true
            )
        )
    }

    @Test
    fun test_reload() {
        repository.mustReturn(
            listOf(
                PerformanceData
                    .Lesson("Math", listOf(PerformanceData.Grade(3, 34)), 5f)
            )
        )
        viewModel.changeQuarter(3)
        viewModel.reload()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            PerformanceState.Base(
                3,
                listOf(PerformanceUi.Lesson("Math", listOf(PerformanceUi.Grade(3, 34)), 5f)),
                true
            )
        )
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(PerformanceViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_error() {
        viewModel.error("error")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(PerformanceState.Error("error"))
    }

    @Test
    fun test_change_type() {
        viewModel.changeType("SOME TYPE")
        repository.checkChangeTypeCalledTimes(1)
        repository.checkChangeTypeCalledWith("SOME TYPE")
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith(viewModel)
    }

    @Test
    fun test_search() {
        viewModel.search("123")
        repository.checkDataCalledTimes(1)
        repository.checkDataCalledWith("123")
        repository.checkActualQuarterCalledTimes(1)
    }
}

private class FakePerformanceCommunication : PerformanceCommunication.Mutable {
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
        throw IllegalStateException("not using in test")
    }
}

private class FakePerformanceRepository: PerformanceRepository {
    private var initCounter = 0
    private lateinit var reload: Reload
    private var quarterCounter = 0
    private var quarterValue = 0
    private val list = mutableListOf<PerformanceData>()
    override fun changeQuarter(new: Int) {
        quarterCounter++
        quarterValue = new
    }

    fun mustReturn(must: List<PerformanceData>) {
        list.addAll(must)
    }

    private val dataList = mutableListOf<String>()

    fun checkDataCalledTimes(expected: Int) {
        assertEquals(expected, dataList.size)
    }

    fun checkDataCalledWith(expected: String) {
        assertEquals(expected, dataList.last())
    }
    override fun data(search: String): List<PerformanceData> {
        dataList.add(search)
        return list
    }

    private var actualQuarterCounter = 0
    fun checkActualQuarterCalledTimes(expected: Int) {
        assertEquals(expected, actualQuarterCounter)
    }
    override fun actualQuarter(): Int {
        actualQuarterCounter++
        return quarterValue
    }

    fun checkChangeQuarterCalledTimes(expected: Int) {
        assertEquals(expected, quarterCounter)
    }

    fun checkChangeQuarterCalledWith(expected: Int) {
        assertEquals(expected, quarterValue)
    }

    override fun init(reload: Reload) {
        initCounter++
        this.reload = reload
    }

    private val changeTypeList = mutableListOf<String>()
    fun checkChangeTypeCalledTimes(expected: Int) {
        assertEquals(expected, changeTypeList.size)
    }

    fun checkChangeTypeCalledWith(expected: String) {
        assertEquals(expected, changeTypeList.last())
    }

    override fun changeType(type: String) {
        changeTypeList.add(type)
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initCounter)
    }

    fun checkInitCalledWith(expected: Reload) {
        assertEquals(expected, reload)
    }
}