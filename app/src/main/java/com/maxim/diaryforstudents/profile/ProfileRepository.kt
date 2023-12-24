package com.maxim.diaryforstudents.profile

import com.google.firebase.Firebase
import com.google.firebase.auth.auth

interface ProfileRepository {
    fun data(): Pair<String, String>
    fun signOut()
    class Base : ProfileRepository {
        override fun data(): Pair<String, String> {
            val user = Firebase.auth.currentUser!!
            val email = user.email!!
            val name = user.displayName!!
            return Pair(email, name)
        }

        override fun signOut() {
            Firebase.auth.signOut()
        }
    }
}