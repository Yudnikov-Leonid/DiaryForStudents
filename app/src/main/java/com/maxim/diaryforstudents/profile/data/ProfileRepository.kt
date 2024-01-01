package com.maxim.diaryforstudents.profile.data

import com.maxim.diaryforstudents.core.service.MyUser

interface ProfileRepository {
    suspend fun data(): Triple<String, GradeResult, String>
    fun signOut()
    class Base(
        private val cloudDataSource: ProfileCloudDataSource,
        private val myUser: MyUser
    ) : ProfileRepository {
        override suspend fun data(): Triple<String, GradeResult, String> {
            val email = myUser.email()
            val name = myUser.name()
            return Triple(name, cloudDataSource.getGrade(), email)
        }

        override fun signOut() {
            cloudDataSource.signOut()
        }
    }
}