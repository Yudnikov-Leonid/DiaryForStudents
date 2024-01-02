package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.performance.data.PerformanceCloudDataSource
import com.maxim.diaryforstudents.performance.data.PerformanceData
import com.maxim.diaryforstudents.performance.data.PerformanceRepository
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class PerformanceRepositoryTest {
    private lateinit var repository: PerformanceRepository
    private lateinit var dataSource: FakePerformanceCloudDataSource

    @Before
    fun before() {
        dataSource = FakePerformanceCloudDataSource()
        repository = PerformanceRepository.Base(dataSource)
    }

    @Test
    fun test_init() {
        val fakeReload = FakeReload()
        repository.init(fakeReload)
        dataSource.checkInitCalledTimes(1)
        dataSource.checkInitCalledWith(fakeReload)
    }

    @Test
    fun test_change_type() {
        repository.changeType("abc")
        dataSource.checkChangeTypeCalledTimes(1)
        dataSource.checkChangeTypeCalledWith("abc")
    }

    @Test
    fun test_quarter() {
        assertEquals(0, repository.actualQuarter())
        repository.changeQuarter(55)
        assertEquals(55, repository.actualQuarter())
    }

    @Test
    fun test_data() {
        dataSource.dataReturn(listOf(PerformanceData.Lesson("name", emptyList(), 3.6f)))
        var actual = repository.data("")
        var expected = listOf<PerformanceData>(PerformanceData.Lesson("name", emptyList(), 3.6f))
        assertEquals(expected, actual)
        dataSource.checkDataCalledTimes(1)

        dataSource.dataReturn(emptyList())
        actual = repository.data("")
        expected = listOf(PerformanceData.Empty)
        assertEquals(expected, actual)
        dataSource.checkDataCalledTimes(2)

        dataSource.dataReturn(
            listOf(
                PerformanceData.Lesson("abc", emptyList(), 3.6f),
                PerformanceData.Lesson("def", emptyList(), 3.6f),
                PerformanceData.Lesson("cg", emptyList(), 3.6f),
            )
        )
        actual = repository.data("c")
        expected = listOf(
            PerformanceData.Lesson("abc", emptyList(), 3.6f),
            PerformanceData.Lesson("cg", emptyList(), 3.6f)
        )
        assertEquals(expected, actual)
        dataSource.checkDataCalledTimes(3)
    }
}

private class FakePerformanceCloudDataSource : PerformanceCloudDataSource {
    private val initList = mutableListOf<Reload>()
    fun checkInitCalledTimes(expected: Int) {
        assertEquals(expected, initList.size)
    }

    fun checkInitCalledWith(expected: Reload) {
        assertEquals(expected, initList.last())
    }

    override fun init(reload: Reload) {
        initList.add(reload)
    }
    fun dataReturn(value: List<PerformanceData.Lesson>) {
        dataValue.clear()
        dataValue.addAll(value)
    }
    private var dataValue = mutableListOf<PerformanceData.Lesson>()
    private var dataCounter = 0
    fun checkDataCalledTimes(expected: Int) {
        assertEquals(expected, dataCounter)
    }
    override fun data(quarter: Int): List<PerformanceData.Lesson> {
        dataCounter++
        return dataValue
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
}

private class FakeReload : Reload {
    fun checkReloadCalledTimes(expected: Int) {
        assertEquals(expected, reloadCounter)
    }

    private var reloadCounter = 0
    override fun reload() {
        reloadCounter++
    }

    private val errorList = mutableListOf<String>()
    fun checkErrorCalledTimes(expected: Int) {
        assertEquals(expected, errorList.size)
    }

    fun checkErrorCalledWith(expected: String) {
        assertEquals(expected, errorList.last())
    }

    override fun error(message: String) {
        errorList.add(message)
    }
}