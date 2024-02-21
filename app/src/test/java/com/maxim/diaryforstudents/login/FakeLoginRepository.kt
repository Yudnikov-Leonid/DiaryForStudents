package com.maxim.diaryforstudents.login

import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.login.data.LoginResult
import com.maxim.diaryforstudents.selectUser.data.SelectUserData
import junit.framework.TestCase.assertEquals

class FakeLoginRepository : LoginRepository {
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

    override suspend fun login(login: String, password: String): LoginResult {
        list.add(Pair(login, password))
        return if (message.isEmpty()) LoginResult.Success else LoginResult.Failure(message)
    }

    private val usersList = mutableListOf<SelectUserData>()
    fun usersMustReturn(value: List<SelectUserData>) {
        usersList.clear()
        usersList.addAll(value)
    }

    override fun users() = usersList

    private val selectList = mutableListOf<Int>()

    fun checkSelectCalledTimes(expected: Int) {
        assertEquals(expected, selectList.size)
    }

    fun checkSelectCalledWith(expected: Int) {
        assertEquals(expected, selectList.last())
    }

    override fun select(position: Int) {
        selectList.add(position)
    }
}