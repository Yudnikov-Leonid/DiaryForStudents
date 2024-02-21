package com.maxim.diaryforstudents.login

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.fakes.FakeManageResources
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.FakeUiValidator
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginException
import com.maxim.diaryforstudents.login.presentation.LoginState
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.presentation.PasswordException
import com.maxim.diaryforstudents.main.HideKeyboard
import com.maxim.diaryforstudents.selectUser.presentation.SelectUserScreen
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: FakeLoginRepository
    private lateinit var communication: FakeLoginCommunication
    private lateinit var loginValidator: FakeUiValidator
    private lateinit var passwordValidator: FakeUiValidator
    private lateinit var runAsync: FakeRunAsync
    private lateinit var navigation: FakeNavigation
    private lateinit var order: Order

    private lateinit var hideKeyboard: FakeHideKeyboard

    @Before
    fun setUp() {
        repository = FakeLoginRepository()
        communication = FakeLoginCommunication()
        loginValidator = FakeUiValidator()
        passwordValidator = FakeUiValidator()
        runAsync = FakeRunAsync()
        order = Order()
        navigation = FakeNavigation(order)
        viewModel = LoginViewModel(
            repository,
            communication,
            loginValidator,
            passwordValidator,
            navigation,
            FakeManageResources("error message"),
            runAsync
        )

        hideKeyboard = FakeHideKeyboard()
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(LoginState.Initial)

        viewModel.init(false)
        communication.checkCalledTimes(1)
    }

    @Test
    fun test_correct_login() {
        viewModel.login("123@gmail.com", "321", hideKeyboard)
        loginValidator.checkCalledTimes(1)
        loginValidator.checkCalledWith("123@gmail.com")
        passwordValidator.checkCalledTimes(1)
        passwordValidator.checkCalledWith("321")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(LoginState.Loading)
        repository.checkCalledTimes(1)
        repository.checkCalledWith("123@gmail.com", "321")

        runAsync.returnResult()

        hideKeyboard.checkCalledTimes(1)
        navigation.checkCalledWith(SelectUserScreen)
        order.check(listOf(NAVIGATION))
    }

    @Test
    fun test_invalid_login() {
        loginValidator.mustThrowFirst(LoginException("invalid login"))
        viewModel.login("@", "321", hideKeyboard)
        loginValidator.checkCalledTimes(1)
        loginValidator.checkCalledWith("@")
        repository.checkCalledTimes(0)
        hideKeyboard.checkCalledTimes(0)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(LoginState.LoginError("invalid login"))
    }

    @Test
    fun test_invalid_password() {
        passwordValidator.mustThrowFirst(PasswordException("invalid password"))
        viewModel.login("123@gmail.com", "-", hideKeyboard)
        passwordValidator.checkCalledTimes(1)
        passwordValidator.checkCalledWith("-")
        repository.checkCalledTimes(0)
        hideKeyboard.checkCalledTimes(0)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(LoginState.PasswordError("invalid password"))
    }

    @Test
    fun test_repository_error() {
        repository.shouldReturnFailure("some error")
        viewModel.login("123@gmail.com", "321", hideKeyboard)
        runAsync.returnResult()
        hideKeyboard.checkCalledTimes(0)
        communication.checkCalledTimes(2)
        communication.checkCalledWith(LoginState.Error("some error"))
    }
}

private class FakeLoginCommunication : LoginCommunication {
    private val list = mutableListOf<LoginState>()
    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: LoginState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: LoginState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) =
        throw IllegalStateException("not using in tests")
}

private class FakeHideKeyboard: HideKeyboard {
    private var counter = 0

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, counter)
    }

    override fun hideKeyboard() {
        counter++
    }
}