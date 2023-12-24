package com.maxim.diaryforstudents

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.Screen
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.ProfileCommunication
import com.maxim.diaryforstudents.profile.ProfileRepository
import com.maxim.diaryforstudents.profile.ProfileState
import com.maxim.diaryforstudents.profile.ProfileViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: FakeProfileRepository
    private lateinit var commnication: FakeProfileCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var manageResource: FakeManageResources

    @Before
    fun before() {
        order = Order()
        repository = FakeProfileRepository(order)
        commnication = FakeProfileCommunication(order)
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        manageResource = FakeManageResources("123\n")
        viewModel = ProfileViewModel(repository, commnication, navigation, clear, manageResource)
    }

    @Test
    fun test_init() {
        repository.mustReturn("email@gmail.com", "name")
        viewModel.init()
        commnication.checkCalledTimes(1)
        commnication.checkCalledWith(ProfileState.Initial("123\nemail@gmail.com\n\n123\nname"))
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

private class FakeProfileRepository(private val order: Order): ProfileRepository {
    private var data = Pair("", "")
    private var counter = 0
    fun mustReturn(email: String, name: String) {
        data = Pair(email, name)
    }
    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }
    override fun signOut() {
        order.add(REPOSITORY)
        counter++
    }
    override fun data() = data
}

private class FakeProfileCommunication(private val order: Order) : ProfileCommunication.Mutable {
    private val list = mutableListOf<ProfileState>()
    override fun update(value: ProfileState) {
        order.add(COMMUNICATION)
        list.add(value)
    }

    fun checkCalledWith(expected: ProfileState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        throw IllegalStateException("not using in test")
    }
}