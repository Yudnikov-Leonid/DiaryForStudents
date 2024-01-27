package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.performance.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository
import com.maxim.diaryforstudents.performance.eduData.PerformanceData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PerformanceInteractorTest {
    private lateinit var interactor: PerformanceInteractor
    private lateinit var repository: FakeEduPerformanceRepository

    @Before
    fun setUp() {
        repository = FakeEduPerformanceRepository()
        interactor = PerformanceInteractor.Base(repository)
    }

    @Test
    fun test_init() = runBlocking {
        interactor.init()
        repository.checkInitCalledTimes(1)
    }

    @Test
    fun test_data() {
        repository.dataMustReturn(listOf(PerformanceData.Error("for example")))
        val actual = interactor.data("search")
        repository.checkDataCalledWith("search")
        assertEquals(listOf(PerformanceData.Error("for example")), actual)
    }

    @Test
    fun test_final_date() {
        repository.finalDataMustReturn(listOf(PerformanceData.Error("abcd")))
        val actual = interactor.finalData("final")
        repository.checkFinalDataCalledWith("final")
        assertEquals(listOf(PerformanceData.Error("abcd")), actual)
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

private class FakeEduPerformanceRepository: EduPerformanceRepository {
    private var initCounter = 0
    override suspend fun init() {
        initCounter++
    }

    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initCounter)
    }

    private val dataValue = mutableListOf<PerformanceData>()
    private val dataList = mutableListOf<String>()
    override fun cachedData(search: String): List<PerformanceData> {
        dataList.add(search)
        return dataValue
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
    override fun cachedFinalData(search: String): List<PerformanceData> {
        return finalDataValue
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