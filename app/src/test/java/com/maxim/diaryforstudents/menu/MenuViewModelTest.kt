package com.maxim.diaryforstudents.menu

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.editDiary.selectClass.presentation.SelectClassScreen
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.menu.domain.MenuInteractor
import com.maxim.diaryforstudents.menu.domain.UserStatus
import com.maxim.diaryforstudents.menu.presentation.MenuCommunication
import com.maxim.diaryforstudents.menu.presentation.MenuState
import com.maxim.diaryforstudents.menu.presentation.MenuViewModel
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class MenuViewModelTest {
    private lateinit var navigation: FakeNavigation
    private lateinit var interactor: FakeMenuInteractor
    private lateinit var communication: FakeMenuCommunication
    private lateinit var viewModel: MenuViewModel
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun before() {
        navigation = FakeNavigation(Order())
        interactor = FakeMenuInteractor()
        communication = FakeMenuCommunication()
        runAsync = FakeRunAsync()
        viewModel = MenuViewModel(interactor, communication, navigation, runAsync)
    }

    @Test
    fun test_init() {
        interactor.mustReturn(UserStatus.Student)
        viewModel.init(true)
        interactor.checkCalledTimes(1)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(MenuState.Loading)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(MenuState.Student)

        viewModel.init(false)
        interactor.checkCalledTimes(1)
        communication.checkCalledTimes(2)

        interactor.mustReturn(UserStatus.Teacher)
        viewModel.init(true)
        runAsync.returnResult()
        interactor.checkCalledTimes(2)
        communication.checkCalledTimes(4)
        communication.checkCalledWith(MenuState.Teacher)

        viewModel.init(false)
        interactor.checkCalledTimes(2)
        communication.checkCalledTimes(4)
    }

    @Test
    fun test_diary() {
        viewModel.diary()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(DiaryScreen)
    }

    @Test
    fun test_diary_for_teacher() {
        viewModel.diaryForTeacher()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(SelectClassScreen)
    }

    @Test
    fun test_performance() {
        viewModel.performance()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(PerformanceScreen)
    }

    @Test
    fun test_profile() {
        viewModel.profile()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(ProfileScreen)
    }

    @Test
    fun test_news() {
        viewModel.news()
        navigation.checkCalledTimes(1)
        navigation.checkCalledWith(NewsScreen)
    }
}

private class FakeMenuCommunication : MenuCommunication {
    private val list = mutableListOf<MenuState>()
    override fun update(value: MenuState) {
        list.add(value)
    }

    fun checkCalledWith(expected: MenuState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MenuState>) {
        throw IllegalStateException("not using in test")
    }
}

private class FakeMenuInteractor : MenuInteractor {
    private var data: UserStatus? = null
    private var counter = 0
    fun mustReturn(value: UserStatus) {
        data = value
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    override suspend fun userStatus(): UserStatus {
        counter++
        return data!!
    }
}