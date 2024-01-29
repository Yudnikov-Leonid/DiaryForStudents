package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceData
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.data.PerformanceRepository
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.domain.ServiceUnavailableException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PerformanceInteractorTest {
    private lateinit var interactor: PerformanceInteractor
    private lateinit var repository: FakePerformanceRepository

    @Before
    fun setUp() {
        repository = FakePerformanceRepository()
        interactor = PerformanceInteractor.Base(
            repository,
            FailureHandler.Base(),
            PerformanceDataToDomainMapper()
        )
    }

    @Test
    fun test_init() = runBlocking {
        interactor.init()
        repository.checkInitCalledTimes(1)
    }

    @Test
    fun test_data_success() {
        repository.dataMustReturn(listOf(PerformanceData.Mark(5, "date", false)))
        val actual = interactor.data("search")
        repository.checkDataCalledWith("search")
        assertEquals(listOf(PerformanceDomain.Mark(5, "date", false)), actual)
    }

    @Test
    fun test_data_failure() {
        repository.dataMustThrowException("message")
        val actual = interactor.data("")
        assertEquals(listOf(PerformanceDomain.Error("message")), actual)
    }

    @Test
    fun test_final_date_success() {
        repository.finalDataMustReturn(listOf(PerformanceData.Mark(5, "date", false)))
        val actual = interactor.finalData("final")
        repository.checkFinalDataCalledWith("final")
        assertEquals(listOf(PerformanceDomain.Mark(5, "date", false)), actual)
    }

    @Test
    fun test_final_date_failure() {
        repository.finalDataMustThrowException("final message")
        val actual = interactor.finalData("")
        assertEquals(listOf(PerformanceDomain.Error("final message")), actual)
    }

    @Test
    fun test_change_quarter() = runBlocking {
        interactor.changeQuarter(2)
        repository.checkChangeQuarterCalledTimes(1)
        repository.checkChangeQuarterCalledWith(2)
    }

    @Test
    fun test_actual_quarter() {
        repository.actualQuarterMustReturn(555)
        val actual = interactor.actualQuarter()
        assertEquals(555, actual)
    }
}

private class FakePerformanceRepository : PerformanceRepository {
    private var initCounter = 0
    override suspend fun init() {
        initCounter++
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initCounter)
    }

    private val dataValue = mutableListOf<PerformanceData>()
    private val dataList = mutableListOf<String>()
    private var exception: Exception? = null
    override fun cachedData(search: String): List<PerformanceData> {
        dataList.add(search)
        exception?.let { throw it }
        return dataValue
    }

    fun dataMustThrowException(message: String) {
        exception = ServiceUnavailableException(message)
    }

    fun dataMustReturn(value: List<PerformanceData>) {
        dataValue.clear()
        dataValue.addAll(value)
    }

    fun checkDataCalledWith(expected: String) {
        assertEquals(expected, dataList.last())
    }

    private val finalDataValue = mutableListOf<PerformanceData>()
    private val finalDataList = mutableListOf<String>()
    private var finalException: Exception? = null
    override fun cachedFinalData(search: String): List<PerformanceData> {
        finalDataList.add(search)
        finalException?.let { throw it }
        return finalDataValue
    }

    fun finalDataMustThrowException(message: String) {
        finalException = ServiceUnavailableException(message)
    }

    fun finalDataMustReturn(value: List<PerformanceData>) {
        finalDataValue.clear()
        finalDataValue.addAll(value)
    }

    fun checkFinalDataCalledWith(expected: String) {
        assertEquals(expected, finalDataList.last())
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