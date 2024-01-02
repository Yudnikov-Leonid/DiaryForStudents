package com.maxim.diaryforstudents.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeManageResources
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.login.data.AuthResultWrapper
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginResult
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginState
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.menu.presentation.MenuScreen
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: FakeLoginRepository
    private lateinit var communication: FakeLoginCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var resources: FakeManageResources
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order

    @Before
    fun before() {
        order = Order()
        repository = FakeLoginRepository()
        communication = FakeLoginCommunication()
        navigation = FakeNavigation(order)
        resources = FakeManageResources("123")
        runAsync = FakeRunAsync()
        clear = FakeClearViewModel(order)
        viewModel =
            LoginViewModel(repository, communication, navigation, clear, resources, runAsync)
    }

    @Test
    fun test_init() {
        repository.expectUserNotLoggedIn(true)
        viewModel.init(true)
        communication.checkCalledWith(LoginState.Initial)
        communication.checkCalledTimes(1)

        viewModel.init(false)
        communication.checkCalledTimes(1)

        repository.expectUserNotLoggedIn(false)
        viewModel.init(true)
        navigation.checkCalledWith(MenuScreen)
        clear.checkCalledWith(LoginViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))

        viewModel.init(false)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_login() {
        viewModel.login()
        communication.checkCalledWith(LoginState.Auth(resources))
        communication.checkCalledTimes(1)
    }

    @Test
    fun test_handle_result() {
        val authResultWrapper = FakeAuthResultWrapper()
        repository.returnSuccess()
        viewModel.handleResult(authResultWrapper)
        repository.checkCalledWith(authResultWrapper)
        repository.checkCalledTimes(1)

        runAsync.returnResult()
        communication.checkCalledTimes(0)
        navigation.checkCalledWith(MenuScreen)
        navigation.checkCalledTimes(1)
        clear.checkCalledTimes(1)
        clear.checkCalledWith(LoginViewModel::class.java)

        repository.returnFailed(message = "Some error")
        viewModel.handleResult(authResultWrapper)
        runAsync.returnResult()
        communication.checkCalledWith(LoginState.Failed("Some error"))
        communication.checkCalledTimes(1)
        navigation.checkCalledTimes(1)
        clear.checkCalledTimes(1)
    }
}

private class FakeAuthResultWrapper : AuthResultWrapper {
    override fun isSuccessful(): Boolean {
        throw IllegalStateException("not using in test")
    }

    override fun task(): Task<GoogleSignInAccount> {
        throw IllegalStateException("not using in test")
    }
}

private class FakeLoginRepository : LoginRepository {
    private var returnType: String? = null
    private val handleResultCalledList = mutableListOf<AuthResultWrapper>()
    private var userNotLoggedIn = true
    fun returnSuccess() {
        returnType = null
    }

    fun returnFailed(message: String) {
        returnType = message
    }

    fun checkCalledWith(expected: AuthResultWrapper) {
        TestCase.assertEquals(expected, handleResultCalledList.last())
    }

    fun expectUserNotLoggedIn(value: Boolean) {
        userNotLoggedIn = value
    }

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, handleResultCalledList.size)
    }

    override fun userNotLoggedIn() = userNotLoggedIn

    override suspend fun handleResult(authResult: AuthResultWrapper): LoginResult {
        handleResultCalledList.add(authResult)
        return if (returnType == null) LoginResult.Successful else LoginResult.Failed(returnType!!)
    }
}

private class FakeLoginCommunication : LoginCommunication {
    private val list = mutableListOf<LoginState>()
    override fun update(value: LoginState) {
        list.add(value)
    }

    fun checkCalledWith(expected: LoginState) {
        TestCase.assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        TestCase.assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) {
        throw IllegalStateException("not using in test")
    }
}