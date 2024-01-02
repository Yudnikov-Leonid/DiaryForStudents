package com.maxim.diaryforstudents.profile

import com.maxim.diaryforstudents.fakes.FakeMyUser
import com.maxim.diaryforstudents.profile.data.GradeResult
import com.maxim.diaryforstudents.profile.data.ProfileCloudDataSource
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProfileRepositoryTest {
    private lateinit var repository: ProfileRepository
    private lateinit var cloudDataSource: FakeProfileCloudDataSource
    private lateinit var myUser: FakeMyUser

    @Before
    fun before() {
        cloudDataSource = FakeProfileCloudDataSource()
        myUser = FakeMyUser()
        repository = ProfileRepository.Base(cloudDataSource, myUser)
    }

    @Test
    fun test_sign_out() {
        repository.signOut()
        cloudDataSource.checkSignOutCalledTimes(1)
    }

    @Test
    fun test_data() = runBlocking {
        myUser.returnEmail("email")
        myUser.returnName("name")
        cloudDataSource.returnGetGrade(GradeResult.Teacher("teacher"))

        val actual = repository.data()
        val expected = Triple("name", GradeResult.Teacher("teacher"), "email")
        assertEquals(expected, actual)
        myUser.checkEmailCalledTimes(1)
        myUser.checkNameCalledTimes(1)
        cloudDataSource.checkGetGradeCalledTimes(1)
    }
}

private class FakeProfileCloudDataSource: ProfileCloudDataSource {
    private var sighOutCounter = 0
    fun checkSignOutCalledTimes(expected: Int) {
        assertEquals(expected, sighOutCounter)
    }
    override fun signOut() {
        sighOutCounter++
    }

    private var value: GradeResult? = null
    private var getGradeCounter = 0
    fun returnGetGrade(value: GradeResult) {
        this.value = value
    }
    fun checkGetGradeCalledTimes(expected: Int) {
        assertEquals(expected, getGradeCounter)
    }

    override suspend fun getGrade(): GradeResult {
        getGradeCounter++
        return value!!
    }
}