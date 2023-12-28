package com.maxim.diaryforstudents.profile.data

import com.google.android.gms.auth.api.signin.GoogleSignInClient

interface ClientWrapper {
    fun signOut()
    class Base(private val client: GoogleSignInClient) : ClientWrapper {
        override fun signOut() {
            client.signOut()
        }
    }
}