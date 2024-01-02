package com.maxim.diaryforstudents.diary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.diary.data.DayData
import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.presentation.DayUi
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryState
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.COMMUNICATION
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.REPOSITORY
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class DiaryViewModelTest {
    private lateinit var viewModel: DiaryViewModel
    private lateinit var repository: FakeDiaryRepository
    private lateinit var communication: FakeDiaryCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var runAsync: RunAsync

    @Before
    fun before() {
        order = Order()
        repository = FakeDiaryRepository(order)
        communication = FakeDiaryCommunication(order)
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = DiaryViewModel(repository, communication, navigation, clear, runAsync)
    }

    @Test
    fun test_init() {
        repository.returnActualDay(100)
        viewModel.init()
        communication.checkCalledWith(DiaryState.Progress)
        repository.checkActualDayCalledTimes(1)
        repository.checkInitCalledWith(viewModel, 13)
        order.check(listOf(COMMUNICATION, REPOSITORY, REPOSITORY))
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(DiaryViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_error() {
        viewModel.error("message")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(DiaryState.Error("message"))
    }

    @Test
    fun test_reload() {
        repository.dateMustReturn(DiaryData.Day(12, emptyList()))
        repository.dayListMustReturn(listOf(DayData(12, false)))
        viewModel.reload()
        repository.checkDateCalledTimes(1)
        repository.checkDateCalledWith(0)
        repository.checkDayListCalledTimes(1)
        repository.checkDayListCalledWith(0)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(12, emptyList()), listOf(DayUi(12, false))
            )
        )
    }

    @Test
    fun test_set_actual_day() {
        repository.dateMustReturn(DiaryData.Day(55, emptyList()))
        repository.dayListMustReturn(listOf(DayData(55, false)))
        viewModel.setActualDay(55)
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith(viewModel, 7)
    }

    @Test
    fun test_next_day() {
        repository.dateMustReturn(DiaryData.Day(55, emptyList()))
        repository.dayListMustReturn(listOf(DayData(55, false)))
        repository.returnActualDay(15)
        viewModel.init()
        viewModel.nextDay()
        repository.checkDateCalledTimes(1)
        repository.checkDateCalledWith(16)
        repository.checkDayListCalledTimes(1)
        repository.checkDayListCalledWith(16)
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(55, emptyList()), listOf(DayUi(55, false))
            )
        )
    }

    @Test
    fun test_previous_day() {
        repository.dateMustReturn(DiaryData.Day(55, emptyList()))
        repository.dayListMustReturn(listOf(DayData(55, false)))
        repository.returnActualDay(15)
        viewModel.init()
        viewModel.previousDay()
        repository.checkDateCalledTimes(1)
        repository.checkDateCalledWith(14)
        repository.checkDayListCalledTimes(1)
        repository.checkDayListCalledWith(14)
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(55, emptyList()), listOf(DayUi(55, false))
            )
        )
    }

    @Test
    fun test_change_week() {
        repository.dateMustReturn(DiaryData.Day(223, emptyList()))
        repository.dayListMustReturn(listOf(DayData(223, false)))
        repository.returnActualDay(17)
        viewModel.init()

        communication.checkCalledTimes(1)
        viewModel.nextDay()
        repository.checkInitCalledTimes(2)
        repository.checkInitCalledWith(viewModel, 2)
        communication.checkCalledTimes(1)

        communication.checkCalledTimes(1)
        viewModel.previousDay()
        repository.checkInitCalledTimes(3)
        repository.checkInitCalledWith(viewModel, 1)
        communication.checkCalledTimes(1)

        communication.checkCalledTimes(1)
        viewModel.previousDay()
        repository.checkInitCalledTimes(3)
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(223, emptyList()), listOf(DayUi(223, false))
            )
        )
    }
}

private class FakeDiaryCommunication(private val order: Order) : DiaryCommunication {
    private val list = mutableListOf<DiaryState>()
    private var counter = 0
    override fun update(value: DiaryState) {
        order.add(COMMUNICATION)
        list.add(value)
        counter++
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    fun checkCalledWith(expected: DiaryState) {
        assertEquals(expected, list.last())
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        throw IllegalStateException("not using in test")
    }

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        //todo test
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        //todo test
    }
}

private class FakeDiaryRepository(private val order: Order) : DiaryRepository {
    private lateinit var actualReload: Reload
    private var actualWeek = -1
    private var actualDay = -1
    private var actualDayCounter = 0
    private var initCounter = 0
    override suspend fun init(reload: Reload, week: Int) {
        actualReload = reload
        actualWeek = week
        initCounter++
        order.add(REPOSITORY)
    }

    fun checkInitCalledWith(expectedReload: Reload, expectedWeek: Int) {
        assertEquals(expectedReload, actualReload)
        assertEquals(expectedWeek, actualWeek)
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initCounter)
    }

    fun checkActualDayCalledTimes(expected: Int) {
        assertEquals(expected, actualDayCounter)
    }

    fun dateMustReturn(value: DiaryData.Day) {
        dateReturn = value
    }

    private var dateReturn: DiaryData.Day? = null
    private var dateList = mutableListOf<Int>()
    fun checkDateCalledTimes(expected: Int) {
        assertEquals(expected, dateList.size)
    }

    fun checkDateCalledWith(expected: Int) {
        assertEquals(expected, dateList.last())
    }

    override fun date(date: Int): DiaryData.Day {
        dateList.add(date)
        return dateReturn!!
    }

    fun returnActualDay(day: Int) {
        actualDay = day
    }

    override fun actualDate(): Int {
        order.add(REPOSITORY)
        actualDayCounter++
        return actualDay
    }

    fun dayListMustReturn(value: List<DayData>) {
        dayListReturn.addAll(value)
    }

    private var dayListReturn = mutableListOf<DayData>()
    private var dayListList = mutableListOf<Int>()

    fun checkDayListCalledTimes(expected: Int) {
        assertEquals(expected, dayListList.size)
    }

    fun checkDayListCalledWith(expected: Int) {
        assertEquals(expected, dayListList.last())
    }

    override fun dayList(today: Int): List<DayData> {
        dayListList.add(today)
        return dayListReturn
    }
}