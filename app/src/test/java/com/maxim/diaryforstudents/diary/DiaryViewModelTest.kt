//package com.maxim.diaryforstudents.diary
//
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.Observer
//import com.maxim.diaryforstudents.core.presentation.RunAsync
//import com.maxim.diaryforstudents.core.presentation.Screen
//import com.maxim.diaryforstudents.diary.data.DiaryData
//import com.maxim.diaryforstudents.diary.data.DiaryRepository
//import com.maxim.diaryforstudents.diary.presentation.DayUi
//import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
//import com.maxim.diaryforstudents.diary.presentation.DiaryState
//import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
//import com.maxim.diaryforstudents.fakes.CLEAR
//import com.maxim.diaryforstudents.fakes.COMMUNICATION
//import com.maxim.diaryforstudents.fakes.FakeClearViewModel
//import com.maxim.diaryforstudents.fakes.FakeNavigation
//import com.maxim.diaryforstudents.fakes.FakeRunAsync
//import com.maxim.diaryforstudents.fakes.NAVIGATION
//import com.maxim.diaryforstudents.fakes.Order
//import com.maxim.diaryforstudents.fakes.REPOSITORY
//import com.maxim.diaryforstudents.news.presentation.Reload
//import junit.framework.TestCase.assertEquals
//import org.junit.Before
//import org.junit.Test
//
//class DiaryViewModelTest {
//    private lateinit var viewModel: DiaryViewModel
//    private lateinit var repository: FakeDiaryRepository
//    private lateinit var communication: FakeDiaryCommunication
//    private lateinit var navigation: FakeNavigation
//    private lateinit var clear: FakeClearViewModel
//    private lateinit var order: Order
//    private lateinit var runAsync: RunAsync
//
//    @Before
//    fun before() {
//        order = Order()
//        repository = FakeDiaryRepository(order)
//        communication = FakeDiaryCommunication(order)
//        navigation = FakeNavigation(order)
//        clear = FakeClearViewModel(order)
//        runAsync = FakeRunAsync()
//        viewModel = DiaryViewModel(repository, communication, navigation, clear, runAsync)
//    }
//
//    @Test
//    fun test_init() {
//        repository.returnActualDay(100)
//        viewModel.init(true)
//        communication.checkCalledWith(DiaryState.Progress)
//        repository.checkActualDayCalledTimes(1)
//        repository.checkInitCalledWith(viewModel, 13)
//        order.check(listOf(COMMUNICATION, REPOSITORY, REPOSITORY))
//
//        viewModel.init(false)
//        repository.checkInitCalledWith(viewModel, 13)
//        order.check(listOf(COMMUNICATION, REPOSITORY, REPOSITORY, REPOSITORY))
//    }
//
//    @Test
//    fun test_back() {
//        viewModel.back()
//        navigation.checkCalledWith(Screen.Pop)
//        clear.checkCalledWith(DiaryViewModel::class.java)
//        order.check(listOf(NAVIGATION, CLEAR))
//    }
//}
//
//private class FakeDiaryCommunication(private val order: Order) : DiaryCommunication.Mutable {
//    private val list = mutableListOf<DiaryState>()
//    override fun update(value: DiaryState) {
//        order.add(COMMUNICATION)
//        list.add(value)
//    }
//
//    fun checkCalledWith(expected: DiaryState) {
//        assertEquals(expected, list.last())
//    }
//
//    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
//        throw IllegalStateException("not using in test")
//    }
//}
//
//private class FakeDiaryRepository(private val order: Order) : DiaryRepository {
//    private lateinit var actualReload: Reload
//    private var actualWeek = -1
//    private var actualDay = -1
//    private var actualDayCounter = 0
//    override suspend fun init(reload: Reload, week: Int) {
//        actualReload = reload
//        actualWeek = week
//        order.add(REPOSITORY)
//    }
//
//    fun checkInitCalledWith(expectedReload: Reload, expectedWeek: Int) {
//        assertEquals(expectedReload, actualReload)
//        assertEquals(expectedWeek, actualWeek)
//    }
//
//    fun checkActualDayCalledTimes(expected: Int) {
//        assertEquals(expected, actualDayCounter)
//    }
//
//    override fun data(date: Int): DiaryData.Day {
//        TODO("Not yet implemented")
//    }
//
//    fun returnActualDay(day: Int) {
//        actualDay = day
//    }
//
//    override fun actualDate(): Int {
//        order.add(REPOSITORY)
//        actualDayCounter++
//        return actualDay
//    }
//
//    override fun dayList(today: Int): List<DayUi> {
//        TODO("Not yet implemented")
//    }
//}