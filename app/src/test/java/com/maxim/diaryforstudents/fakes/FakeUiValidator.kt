package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.sl.ManageResource
import com.maxim.diaryforstudents.login.presentation.UiValidator
import junit.framework.TestCase

class FakeUiValidator : UiValidator {
    private var counter = 0
    private var firstException: Exception? = null
    private var secondException: Exception? = null
    private val list = mutableListOf<String>()
    fun mustThrowFirst(value: Exception) {
        firstException = value
    }

    fun mustThrowSecond(value: Exception) {
        secondException = value
    }

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: String) {
        TestCase.assertEquals(expected, list.last())
    }

    override fun isValid(value: String, manageResource: ManageResource) {
        list.add(value)
        counter++
        if (counter == 1 && firstException != null) throw firstException!!
        else if (counter == 2 && secondException != null) throw secondException!!
    }
}