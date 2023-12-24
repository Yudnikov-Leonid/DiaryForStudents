package com.maxim.diaryforstudents.fakes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.core.Screen
import junit.framework.TestCase.assertEquals
import java.lang.IllegalStateException

class FakeNavigation(private val order: Order): Navigation.Mutable {
    private var lastArgument: Screen? = null
    private var counter = 0
    override fun update(value: Screen) {
        order.add(NAVIGATION)
        lastArgument = value
        counter++
    }

    fun checkCalledWith(expected: Screen) {
        assertEquals(expected, lastArgument)
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) {
        throw IllegalStateException("not using in tests")
    }
}