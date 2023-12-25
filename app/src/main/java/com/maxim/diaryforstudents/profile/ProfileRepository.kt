package com.maxim.diaryforstudents.profile

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

interface ProfileRepository {
    suspend fun data(): Triple<String, String, String>
    fun signOut()
    class Base(
        private val cloudDataSource: ProfileCloudDataSource
    ) : ProfileRepository {
        override suspend fun data(): Triple<String, String, String> {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            val name = user.displayName!!
            val grade = cloudDataSource.getGrade()
            return Triple(name, grade, email)
        }

        override fun signOut() {
            cloudDataSource.signOut()
        }
    }
}