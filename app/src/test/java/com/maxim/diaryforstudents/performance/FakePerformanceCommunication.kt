package com.maxim.diaryforstudents.performance

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceCommunication
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceState
import junit.framework.TestCase

class FakePerformanceCommunication : PerformanceCommunication {
    private val list = mutableListOf<PerformanceState>()

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: PerformanceState) {
        TestCase.assertEquals(expected, list.last())
    }

    fun checkCalledWith(expected: List<PerformanceState>) {
        TestCase.assertEquals(expected, list)
    }

    override fun update(value: PerformanceState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        throw IllegalStateException("not used in tests")
    }

    private var key = ""
    private var bundleWrapper: BundleWrapper.Mutable? = null
    private var saveCounter = 0
    private var restoreCounter = 0

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        this.key = key
        this.bundleWrapper = bundleWrapper as BundleWrapper.Mutable
        saveCounter++
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        TestCase.assertEquals(this.key, key)
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