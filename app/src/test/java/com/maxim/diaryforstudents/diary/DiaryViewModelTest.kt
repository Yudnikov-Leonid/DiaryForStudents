package com.maxim.diaryforstudents.diary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.diary.domain.DayDomain
import com.maxim.diaryforstudents.diary.domain.DayDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.presentation.DayUi
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryState
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.STORAGE
import com.maxim.diaryforstudents.lessonDetails.data.LessonDetailsStorage
import com.maxim.diaryforstudents.lessonDetails.presentation.LessonDetailsScreen
import com.maxim.diaryforstudents.openNews.Share
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class DiaryViewModelTest {
    private lateinit var viewModel: DiaryViewModel
    private lateinit var interactor: FakeDiaryInteractor
    private lateinit var communication: FakeDiaryCommunication
    private lateinit var storage: FakeLessonDetailsStorage
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun setUp() {
        interactor = FakeDiaryInteractor()
        communication = FakeDiaryCommunication()
        order = Order()
        storage = FakeLessonDetailsStorage(order)
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = DiaryViewModel(
            interactor,
            communication,
            storage,
            navigation,
            clearViewModel,
            DiaryDomainToUiMapper(PerformanceDomainToUiMapper()),
            DayDomainToUiMapper(),
            runAsync
        )
    }

    @Test
    fun test_reload() {
        viewModel.reload(showLoading = true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(DiaryState.Progress)
        interactor.checkDayCalledTimes(1)
        interactor.checkDayCalledWith(0)
        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(
                    0,
                    listOf(
                        DiaryUi.Lesson(
                            "name", 1, "teacher", "topic", "homework",
                            "previousHomework", "startTime", "endTime", 1234,
                            emptyList(), listOf("absence"), listOf("note")
                        )
                    )
                ),
                listOf(DayUi(123, false), DayUi(124, false)),
                listOf(DayUi(125, true), DayUi(126, false)),
                listOf(DayUi(127, false), DayUi(128, false)),
                true
            )
        )
    }

    @Test
    fun test_init() {
        interactor.actualDayMustReturn(55)
        viewModel.init(true)
        runAsync.returnResult()
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(
                    55,
                    listOf(
                        DiaryUi.Lesson(
                            "name", 1, "teacher", "topic", "homework",
                            "previousHomework", "startTime", "endTime", 1234,
                            emptyList(), listOf("absence"), listOf("note")
                        )
                    )
                ),
                listOf(DayUi(123, false), DayUi(124, false)),
                listOf(DayUi(125, true), DayUi(126, false)),
                listOf(DayUi(127, false), DayUi(128, false)),
                true
            )
        )
    }

    @Test
    fun test_reload_failure() {
        interactor.dayMustReturnFail("message")

        viewModel.reload(showLoading = true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(DiaryState.Progress)
        interactor.checkDayCalledTimes(1)
        interactor.checkDayCalledWith(0)
        runAsync.returnResult()

        communication.checkCalledTimes(2)
        communication.checkCalledWith(DiaryState.Error("message"))
    }

    @Test
    fun test_share_homework() {
        interactor.actualDayMustReturn(555)
        viewModel.init(true)

        val fakeShare = FakeShare()
        viewModel.shareHomework(fakeShare)
        interactor.checkHomeworksCalledTimes(1)
        interactor.checkHomeworksCalledWith(555)
        fakeShare.checkShareCalledTimes(1)
        fakeShare.checkShareCalledWith("some homeworks from interactor")
    }

    @Test
    fun test_share_previous_homework() {
        interactor.actualDayMustReturn(228)
        viewModel.init(true)

        val fakeShare = FakeShare()
        viewModel.sharePreviousHomework(fakeShare)
        interactor.checkPreviousHomeworksCalledTimes(1)
        interactor.checkPreviousHomeworksCalledWith(228)
        fakeShare.checkShareCalledTimes(1)
        fakeShare.checkShareCalledWith("some previous homeworks from interactor")
    }

    @Test
    fun test_open_details() {
        val item = DiaryUi.Lesson(
            "name", 1, "teacher", "topic", "homework",
            "previousHomework", "startTime", "endTime", 1234,
            emptyList(), listOf("absence"), listOf("note")
        )
        viewModel.openDetails(item)
        storage.checkSaveCalledTimes(1)
        storage.checkSaveCalledWith(item)
        navigation.checkCalledWith(LessonDetailsScreen)
        order.check(listOf(STORAGE, NAVIGATION))
    }

    @Test
    fun test_set_actual_day() {
        viewModel.setActualDay(999)
        runAsync.returnResult()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(
                    999,
                    listOf(
                        DiaryUi.Lesson(
                            "name", 1, "teacher", "topic", "homework",
                            "previousHomework", "startTime", "endTime", 1234,
                            emptyList(), listOf("absence"), listOf("note")
                        )
                    )
                ),
                listOf(DayUi(123, false), DayUi(124, false)),
                listOf(DayUi(125, true), DayUi(126, false)),
                listOf(DayUi(127, false), DayUi(128, false)),
                true
            )
        )
    }

    @Test
    fun test_next_week() {
        viewModel.setActualDay(500)
        runAsync.returnResult()
        viewModel.nextWeek()
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(
                    507,
                    listOf(
                        DiaryUi.Lesson(
                            "name", 1, "teacher", "topic", "homework",
                            "previousHomework", "startTime", "endTime", 1234,
                            emptyList(), listOf("absence"), listOf("note")
                        )
                    )
                ),
                listOf(DayUi(123, false), DayUi(124, false)),
                listOf(DayUi(125, true), DayUi(126, false)),
                listOf(DayUi(127, false), DayUi(128, false)),
                true
            )
        )
    }

    @Test
    fun test_previous_week() {
        viewModel.setActualDay(500)
        runAsync.returnResult()
        viewModel.previousWeek()
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            DiaryState.Base(
                DiaryUi.Day(
                    493,
                    listOf(
                        DiaryUi.Lesson(
                            "name", 1, "teacher", "topic", "homework",
                            "previousHomework", "startTime", "endTime", 1234,
                            emptyList(), listOf("absence"), listOf("note")
                        )
                    )
                ),
                listOf(DayUi(123, false), DayUi(124, false)),
                listOf(DayUi(125, true), DayUi(126, false)),
                listOf(DayUi(127, false), DayUi(128, false)),
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

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(DiaryViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }
}

private class FakeDiaryCommunication : DiaryCommunication {
    private val list = mutableListOf<DiaryState>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: DiaryState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: DiaryState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<DiaryState>) {
        throw IllegalStateException("not using in tests")
    }

    private var key = ""
    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.key = key
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        assertEquals(key, this.key)
        assertEquals(bundleWrapper, this.bundleWrapper)
        restoreCounter++
    }
}

