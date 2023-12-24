package com.maxim.diaryforstudents.profile

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.maxim.diaryforstudents.R

interface ProfileRepository {
    fun data(): Pair<String, String>
    fun signOut()
    class Base(
        private val cloudDataSource: ProfileCloudDataSource
    ) : ProfileRepository {
        override fun data(): Pair<String, String> {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            val name = user.displayName!!
            return Pair(email, name)
        }

        override fun signOut() {
            cloudDataSource.signOut()
        }
    }
}