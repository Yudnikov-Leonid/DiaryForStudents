package com.maxim.diaryforstudents.profile

import com.maxim.diaryforstudents.core.service.CloudClass
import com.maxim.diaryforstudents.core.service.CloudUser
import com.maxim.diaryforstudents.fakes.FakeLessonMapper
import com.maxim.diaryforstudents.fakes.FakeMyUser
import com.maxim.diaryforstudents.fakes.FakeService
import com.maxim.diaryforstudents.profile.data.ClientWrapper
import com.maxim.diaryforstudents.profile.data.GradeResult
import com.maxim.diaryforstudents.profile.data.ProfileCloudDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProfileCloudDataSourceTest {
    private lateinit var dataSource: ProfileCloudDataSource
    private lateinit var service: FakeService
    private lateinit var clientWrapper: FakeClientWrapper
    private lateinit var lessonMapper: FakeLessonMapper
    private lateinit var myUser: FakeMyUser

    @Before
    fun before() {
        myUser = FakeMyUser()
        lessonMapper = FakeLessonMapper("12")
        clientWrapper = FakeClientWrapper()
        service = FakeService()
        dataSource = ProfileCloudDataSource.Base(service, clientWrapper, lessonMapper, myUser)
    }

    @Test
    fun test_sign_out() {
        dataSource.signOut()
        myUser.checkSignOutCalledTimes(1)
        myUser.checkSignOutCalledWith(clientWrapper)
    }

    @Test
    fun test_get_grade_teacher() = runBlocking {
        myUser.returnId("someId")
        service.getReturn(
            mapOf(
                listOf("users", "someId") to listOf(
                    Pair(
                        "blablabla",
                        CloudUser("", "email", "name", "teacher", "algebra")
                    )
                )
            )
        )
        val actual = dataSource.getGrade()
        val expected = GradeResult.Teacher("algebra12")
        assertEquals(expected, actual)
        service.checkCalledTimes(1)
    }

    @Test
    fun test_get_grade_student() = runBlocking {
        myUser.returnId("someId")
        service.getReturn(
            mapOf(
                listOf("users", "someId") to listOf(
                    Pair(
                        "blablabla",
                        CloudUser("454", "email", "name", "student", "")
                    )
                ),
                listOf("classes", "454") to listOf(Pair("blablabla", CloudClass("className")))
            )

        )
        val actual = dataSource.getGrade()
        val expected = GradeResult.Student("className")
        assertEquals(expected, actual)
        service.checkCalledTimes(2)
    }

    @Test
    fun test_get_grade_empty() = runBlocking {
        myUser.returnId("someId")
        service.getReturn(
            mapOf(
                listOf("users", "someId") to
                        listOf(Pair("blablabla", CloudUser("", "email", "name", "teacher", "")))
            )
        )
        val actual = dataSource.getGrade()
        val expected = GradeResult.Empty
        assertEquals(expected, actual)
        service.checkCalledTimes(1)
    }
}

private class FakeClientWrapper : ClientWrapper {
    override fun signOut() {
        throw IllegalStateException("FakeClientWrapper#signOut not use in tests")
    }
}