private class FakeLessonDetailsStorage(private val order: Order) : LessonDetailsStorage.Save {
    private val list = mutableListOf<DiaryUi.Lesson>()

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkSaveCalledWith(expected: DiaryUi.Lesson) {
        assertEquals(expected, list.last())
    }

    override fun save(value: DiaryUi.Lesson) {
        list.add(value)
        order.add(STORAGE)
    }
}

private class FakeDiaryInteractor : DiaryInteractor {
    override fun dayLists(today: Int): Triple<List<DayDomain>, List<DayDomain>, List<DayDomain>> {
        return Triple(
            listOf(DayDomain(123, false), DayDomain(124, false)),
            listOf(DayDomain(125, true), DayDomain(126, false)),
            listOf(DayDomain(127, false), DayDomain(128, false))
        )
    }

    private var dayList = mutableListOf<Int>()
    private var dayFailMessage = ""

    fun dayMustReturnFail(message: String) {
        dayFailMessage = message
    }

    fun checkDayCalledTimes(expected: Int) {
        assertEquals(expected, dayList.size)
    }

    fun checkDayCalledWith(expected: Int) {
        assertEquals(expected, dayList.last())
    }

    override suspend fun day(date: Int): DiaryDomain {
        dayList.add(date)
        return if (dayFailMessage.isNotEmpty()) DiaryDomain.Error(dayFailMessage) else DiaryDomain.Day(
            date, listOf(
                DiaryDomain.Lesson(
                    "name", 1, "teacher", "topic", "homework",
                    "previousHomework", "startTime", "endTime", 1234,
                    emptyList(), listOf("absence"), listOf("note")
                )
            )
        )
    }

    private var actualDateValue = 0
    fun actualDayMustReturn(value: Int) {
        actualDateValue = value
    }

    override fun actualDate() = actualDateValue

    private val homeworksList = mutableListOf<Int>()

    fun checkHomeworksCalledTimes(expected: Int) {
        assertEquals(expected, homeworksList.size)
    }

    fun checkHomeworksCalledWith(expected: Int) {
        assertEquals(expected, homeworksList.last())
    }

    override fun homeworks(date: Int): String {
        homeworksList.add(date)
        return "some homeworks from interactor"
    }

    private val previousHomeworksList = mutableListOf<Int>()

    fun checkPreviousHomeworksCalledTimes(expected: Int) {
        assertEquals(expected, previousHomeworksList.size)
    }

    fun checkPreviousHomeworksCalledWith(expected: Int) {
        assertEquals(expected, previousHomeworksList.last())
    }

    override fun previousHomeworks(date: Int): String {
        previousHomeworksList.add(date)
        return "some previous homeworks from interactor"
    }
}

private class FakeShare : Share {
    private val list = mutableListOf<String>()

    fun checkShareCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkShareCalledWith(expected: String) {
        assertEquals(expected, list.last())
    }

    override fun share(content: String) {
        list.add(content)
    }
}