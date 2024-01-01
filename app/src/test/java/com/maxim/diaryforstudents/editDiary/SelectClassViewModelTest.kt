package com.maxim.diaryforstudents.editDiary

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Reload
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.editDiary.common.SelectedClassCache
import com.maxim.diaryforstudents.editDiary.edit.presentation.EditDiaryScreen
import com.maxim.diaryforstudents.editDiary.selectClass.data.ClassData
import com.maxim.diaryforstudents.editDiary.selectClass.data.SelectClassRepository
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.ClassUi
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassCommunication
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassState
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassViewModel
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class SelectClassViewModelTest {
    private lateinit var viewModel: SelectClassViewModel
    private lateinit var communication: FakeSelectClassCommunication
    private lateinit var repository: FakeSelectClassRepository
    private lateinit var cache: FakeSelectedClassCache
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order

    @Before
    fun before() {
        communication = FakeSelectClassCommunication()
        repository = FakeSelectClassRepository()
        cache = FakeSelectedClassCache()
        order = Order()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        viewModel = SelectClassViewModel(repository, communication, cache, navigation, clear)
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(SelectClassState.Loading)
        repository.checkInitCalledTimes(1)
        repository.checkInitCalledWith(viewModel)

        viewModel.init(false)
        communication.checkCalledTimes(1)
        repository.checkInitCalledTimes(1)
    }

    @Test
    fun test_open() {
        viewModel.open("353")
        cache.checkCalledTimes(1)
        cache.checkCalledWith("353")
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(EditDiaryScreen)
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(SelectClassViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_reload() {
        repository.dataMustReturn(listOf(ClassData.Base("id", "name")))
        viewModel.reload()
        repository.checkDataCalledTimes(1)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(SelectClassState.Base(listOf(ClassUi.Base("id", "name"))))
    }
}

private class FakeSelectedClassCache : SelectedClassCache.Update {
    private val updateList = mutableListOf<String>()
    fun checkCalledWith(expected: String) {
        assertEquals(expected, updateList.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, updateList.size)
    }

    override fun update(value: String) {
        updateList.add(value)
    }
}

private class FakeSelectClassRepository : SelectClassRepository {
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

    private var dataCounter = 0
    private val dataReturn = mutableListOf<ClassData>()
    fun checkDataCalledTimes(expected: Int) {
        assertEquals(expected, dataCounter)
    }

    fun dataMustReturn(value: List<ClassData>) {
        dataReturn.addAll(value)
    }

    override fun data(): List<ClassData> {
        dataCounter++
        return dataReturn
    }
}

private class FakeSelectClassCommunication : SelectClassCommunication {
    private val list = mutableListOf<SelectClassState>()
    override fun update(value: SelectClassState) {
        list.add(value)
    }

    fun checkCalledWith(expected: SelectClassState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SelectClassState>) {
        throw IllegalStateException("not using in test")
    }
}