package com.maxim.diaryforstudents.fakes

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import junit.framework.TestCase.assertEquals

class FakeClearViewModel(private val order: Order): ClearViewModel {
    private val viewModelList = mutableListOf<Class<out ViewModel>>()
    override fun clearViewModel(clazz: Class<out ViewModel>) {
        order.add(CLEAR)
        viewModelList.add(clazz)
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, viewModelList.size)
    }

    fun checkCalledWith(expected: Class<out ViewModel>) {
        assertEquals(expected, viewModelList.last())
    }
}