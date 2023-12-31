package com.maxim.diaryforstudents.profile.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

interface ProfileRepository {
    suspend fun data(): Triple<String, GradeResult, String>
    fun signOut()
    class Base(
        private val cloudDataSource: ProfileCloudDataSource
    ) : ProfileRepository {
        override suspend fun data(): Triple<String, GradeResult, String> {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            val name = user.displayName!!
            return Triple(name, cloudDataSource.getGrade(), email)
        }

        override fun signOut() {
            cloudDataSource.signOut()
        }
    }
}