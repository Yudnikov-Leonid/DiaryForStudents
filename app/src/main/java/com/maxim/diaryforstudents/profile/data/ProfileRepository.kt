package com.maxim.diaryforstudents.profile.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.maxim.diaryforstudents.profile.presentation.ShowProfileCallback

interface ProfileRepository {
    suspend fun data(callback: ShowProfileCallback)
    fun signOut()
    class Base(
        private val cloudDataSource: ProfileCloudDataSource
    ) : ProfileRepository {
        override suspend fun data(callback: ShowProfileCallback) {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            val name = user.displayName!!
            cloudDataSource.getGrade(object : ClassNameCallback {
                override fun provide(className: String) {
                    callback.show(name, className, email)
                }
            })
        }

        override fun signOut() {
            cloudDataSource.signOut()
        }
    }
}

interface ClassNameCallback {
    fun provide(className: String)
}