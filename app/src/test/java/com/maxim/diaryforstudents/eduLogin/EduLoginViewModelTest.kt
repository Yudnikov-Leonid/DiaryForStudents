package com.maxim.diaryforstudents.eduLogin

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.LoginException
import com.maxim.diaryforstudents.editDiary.createLesson.presentation.PasswordException
import com.maxim.diaryforstudents.eduLogin.data.EduLoginRepository
import com.maxim.diaryforstudents.eduLogin.data.EduLoginResult
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginCommunication
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginState
import com.maxim.diaryforstudents.eduLogin.presentation.EduLoginViewModel
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.FakeUiValidator
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class EduLoginViewModelTest {
    private lateinit var viewModel: EduLoginViewModel
    private lateinit var repository: FakeEduLoginRepository
    private lateinit var communication: FakeEduLoginCommunication
    private lateinit var loginValidator: FakeUiValidator
    private lateinit var passwordValidator: FakeUiValidator
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clearViewModel: FakeClearViewModel
    private lateinit var navigation: FakeNavigation
    private lateinit var order: Order

    @Before
    fun setUp() {
        repository = FakeEduLoginRepository()
        communication = FakeEduLoginCommunication()
        loginValidator = FakeUiValidator()
        passwordValidator = FakeUiValidator()
        runAsync = FakeRunAsync()
        order = Order()
        clearViewModel = FakeClearViewModel(order)
        navigation = FakeNavigation(order)
        viewModel = EduLoginViewModel(
            repository,
            communication,
            loginValidator,
            passwordValidator,
            navigation,
            clearViewModel,
            runAsync
        )
    }

    @Test
    fun test_correct_login() {
        viewModel.login("123@gmail.com", "321")
        loginValidator.checkCalledTimes(1)
        loginValidator.checkCalledWith("123@gmail.com")
        passwordValidator.checkCalledTimes(1)
        passwordValidator.checkCalledWith("321")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(EduLoginState.Loading)
        repository.checkCalledTimes(1)

        runAsync.returnResult()

        repository.checkCalledWith("123@gmail.com", "321")
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_invalid_login() {
        loginValidator.mustThrowFirst(LoginException("invalid login"))
        viewModel.login("@", "321")
        loginValidator.checkCalledTimes(1)
        loginValidator.checkCalledWith("@")
        repository.checkCalledTimes(0)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(EduLoginState.LoginError("invalid login"))
    }

    @Test
    fun test_invalid_password() {
        passwordValidator.mustThrowFirst(PasswordException("invalid password"))
        viewModel.login("123@gmail.com", "-")
        passwordValidator.checkCalledTimes(1)
        passwordValidator.checkCalledWith("-")
        repository.checkCalledTimes(0)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(EduLoginState.PasswordError("invalid password"))
    }

    @Test
    fun test_repository_error() {
        repository.shouldReturnFailure("some error")
        viewModel.login("123@gmail.com", "321")
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(EduLoginState.Error("some error"))
    }
}

private class FakeEduLoginRepository : EduLoginRepository {
    private var message = ""

    private val list = mutableListOf<Pair<String, String>>()

    fun shouldReturnFailure(message: String) {
        this.message = message
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(login: String, password: String) {
        assertEquals(Pair(login, password), list.last())
    }

    override suspend fun login(login: String, password: String): EduLoginResult {
        list.add(Pair(login, password))
        return if (message.isEmpty()) EduLoginResult.Success else EduLoginResult.Failure(message)
    }
}

private class FakeEduLoginCommunication : EduLoginCommunication {
    private val list = mutableListOf<EduLoginState>()
    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    fun checkCalledWith(expected: EduLoginState) {
        assertEquals(expected, list.last())
    }

    override fun update(value: EduLoginState) {
        list.add(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<EduLoginState>) =
        throw IllegalStateException("not using in tests")
}