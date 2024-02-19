package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import junit.framework.TestCase

class FakePerformanceInteractor : PerformanceInteractor {
    private var currentQuarter = 0
    private var loadCounter = 0

    override suspend fun loadData() {
        loadCounter++
    }
    fun checkLoadDataCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, loadCounter)
    }

    private var actualDataErrorMessage = ""
    fun actualDataMustReturnFail(message: String) {
        actualDataErrorMessage = message
    }

    override fun actualData(): List<PerformanceDomain> {
        return if (actualDataErrorMessage.isNotEmpty())
            listOf(PerformanceDomain.Error(actualDataErrorMessage))
        else listOf(PerformanceDomain.Mark(5, "12.34.5678", "lesson name", false))
    }

    override fun finalData(): List<PerformanceDomain> {
        TODO("Not yet implemented")
    }

    private var progressTypeValue: ProgressType = ProgressType.Hide
    fun progressTypeMustReturn(value: ProgressType) {
        progressTypeValue = value
    }

    override fun currentProgressType() = progressTypeValue

    private var currentQuarterCounter = 0
    fun checkCurrentQuarterCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, currentQuarterCounter)
    }

    override fun currentQuarter(): Int {
        currentQuarterCounter++
        return currentQuarter
    }

    override suspend fun getLessonByMark(lessonName: String, date: String): DiaryDomain.Lesson {
        TODO("Not yet implemented")
    }

    private val changeQuarterList = mutableListOf<Int>()
    fun checkChangeQuarterCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, changeQuarterList.size)
    }

    fun checkChangeQuarterCalledWith(expected: Int) {
        TestCase.assertEquals(expected, changeQuarterList.last())
    }

    override suspend fun changeQuarter(quarter: Int) {
        changeQuarterList.add(quarter)
        currentQuarter = quarter
    }

    private var dataIsEmptyValue = false
    private var dataIsEmptyCallback: (() -> Unit)? = null
    fun dataIsEmptyMustReturn(value: Boolean) {
        dataIsEmptyValue = value
    }
    fun dataIsEmptyRunCallback() {
        dataIsEmptyCallback?.invoke()
    }
    override fun dataIsEmpty(callback: () -> Unit): Boolean {
        dataIsEmptyCallback = callback
        return dataIsEmptyValue
    }

    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(bundleWrapper: BundleWrapper.Save) {
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        TestCase.assertEquals(this.bundleWrapper, bundleWrapper)
        restoreCounter++
    }

    fun checkSaveCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, restoreCounter)
    }
}