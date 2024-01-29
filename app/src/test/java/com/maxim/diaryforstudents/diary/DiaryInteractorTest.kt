package com.maxim.diaryforstudents.diary

import com.maxim.diaryforstudents.diary.data.DayData
import com.maxim.diaryforstudents.diary.data.DayDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryData
import com.maxim.diaryforstudents.diary.data.DiaryDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.domain.DayDomain
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DiaryInteractorTest {
    private lateinit var interactor: DiaryInteractor
    private lateinit var repository: FakeDiaryRepository

    @Before
    fun setUp() {
        repository = FakeDiaryRepository()
        interactor = DiaryInteractor.Base(
            repository,
            FailureHandler.Base(),
            DiaryDataToDomainMapper(PerformanceDataToDomainMapper()),
            DayDataToDomainMapper()
        )
    }

    @Test
    fun test_day_list() {
        repository.dayListMustReturn(listOf(DayData(5, true)))
        val actual = interactor.dayList(44)
        repository.checkDayListCalledTimes(1)
        repository.checkDayListCalledWith(44)
        assertEquals(listOf(DayDomain(5, true)), actual)
    }

    @Test
    fun test_day_success() = runBlocking {
        repository.dayMustReturn(DiaryData.Day(55, emptyList()))
        val actual = interactor.day(123)
        repository.checkDayCalledTimes(1)
        repository.checkDayCalledWith(123)
        assertEquals(DiaryDomain.Day(55, emptyList()), actual)
    }

    @Test
    fun test_day_failure() = runBlocking {
        repository.dayMustThrow("message")
        val actual = interactor.day(123)
        repository.checkDayCalledTimes(1)
        repository.checkDayCalledWith(123)
        assertEquals(DiaryDomain.Error("message"), actual)
    }

    @Test
    fun test_actual_date() {
        repository.actualDateMustReturn(4567)
        val actual = interactor.actualDate()
        assertEquals(4567, actual)
    }

    @Test
    fun test_homeworks() {
        repository.homeworksMustReturn("abcd")
        val actual = interactor.homeworks(65)
        repository.checkHomeworksCalledTimes(1)
        repository.checkHomeworksCalledWith(65)
        assertEquals("abcd", actual)
    }

    @Test
    fun test_previousHomeworkds() {
        repository.previousHomeworksMustReturn("aaaa")
        val actual = interactor.previousHomeworks(3)
        repository.checkPreviousHomeworksCalledTimes(1)
        repository.checkPreviousHomeworksCalledWith(3)
        assertEquals("aaaa", actual)
    }

    @Test
    fun test_save_filters() {
        interactor.saveFilters(booleanArrayOf(true, true, false))
        repository.checkSaveFiltersCalledTimes(1)
        repository.checkSaveFiltersCalledWith(booleanArrayOf(true, true, false))
    }

    @Test
    fun test_filters() {
        repository.filtersMustReturn(booleanArrayOf(true, false, true))
        val actual = interactor.filters()
        assertEquals(listOf(true, false, true), actual.toList())
    }

    @Test
    fun test_save_homework_from() {
        interactor.saveHomeworkFrom(true)
        repository.checkSaveHomeworkFromCalledTimes(1)
        repository.checkSaveHomeworkFromCalledWith(true)

        interactor.saveHomeworkFrom(false)
        repository.checkSaveHomeworkFromCalledTimes(2)
        repository.checkSaveHomeworkFromCalledWith(false)
    }

    @Test
    fun test_homework_from() {
        repository.homeworkFromMustReturn(true)
        var actual = interactor.homeworkFrom()
        assertEquals(true, actual)

        repository.homeworkFromMustReturn(false)
        actual = interactor.homeworkFrom()
        assertEquals(false, actual)
    }
}

private class FakeDiaryRepository : DiaryRepository {
    private val dayListValue = mutableListOf<DayData>()
    private val dayListList = mutableListOf<Int>()
    override fun dayList(today: Int): List<DayData> {
        dayListList.add(today)
        return dayListValue
    }

    fun dayListMustReturn(value: List<DayData>) {
        dayListValue.clear()
        dayListValue.addAll(value)
    }

    fun checkDayListCalledTimes(expected: Int) {
        assertEquals(expected, dayListList.size)
    }

    fun checkDayListCalledWith(expected: Int) {
        assertEquals(expected, dayListList.last())
    }

    private lateinit var dayValue: DiaryData.Day
    private val dayList = mutableListOf<Int>()
    private var dayException: Exception? = null
    override suspend fun day(date: Int): DiaryData.Day {
        dayList.add(date)
        dayException?.let { throw it }
        return dayValue
    }

    fun dayMustThrow(message: String) {
        dayException = ServiceUnavailableException(message)
    }

    fun dayMustReturn(value: DiaryData.Day) {
        dayValue = value
    }

    fun checkDayCalledTimes(expected: Int) {
        assertEquals(expected, dayList.size)
    }

    fun checkDayCalledWith(expected: Int) {
        assertEquals(expected, dayList.last())
    }

    private var actualDateValue = 0
    override fun actualDate(): Int = actualDateValue

    fun actualDateMustReturn(value: Int) {
        actualDateValue = value
    }

    private var homeworksValue = ""
    private val homeworksList = mutableListOf<Int>()
    override fun homeworks(date: Int): String {
        homeworksList.add(date)
        return homeworksValue
    }

    fun homeworksMustReturn(value: String) {
        homeworksValue = value
    }

    fun checkHomeworksCalledTimes(expected: Int) {
        assertEquals(expected, homeworksList.size)
    }

    fun checkHomeworksCalledWith(expected: Int) {
        assertEquals(expected, homeworksList.last())
    }

    private var previousHomeworksValue = ""
    private val previousHomeworksList = mutableListOf<Int>()
    override fun previousHomeworks(date: Int): String {
        previousHomeworksList.add(date)
        return previousHomeworksValue
    }

    fun previousHomeworksMustReturn(value: String) {
        previousHomeworksValue = value
    }

    fun checkPreviousHomeworksCalledTimes(expected: Int) {
        assertEquals(expected, previousHomeworksList.size)
    }

    fun checkPreviousHomeworksCalledWith(expected: Int) {
        assertEquals(expected, previousHomeworksList.last())
    }

    private val saveFiltersList = mutableListOf<BooleanArray>()
    override fun saveFilters(booleanArray: BooleanArray) {
        saveFiltersList.add(booleanArray)
    }

    fun checkSaveFiltersCalledTimes(expected: Int) {
        assertEquals(expected, saveFiltersList.size)
    }

    fun checkSaveFiltersCalledWith(expected: BooleanArray) {
        assertEquals(expected.toList(), saveFiltersList.last().toList())
    }

    private var filtersValue = booleanArrayOf()
    override fun filters(): BooleanArray = filtersValue

    fun filtersMustReturn(value: BooleanArray) {
        filtersValue = value
    }

    private val saveHomeworkFromList = mutableListOf<Boolean>()
    override fun saveHomeworkFrom(value: Boolean) {
        saveHomeworkFromList.add(value)
    }

    fun checkSaveHomeworkFromCalledTimes(expected: Int) {
        assertEquals(expected, saveHomeworkFromList.size)
    }

    fun checkSaveHomeworkFromCalledWith(expected: Boolean) {
        assertEquals(expected, saveHomeworkFromList.last())
    }

    private var homeworkFromValue = false
    override fun homeworkFrom(): Boolean = homeworkFromValue

    fun homeworkFromMustReturn(value: Boolean) {
        homeworkFromValue = value
    }
}