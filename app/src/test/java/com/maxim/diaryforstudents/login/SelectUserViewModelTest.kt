package com.maxim.diaryforstudents.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import com.maxim.diaryforstudents.selectUser.data.SelectUserData
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserCommunication
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserState
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserUi
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserViewModel
import com.maxim.diaryforstudents.selectUser.sl.SelectUserModule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class SelectUserViewModelTest {
    private lateinit var viewModel: SelectUserViewModel
    private lateinit var repository: FakeLoginRepository
    private lateinit var communication: FakeSelectUserCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var module: FakeSelectUserModule

    @Before
    fun setUp() {
        repository = FakeLoginRepository()
        communication = FakeSelectUserCommunication()
        order = Order()
        navigation = FakeNavigation(order)
        clearViewModel = FakeClearViewModel(order)
        module = FakeSelectUserModule()
        viewModel = SelectUserViewModel(
            repository,
            communication,
            navigation,
            clearViewModel,
            SelectUserDataToUiMapper(),
            module
        )
    }

    @Test
    fun test_init() {
        repository.usersMustReturn(listOf(SelectUserData.Base("name", "school name")))
        viewModel.init()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            SelectUserState.Base(
                listOf(
                    SelectUserUi.Title,
                    SelectUserUi.Base(
                        "name",
                        "school name"
                    )
                )
            )
        )
    }

    @Test
    fun test_select() {
        viewModel.select(0)
        repository.checkSelectCalledTimes(1)
        repository.checkSelectCalledWith(0)
        module.checkClearCalledTimes(1)
        navigation.checkCalledWith(MenuScreen)
        clearViewModel.checkCalledWith(listOf(LoginViewModel::class.java, SelectUserViewModel::class.java))
        order.check(listOf(NAVIGATION, CLEAR, CLEAR))
    }

    @Test
    fun test_go_back() {
        viewModel.goBack()
        navigation.checkCalledWith(Screen.Pop)
        clearViewModel.checkCalledWith(SelectUserViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        repository.checkSaveCalledTimes(1)
        viewModel.restore(bundleWrapper)
        repository.checkRestoreCalledTimes(1)
    }
}

private class FakeSelectUserModule: SelectUserModule {
    private var counter = 0

    fun checkClearCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    override fun clear() {
        counter++
    }

    override fun viewModel(): SelectUserViewModel {
        throw java.lang.IllegalStateException("not using in tests")
    }
}

private class FakeSelectUserCommunication : SelectUserCommunication {
    private val list = mutableListOf<SelectUserState>()

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: SelectUserState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: SelectUserState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<SelectUserState>) {
        throw IllegalStateException("not using in tests")
    }
}