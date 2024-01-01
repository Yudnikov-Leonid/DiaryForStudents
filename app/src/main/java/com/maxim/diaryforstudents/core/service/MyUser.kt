package com.maxim.diaryforstudents.core.service

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.maxim.diaryforstudents.profile.data.ClientWrapper
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface MyUser {
    fun id(): String
    fun email(): String
    fun name(): String
    fun userNotLoggedIn(): Boolean
    suspend fun signIn(idToken: String): Pair<Boolean, String>
    fun signOut(clientWrapper: ClientWrapper)

    class Base(private val navigateToLogin: NavigateToLogin) : MyUser {
        private fun user(): FirebaseUser? {
            return if (userNotLoggedIn()) {
                navigateToLogin.navigate()
                null
            }
            else
                Firebase.auth.currentUser!!
        }

        override fun id() = Firebase.auth.uid!!
        override fun email() = user()?.email!!

        override fun name() = user()?.displayName!!
        override fun userNotLoggedIn() = Firebase.auth.currentUser == null

        override suspend fun signIn(idToken: String): Pair<Boolean, String> {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val signInWithCredential = Firebase.auth.signInWithCredential(credential)
            return handleSignIn(signInWithCredential)
        }

        override fun signOut(clientWrapper: ClientWrapper) {
            Firebase.auth.signOut()
            clientWrapper.signOut()
        }

        private suspend fun handleSignIn(task: Task<AuthResult>): Pair<Boolean, String> =
            suspendCoroutine { cont ->
                task.addOnSuccessListener {
                    cont.resume(Pair(true, ""))
                }.addOnFailureListener {
                    cont.resume(Pair(false, it.message ?: "error"))
                }
            }
    }
}

interface NavigateToLogin {
    fun navigate()
}