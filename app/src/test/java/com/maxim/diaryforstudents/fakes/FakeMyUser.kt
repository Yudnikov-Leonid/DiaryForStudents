package com.maxim.diaryforstudents.fakes

import com.maxim.diaryforstudents.core.service.MyUser
import com.maxim.diaryforstudents.profile.data.ClientWrapper
import junit.framework.TestCase.assertEquals

class FakeMyUser: MyUser {
    private var id = ""
    private var email = ""
    private var name = ""

    fun returnEmail(value: String) {
        email = value
    }
    fun checkEmailCalledTimes(expected: Int) {
        assertEquals(expected, emailCounter)
    }
    fun returnName(value: String) {
        name = value
    }
    fun checkNameCalledTimes(expected: Int) {
        assertEquals(expected, nameCounter)
    }
    fun returnId(value: String) {
        id = value
    }
    fun checkIdCalledTimes(expected: Int) {
        assertEquals(expected, idCounter)
    }



    private var idCounter = 0
    override fun id(): String {
        idCounter++
        return id
    }

    private var emailCounter = 0
    override fun email(): String {
        emailCounter++
        return email
    }

    private var nameCounter = 0
    override fun name(): String {
        nameCounter++
        return name
    }

    override fun userNotLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(idToken: String): Pair<Boolean, String> {
        TODO("Not yet implemented")
    }



    private var signOutList = mutableListOf<ClientWrapper>()
    fun checkSignOutCalledTimes(expected: Int) {
        assertEquals(expected, signOutList.size)
    }
    fun checkSignOutCalledWith(expected: ClientWrapper) {
        assertEquals(expected, signOutList.last())
    }
    override fun signOut(clientWrapper: ClientWrapper) {
        signOutList.add(clientWrapper)
    }
}