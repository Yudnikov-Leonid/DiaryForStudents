package com.maxim.diaryforstudents.performance

import com.maxim.diaryforstudents.analytics.domain.AnalyticsDomain
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomain
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.MarkType
import com.maxim.diaryforstudents.performance.common.presentation.ProgressType
import junit.framework.TestCase.assertEquals

class FakePerformanceInteractor : PerformanceInteractor {
    private var currentQuarter = 0
    private var loadCounter = 0

    override suspend fun loadData() {
        loadCounter++
    }

    fun checkLoadDataCalledTimes(expected: Int) {
        assertEquals(expected, loadCounter)
    }

    private var actualDataErrorMessage = ""
    fun actualDataMustReturnFail(message: String) {
        actualDataErrorMessage = message
    }

    override fun actualData(): List<PerformanceDomain> {
        return if (actualDataErrorMessage.isNotEmpty())
            listOf(PerformanceDomain.Error(actualDataErrorMessage))
        else listOf(
            PerformanceDomain.Mark(
                5,
                MarkType.Current,
                "12.34.5678",
                "lesson name",
                false,
                true
            )
        )
    }

    private var finalDataErrorMessage = ""
    fun finalDataMustReturnFail(message: String) {
        finalDataErrorMessage = message
    }

    override fun finalData(): List<PerformanceDomain> {
        return if (finalDataErrorMessage.isNotEmpty())
            listOf(PerformanceDomain.Error(finalDataErrorMessage))
        else listOf(
            PerformanceDomain.Mark(
                4,
                MarkType.Current,
                "12.34.5678",
                "lesson name",
                true,
                true
            )
        )
    }

    private val analyticsList = mutableListOf<List<Any>>()

    fun checkAnalyticsCalledTimes(expected: Int) {
        assertEquals(expected, analyticsList.size)
    }

    fun checkAnalyticsCalledWith(
        quarter: Int,
        lessonName: String,
        interval: Int,
        showFinal: Boolean
    ) {
        assertEquals(listOf(quarter, lessonName, interval, showFinal), analyticsList.last())
    }

    private var analyticsMessage = ""
    fun analyticsMustReturnFail(message: String) {
        analyticsMessage = message
    }

    override suspend fun analytics(
        quarter: Int,
        lessonName: String,
        interval: Int,
        showFinal: Boolean
    ): List<AnalyticsDomain> {
        analyticsList.add(listOf(quarter, lessonName, interval, showFinal))
        return if (analyticsMessage.isNotEmpty()) listOf(AnalyticsDomain.Error(analyticsMessage)) else listOf(
            AnalyticsDomain.LineCommon(
                listOf(5.0f, 4.5f, 4.4f),
                listOf("1", "2", "3"),
                quarter,
                interval
            )
        )
    }

    private var progressTypeValue: ProgressType = ProgressType.Hide
    fun progressTypeMustReturn(value: ProgressType) {
        progressTypeValue = value
    }

    private var showTypeValue = true
    fun showTypeMustReturn(value: Boolean) {
        showTypeValue = value
    }

    override fun currentProgressType() = progressTypeValue
    override fun showType() = showTypeValue

    private var currentQuarterCounter = 0
    fun checkCurrentQuarterCalledTimes(expected: Int) {
        assertEquals(expected, currentQuarterCounter)
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
        assertEquals(expected, changeQuarterList.size)
    }

    fun checkChangeQuarterCalledWith(expected: Int) {
        assertEquals(expected, changeQuarterList.last())
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

    override fun dataIsLoading(callback: () -> Unit): Boolean {
        dataIsEmptyCallback = callback
        return dataIsEmptyValue
    }

    private var newMarksCountValue = 0
    fun newMarksCountMustReturn(value: Int) {
        newMarksCountValue = value
    }

    override fun newMarksCount() = newMarksCountValue

    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(bundleWrapper: BundleWrapper.Save) {
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        assertEquals(this.bundleWrapper, bundleWrapper)
        restoreCounter++
    }

    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveCounter)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }
}