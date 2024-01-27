package com.maxim.diaryforstudents.diary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.diary.domain.DayDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryState
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.STORAGE
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

//todo not everything is tested
class DiaryViewModelTest {
    private lateinit var viewModel: DiaryViewModel
    private val order = Order()
    private val interactor = FakeDiaryInteractor()
    private val communication = FakeDiaryCommunication()
    private val storage = FakeLessonDetailsStorage(order)
    private val navigation = FakeNavigation(order)
    private val clearViewModel = FakeClearViewModel(order)
    private val runAsync = FakeRunAsync()

    @Before
    fun setUp() {
        viewModel = DiaryViewModel(
            ArrayList(),
            interactor,
            communication,
            storage,
            navigation,
            clearViewModel,
            runAsync
        )
    }

    @Test
    fun test_init_and_reload() {
        interactor.actualDateMustReturn(55)

        viewModel.init(true)
        interactor.checkActualDateCalledTimes(1)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(DiaryState.Progress)

        //return()

        interactor.checkDayCalledTimes(1)
        interactor.checkActualDayCalledWith(55)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(55, emptyList()),
                emptyList(),
                0,
                false
            )
        )
    }

    @Test
    fun test_false_init() {
        viewModel.init(false)
        interactor.checkActualDateCalledTimes(0)
    }

    @Test
    fun test_go_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(DiaryViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_open_details() {
        val fakeLesson = DiaryUi.Lesson(
            "name",
            "teacher name", "topic", "homework",
            "previous homework", "start time", "end time", 5, emptyList()
        )
        viewModel.openDetails(fakeLesson)
        navigation.checkCalledWith(LessonDetailsScreen)
        storage.checkCalledWith(fakeLesson)
        order.check(listOf(STORAGE, NAVIGATION))
    }

    @Test
    fun test_homework_to_share() {
        viewModel.setActualDay(5)
        interactor.homeworksToShareMustReturn("12345-")
        val actual = viewModel.homeworkToShare()
        assertEquals("12345-5", actual)
    }

    @Test
    fun test_previous_homework_to_share() {
        viewModel.setActualDay(7)
        interactor.previousHomeworksToShareMustReturn("123456-")
        val actual = viewModel.previousHomeworkToShare()
        assertEquals("123456-7", actual)
    }

    @Test
    fun test_homework_from() {
        interactor.homeworkFromMustReturn(false)
        var actual = viewModel.homeworkFrom()
        assertEquals(false, actual)

        interactor.homeworkFromMustReturn(true)
        actual = viewModel.homeworkFrom()
        assertEquals(true, actual)
    }
}

private class FakeLessonDetailsStorage(private val order: Order) : LessonDetailsStorage.Save {
    private lateinit var value: DiaryUi.Lesson

    override fun save(value: DiaryUi.Lesson) {
        order.add(STORAGE)
        this.value = value
    }

    fun checkCalledWith(expected: DiaryUi.Lesson) {
        assertEquals(expected, value)
    }
}

private class FakeDiaryInteractor : DiaryInteractor {
    override fun dayList(today: Int): List<DayDomain> = emptyList()

    private val dayList = mutableListOf<Int>()
    override suspend fun day(date: Int): DiaryDomain.Day {
        dayList.add(date)
        return DiaryDomain.Day(date, emptyList())
    }

    fun checkDayCalledTimes(expected: Int) {
        assertEquals(expected, dayList.size)
    }

    fun checkActualDayCalledWith(expected: Int) {
        assertEquals(expected, dayList.last())
    }

    override fun cachedDay(date: Int): DiaryDomain.Day {
        TODO("Not yet implemented")
    }

    private var actualDateValue = 0
    private var actualDateCounter = 0
    override fun actualDate(): Int {
        actualDateCounter++
        return actualDateValue
    }

    fun actualDateMustReturn(value: Int) {
        actualDateValue = value
    }

    fun checkActualDateCalledTimes(expected: Int) {
        assertEquals(expected, actualDateCounter)
    }

    private var homeworkValue = ""
    override fun homeworks(date: Int) = "$homeworkValue$date"

    fun homeworksToShareMustReturn(value: String) {
        homeworkValue = value
    }

    private var previousHomeworkValue = ""
    override fun previousHomeworks(date: Int) = "$previousHomeworkValue$date"

    fun previousHomeworksToShareMustReturn(value: String) {
        previousHomeworkValue = value
    }

    override fun saveFilters(booleanArray: BooleanArray) {
        TODO("Not yet implemented")
    }

    override fun filters() = booleanArrayOf()

    override fun saveHomeworkFrom(value: Boolean) {
        TODO("Not yet implemented")
    }

    private var homeworkFromValue = false
    override fun homeworkFrom() = homeworkFromValue

    fun homeworkFromMustReturn(value: Boolean) {
        homeworkFromValue = value
    }
}

private class FakeDiaryCommunication : DiaryCommunication {
    private val list = mutableListOf<DiaryState>()

    override fun update(value: DiaryState) {
        list.add(value)
    }

    fun checkCalledWith(expected: DiaryState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        throw IllegalStateException("not using in tests")
    }
}