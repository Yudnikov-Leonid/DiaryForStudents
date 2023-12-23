package com.maxim.diaryforstudents

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.maxim.diaryforstudents.core.ManageResource
import com.maxim.diaryforstudents.core.RunAsync
import com.maxim.diaryforstudents.login.LoginCommunication
import com.maxim.diaryforstudents.login.LoginState
import com.maxim.diaryforstudents.login.LoginViewModel
import com.maxim.diaryforstudents.login.data.AuthResultWrapper
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginResult
import com.maxim.diaryforstudents.profile.ProfileScreen
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: FakeLoginRepository
    private lateinit var communication: FakeLoginCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var resources: FakeManageResources
    private lateinit var runAsync: FakeRunAsync

    @Before
    fun before() {
        repository = FakeLoginRepository()
        communication = FakeLoginCommunication()
        navigation = FakeNavigation()
        resources = FakeManageResources()
        runAsync = FakeRunAsync()
        viewModel = LoginViewModel(repository, communication, navigation, resources, runAsync)
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
        communication.checkCalledWith(LoginState.Auth(resources))
        communication.checkCalledTimes(2)

        viewModel.init(false)
        communication.checkCalledTimes(2)
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
        navigation.checkCalledWith(ProfileScreen)
        navigation.checkCalledTimes(1)

        repository.returnFailed(message = "Some error")
        viewModel.handleResult(authResultWrapper)
        runAsync.returnResult()
        communication.checkCalledWith(LoginState.Failed("Some error"))
        communication.checkCalledTimes(1)
        navigation.checkCalledTimes(1)
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

private class FakeRunAsync : RunAsync {
    private lateinit var cached: (Any) -> Unit
    private lateinit var cachedArgument: Any
    fun returnResult() {
        cached.invoke(cachedArgument)
    }

    override fun <T : Any> handleAsync(
        coroutineScope: CoroutineScope,
        backgroundBlock: suspend () -> T,
        uiBlock: (T) -> Unit
    ) = runBlocking {
        cachedArgument = backgroundBlock.invoke()
        cached = uiBlock as (Any) -> Unit
    }

}

private class FakeManageResources : ManageResource {
    override fun string(key: Int) = "123"
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
        assertEquals(expected, handleResultCalledList.last())
    }

    fun expectUserNotLoggedIn(value: Boolean) {
        userNotLoggedIn = value
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, handleResultCalledList.size)
    }

    override fun userNotLoggedIn() = userNotLoggedIn

    override suspend fun handleResult(authResult: AuthResultWrapper): LoginResult {
        handleResultCalledList.add(authResult)
        return if (returnType == null) LoginResult.Successful else LoginResult.Failed(returnType!!)
    }
}

private class FakeLoginCommunication : LoginCommunication.Mutable {
    private val list = mutableListOf<LoginState>()
    override fun update(value: LoginState) {
        list.add(value)
    }

    fun checkCalledWith(expected: LoginState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<LoginState>) {
        throw IllegalStateException("not using in test")
    }
}