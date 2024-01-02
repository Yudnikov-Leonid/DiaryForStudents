package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import junit.framework.TestCase.assertEquals
import java.io.Serializable

class FakeBundleWrapper: BundleWrapper.Mutable {
    private var saveKey = ""
    private var restoreKey = ""

    private val saveList = mutableListOf<Any>()
    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveList.size)
    }
    fun checkSaveCalledWith(expected: Any) {
        assertEquals(expected, saveList.last())
    }
    override fun <T : Serializable> save(key: String, value: T) {
        saveList.add(value)
        saveKey = key
    }

    private var restoreCounter = 0
    private var restoreValue: Any? = null
    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreCounter)
    }
    fun <T: Serializable?> restoreMustReturn(value: T) {
        restoreValue = value
    }

    override fun <T : Serializable?> restore(key: String): T? {
        restoreCounter++
        restoreKey = key
        return restoreValue as T?
    }

    fun checkSaveAndRestoreWasCalledWithSameKey() {
        assertEquals(saveKey, restoreKey)
    }
}