package com.maxim.diaryforstudents.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.COMMUNICATION
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.REPOSITORY
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.data.GradeResult
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import com.maxim.diaryforstudents.profile.presentation.ProfileCommunication
import com.maxim.diaryforstudents.profile.presentation.ProfileState
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: FakeProfileRepository
    private lateinit var commnication: FakeProfileCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var order: Order

    @Before
    fun before() {
        order = Order()
        repository = FakeProfileRepository(order)
        commnication = FakeProfileCommunication(order)
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = ProfileViewModel(repository, commnication, navigation, clear, runAsync)
    }

    @Test
    fun test_init() {
        repository.mustReturn("email@gmail.com", "name", GradeResult.Student("10"))
        viewModel.init()
        commnication.checkCalledTimes(1)
        runAsync.returnResult()
        commnication.checkCalledTimes(2)
        commnication.checkCalledWith(
            listOf(
                ProfileState.Loading,
                ProfileState.Base("name", GradeResult.Student("10"), "email@gmail.com")
            )
        )
    }

    @Test
    fun test_sign_out() {
        viewModel.signOut()
        navigation.checkCalledWith(LoginScreen)
        clear.checkCalledWith(ProfileViewModel::class.java)
        order.check(listOf(REPOSITORY, NAVIGATION, CLEAR))
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(ProfileViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }
}

private class FakeProfileRepository(private val order: Order) : ProfileRepository {
    private var data = Triple<String, GradeResult, String>("",  GradeResult.Empty, "")
    private var counter = 0
    fun mustReturn(email: String, name: String, grade: GradeResult) {
        data = Triple(name, grade, email)
    }

    override fun signOut() {
        order.add(REPOSITORY)
        counter++
    }

    override suspend fun data(): Triple<String, GradeResult, String> = data
}

private class FakeProfileCommunication(private val order: Order) : ProfileCommunication.Mutable {
    private val list = mutableListOf<ProfileState>()
    override fun update(value: ProfileState) {
        order.add(COMMUNICATION)
        list.add(value)
    }

    fun checkCalledWith(expected: List<ProfileState>) {
        TestCase.assertEquals(expected, list)
    }

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        throw IllegalStateException("not using in test")
    }
}